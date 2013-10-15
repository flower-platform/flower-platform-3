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
package org.flowerplatform.codesync.code.javascript;

import java.io.File;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.IDragOnDiagramHandler;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavascriptDragOnDiagramHandler implements IDragOnDiagramHandler {

	@Override
	public boolean handleDragOnDiagram(Collection<?> draggedObjects, Diagram diagram, View viewUnderMouse, Object layoutHint, CommunicationChannel communicationChannel) {
		for (Object object : draggedObjects) {
			File resource = (File) EditorPlugin.getInstance().getFileAccessController().getFile((String) object);
			File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile(resource);
			
			if (!acceptDraggedObject(resource)) {
				return false;
			}
			
			CodeSyncElement cse = CodeSyncCodePlugin.getInstance().getCodeSyncElement(project, resource, CodeSyncCodeJavascriptPlugin.TECHNOLOGY, communicationChannel, false);
			
			Node node = NotationFactory.eINSTANCE.createNode();
			node.setViewType("file");
			node.setDiagrammableElement(cse);
			
			Bounds bounds = NotationFactory.eINSTANCE.createBounds();
			bounds.setX(200);
			bounds.setY(200);
			bounds.setHeight(100);
			bounds.setWidth(100);
			node.setLayoutConstraint(bounds);
			diagram.getPersistentChildren().add(node);
			
		}
		return true;
	}
	
	private boolean acceptDraggedObject(Object object) {
		return (object instanceof IFile && ((IFile) object).getFileExtension().equals(CodeSyncCodeJavascriptPlugin.TECHNOLOGY));
	}

}
