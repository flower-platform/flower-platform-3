package org.flowerplatform.communication.stateful_service;

import java.util.Collection;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.CommunicationChannelManager;

/**
 * @author Cristi
 * @flowerModelElementId _x9QTkAJ1EeKGLqam5SXwYg
 */
abstract public class StatefulService {

	///////////////////////////////////////////////////////////////
	// JMX Methods
	///////////////////////////////////////////////////////////////

	public void unsubscribeClientForcefully(String communicationChannelId) {
		CommunicationChannel channel = CommunicationPlugin.getInstance().getCommunicationChannelManager().getCommunicationChannelById(communicationChannelId);
		if (channel == null) {
			throw new IllegalArgumentException("WebCommunicationChannel not found for id = " + communicationChannelId);
		}
		for (String statefulClientId : getStatefulClientIdsForCommunicationChannel(channel)) {
			invokeClientMethod(channel, statefulClientId, "unsubscribedForcefully", null);
		}
		communicationChannelDestroyed(channel);
	}
	
	public Collection<String> getStatefulClientIdsForCommunicationChannel(CommunicationChannel communicationChannel) {
		return null;
	}
	
	///////////////////////////////////////////////////////////////
	// Normal methods
	///////////////////////////////////////////////////////////////

	/**
	 * @flowerModelElementId _M8aQAAcIEeK49485S7r3Vw
	 */
	protected void invokeClientMethod(CommunicationChannel statefulClientCommunicationChannel, String statefulClientId, String methodName, Object[] parameters) {
		statefulClientCommunicationChannel.appendOrSendCommand(new InvokeStatefulClientMethodClientCommand(statefulClientId, methodName, parameters));
	}

	/**
	 * This method has been pseudo-moved to project .mp from .mp.web to satisfy
	 * dependency from {@link #unsubscribeClientForcefully(String)}.
	 * 
	 * @param webCommunicationChannel
	 */
	public void communicationChannelDestroyed(CommunicationChannel webCommunicationChannel) {
		// Does nothing 
	}
	
	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////

	/**
	 * @flowerModelElementId _1bBa8AJ1EeKGLqam5SXwYg
	 */
	@RemoteInvocation
	abstract public void subscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState);
	
	/**
	 * @flowerModelElementId _M8iy4AcIEeK49485S7r3Vw
	 */
	@RemoteInvocation
	abstract public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState);

}