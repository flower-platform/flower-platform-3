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
package org.flowerplatform.communication.stateful_service;

import java.util.Collection;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.CommunicationChannelManager;

/**
 * @author Cristi
 * 
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
	 * 
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
	 * 
	 */
	@RemoteInvocation
	abstract public void subscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState);
	
	/**
	 * 
	 */
	@RemoteInvocation
	abstract public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState);

}