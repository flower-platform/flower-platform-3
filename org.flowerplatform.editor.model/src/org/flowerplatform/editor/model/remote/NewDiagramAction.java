/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.editor.model.remote;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.file.IFileAccessController;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.emf_model.notation.Diagram;

/**
 * @author Mariana Gheorghe
 * @author Sebastian Solomon
 */
public abstract class NewDiagramAction extends AbstractServerCommand {

	public String parentPath;
	public String name;
	// TODO CS temp for tests
	public boolean openAutomatically = true;

	@Override
	public void executeCommand() {
		Object diagram = createDiagram();
		if (diagram instanceof File) {
			openDiagram((File) diagram);
		}
	}

	protected Object createDiagram() {
		Object file;
		Object diagram;
		IFileAccessController fileAccessController = EditorPlugin.getInstance()
				.getFileAccessController();
		try {
			file = fileAccessController.getFile(parentPath);
			// go to parent dir if actions was executed on a file
			if (!EditorPlugin.getInstance().getFileAccessController()
					.isDirectory(file)) {

				file = EditorPlugin.getInstance().getFileAccessController()
						.getParentFile(file);
			}
			diagram = fileAccessController.createNewFile(file,
					getNextDiagram(file, name));
			fileAccessController.createNewFile(diagram);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		URI resourceURI = EditorModelPlugin.getInstance()
				.getModelAccessController().getURIFromFile(diagram);
		ResourceSet resourceSet = new ResourceSetImpl();
		Resource resource = resourceSet.createResource(resourceURI);
		resource.getContents().clear();
		resource.getContents().add(createDiagram(file, resourceSet));
		Map<Object, Object> options = EditorModelPlugin.getInstance()
				.getLoadSaveOptions();
		try {
			for (Resource r : resourceSet.getResources()) {
				r.save(options);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return diagram;
	}

	public void openDiagram(File diagram) {
		if (!openAutomatically) {
			return;
		}

		DiagramEditorStatefulService service = (DiagramEditorStatefulService) CommunicationPlugin
				.getInstance().getServiceRegistry().getService(getServiceId());
		service.subscribeClientForcefully(
				getCommunicationChannel(),
				EditorPlugin.getInstance().getFileAccessController()
						.getPath(diagram));
	}

	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 * @author Sebastian Solomon
	 */
	protected String getNextDiagram(Object parent, String name) {
		// before changing name, verify if the current one exists
		IFileAccessController fileAccessController = EditorPlugin.getInstance()
				.getFileAccessController();
		Object newFile = fileAccessController.createNewFile(parent, name);
		if (!fileAccessController.exists(newFile)) {
			return name;
		}
		int i = 0;
		boolean exists = true;
		StringBuilder builder = null;
		while (exists) {
			i++;
			builder = new StringBuilder(name);
			builder.insert(builder.indexOf("."), i);
			newFile = fileAccessController.createNewFile(parent,
					builder.toString());
			if (!fileAccessController.exists(newFile)) {
				exists = false;
			}
		}
		return builder.toString();
	}

	abstract protected Diagram createDiagram(Object file, ResourceSet resourceSet);
	
	abstract protected String getServiceId();
}
