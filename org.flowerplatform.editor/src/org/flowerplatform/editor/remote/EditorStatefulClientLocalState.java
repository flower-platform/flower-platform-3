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
