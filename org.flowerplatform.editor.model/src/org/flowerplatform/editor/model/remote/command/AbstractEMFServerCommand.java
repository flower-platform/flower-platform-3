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