package org.flowerplatform.codesync.remote;

import java.util.Map;

import org.flowerplatform.communication.stateful_service.RemoteInvocation;


public interface CodeSyncDiagramOperationsService1 {

	/**
	 * @return ID of the view pointing to the newly added element.
	 */
	@RemoteInvocation
	String addNew(String diagramId, String viewIdOfParent, String codeSyncType, Map<String, Object> parameters);
	
	@RemoteInvocation
	String getInplaceEditorText(String viewId);
	
	@RemoteInvocation
	void setInplaceEditorText(String viewId, String text);
	
}
