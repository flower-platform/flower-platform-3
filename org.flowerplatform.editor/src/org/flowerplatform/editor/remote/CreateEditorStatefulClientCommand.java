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
package org.flowerplatform.editor.remote;

import org.flowerplatform.communication.command.AbstractClientCommand;

/**
 * @author Cristi
 * @author Mariana
 */
public class CreateEditorStatefulClientCommand extends
		AbstractClientCommand {

	private String editableResourcePath;
	
	private String editor;
	
	private boolean handleAsClientSubscription;

	public String getEditableResourcePath() {
		return editableResourcePath;
	}

	public void setEditableResourcePath(String editableResourcePath) {
		this.editableResourcePath = editableResourcePath;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	/**
	 * In case of model resources, used to determine if the open diagrams dialog
	 * will be displayed on client side.
	 * 
	 * @author Mariana
	 */
	public boolean isHandleAsClientSubscription() {
		return handleAsClientSubscription;
	}

	public void setHandleAsClientSubscription(boolean handleAsClientSubscription) {
		this.handleAsClientSubscription = handleAsClientSubscription;
	}

	public CreateEditorStatefulClientCommand(String editableResourcePath,
			String editor, boolean handleAsClientSubscription) {
		super();
		this.editableResourcePath = editableResourcePath;
		this.editor = editor;
		this.setHandleAsClientSubscription(handleAsClientSubscription);
	}

}