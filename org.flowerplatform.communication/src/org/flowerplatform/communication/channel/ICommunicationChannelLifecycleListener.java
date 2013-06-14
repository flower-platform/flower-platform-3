package org.flowerplatform.communication.channel;

/**
 * @author Cristi
 */
public interface ICommunicationChannelLifecycleListener {
	
	public void communicationChannelCreated(CommunicationChannel webCommunicationChannel);

	public void communicationChannelDestroyed(CommunicationChannel webCommunicationChannel);
}