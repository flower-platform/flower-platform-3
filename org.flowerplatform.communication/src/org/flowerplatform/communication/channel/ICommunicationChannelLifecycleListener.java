package org.flowerplatform.communication.channel;

/**
 * @author Cristi
 */
public interface ICommunicationChannelLifecycleListener {
	
	public void webCommunicationChannelCreated(CommunicationChannel webCommunicationChannel);

	public void webCommunicationChannelDestroyed(CommunicationChannel webCommunicationChannel);
}