package org.flowerplatform.communication.tree.remote;

import org.flowerplatform.communication.stateful_service.IStatefulServiceMXBean;

/**
 * @author Cristina
 * @flowerModelElementId _slEa0BN0EeKR8sYuzDGiDQ
 */
public interface INodeInfoStatefulServiceMXBean extends IStatefulServiceMXBean {

	/**
	 * @flowerModelElementId _slf4oRN0EeKR8sYuzDGiDQ
	 */
	public String printNodeInfos();
	
	public String printTreeStatefulContext(String webCommunicationChannelIdFilter, String linePrefix);
	
}
