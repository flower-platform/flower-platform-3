package org.flowerplatform.editor.remote;

import org.flowerplatform.communication.stateful_service.IStatefulServiceMXBean;

/**
 * @flowerModelElementId _M-M_wgcIEeK49485S7r3Vw
 */
public interface IEditorStatefulClientMXBean extends IStatefulServiceMXBean {
	
	/**
	 * @flowerModelElementId _M-O08AcIEeK49485S7r3Vw
	 */
	public String printEditableResources();
	
	/**
	 * @flowerModelElementId _M-PcAQcIEeK49485S7r3Vw
	 */
	public int getNumberOfRegisteredLocksForNamedLockPool();
	
	public void unsubscribeClientForcefully(String communicationChannelId, String editableResourcePath);
	
	public void unsubscribeAllClientsForcefully(String editableResourcePath, boolean displayMessageToClient);
	
	public void subscribeClientForcefully(String communicationChannelId, String editableResourcePath);
	
	/**
	 * @author Mariana
	 */
	public void reloadEditableResource(String editableResourcePath, boolean displayMessageToClient);
	
}