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

import com.crispico.flower.mp.codesync.base.communication.CodeSyncEditorStatefulService;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;

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
					CodeSyncCodePlugin.getInstance().getDragOnDiagramHandler().handleDragOnDiagram(Arrays.asList(resource), null, null, null, client.getCommunicationChannel());
				}
			}
		}
	}
}
