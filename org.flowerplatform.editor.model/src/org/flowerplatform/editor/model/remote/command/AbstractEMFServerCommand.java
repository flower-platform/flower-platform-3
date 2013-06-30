package org.flowerplatform.editor.model.remote.command;

import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.editor.remote.EditableResourceClient;

public abstract class AbstractEMFServerCommand extends AbstractServerCommand {

	private DiagramEditorStatefulService editorStatefulService;
	
	private StatefulServiceInvocationContext invocationContext;
	
	private DiagramEditableResource editableResource;
	
	private EditableResourceClient editableResourceClient;

	public DiagramEditorStatefulService getEditorStatefulService() {
		return editorStatefulService;
	}

	public void setEditorStatefulService(DiagramEditorStatefulService editorStatefulService) {
		this.editorStatefulService = editorStatefulService;
	}

	public StatefulServiceInvocationContext getInvocationContext() {
		return invocationContext;
	}

	public void setInvocationContext(StatefulServiceInvocationContext invocationContext) {
		this.invocationContext = invocationContext;
	}

	public DiagramEditableResource getEditableResource() {
		return editableResource;
	}

	public void setEditableResource(DiagramEditableResource editableResource) {
		this.editableResource = editableResource;
	}

	public EditableResourceClient getEditableResourceClient() {
		return editableResourceClient;
	}

	public void setEditableResourceClient(EditableResourceClient editableResourceClient) {
		this.editableResourceClient = editableResourceClient;
	}
	
}
