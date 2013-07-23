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

import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;

/**
 * @author Cristi
 * @author Mariana
 */
public class EditorStatefulClientLocalState implements
		IStatefulClientLocalState {

	private String editableResourcePath;
	
	private boolean forcingSubscriptionFromServer;
	
	private boolean handleAsClientSubscription;

	public EditorStatefulClientLocalState() {
		super();
	}

	public EditorStatefulClientLocalState(String editableResourcePath) {
		super();
		this.editableResourcePath = editableResourcePath;
	}

	public String getEditableResourcePath() {
		return editableResourcePath;
	}

	public void setEditableResourcePath(String editableResourcePath) {
		this.editableResourcePath = editableResourcePath;
	}

	/**
	 * Set to <code>true</code> if the request comes from 
	 * {@link EditorSupport#navigateFriendlyEditableResourcePathList()}.
	 * 
	 * @see CreateEditorStatefulClientCommand#isHandleAsClientSubscription()
	 * 
	 * @author Mariana
	 */
	public boolean isForcingSubscriptionFromServer() {
		return forcingSubscriptionFromServer;
	}

	public void setForcingSubscriptionFromServer(
			boolean forcingSubscriptionFromServer) {
		this.forcingSubscriptionFromServer = forcingSubscriptionFromServer;
	}

	public boolean isHandleAsClientSubscription() {
		return handleAsClientSubscription;
	}

	public void setHandleAsClientSubscription(boolean handleAsClientSubscription) {
		this.handleAsClientSubscription = handleAsClientSubscription;
	}

}