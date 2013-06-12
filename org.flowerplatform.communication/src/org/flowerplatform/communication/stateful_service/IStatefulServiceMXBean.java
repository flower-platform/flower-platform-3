package org.flowerplatform.communication.stateful_service;

/**
 * @flowerModelElementId _M7PyYAcIEeK49485S7r3Vw
 */
public interface IStatefulServiceMXBean {
	
	public String printStatefulDataPerCommunicationChannel(String communicationChannelIdFilter, String linePrefix);
	
	public void unsubscribeClientForcefully(String communicationChannelId);

}
