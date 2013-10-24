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
package com.crispico.flower.mp.codesync.code.java;

import java.util.Arrays;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.remote.EditableResource;
import org.flowerplatform.editor.remote.EditableResourceClient;
import org.flowerplatform.editor.remote.EditorStatefulService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;

/**
 * @author Mariana
 */
public class JavaResourceChangeListener implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		processDelta(event.getDelta());
	}
	
	private void processDelta(IResourceDelta deltaToProcess) {
		for (IResourceDelta delta : deltaToProcess.getAffectedChildren()) {
			processDelta(delta);
		}
		
		IResource resource = deltaToProcess.getResource();
		if (resource.getProject() != null) {
			String path = resource.getProject().getFullPath().toString();
			EditorStatefulService service = (EditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncEditorStatefulService.SERVICE_ID);
			EditableResource editableResource = service.getEditableResource(path);
			if (editableResource != null) {
				for (EditableResourceClient client : editableResource.getClients()) {
//					CodeSyncPlugin.getInstance().getDragOnDiagramHandler().handleDragOnDiagram(Arrays.asList(resource), null, null, null, client.getCommunicationChannel());
				}
			}
		}
	}
}