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
package org.flowerplatform.blazeds.heartbeat;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

import org.flowerplatform.blazeds.channel.BlazedsCommunicationChannel;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.RegularStatefulService;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitors channels to disconnect them by checking if :
 * <ul>
 *  <li> they are still alive, by watching traveling data and heartbeat signal.
 *  <li> they are still active, by watching incoming traveling data (except hearbeat signal) and activity signal.
 * </ul> 
 * 
 * <p>
 * Service started and destroyed along with the server.
 * The service subscribes and unsubscribes for each channel that is created or destroyed.
 * 
 * @author Sorin
 * 
 */
public class HeartbeatStatefulService extends RegularStatefulService<BlazedsCommunicationChannel, HeartbeatDetails> implements ICommunicationChannelLifecycleListener { 
	
	/* package */ static final Logger logger = LoggerFactory.getLogger(HeartbeatStatefulService.class);
	
	public static final String SERVICE_ID = "heartbeatStatefulService";
	
	private static final String CLIENT_ID = "heartbeatStatefulClient";
	
	///////////////////////////////////////////////////////////////
	// JMX Methods
	///////////////////////////////////////////////////////////////
	
	/**
	 * 
	 */
	@Override
	protected void printStatefulDataForClient(StringBuffer stringBuffer, String linePrefix, BlazedsCommunicationChannel client, HeartbeatDetails data) {
		super.printStatefulDataForClient(stringBuffer, linePrefix, client, data);
		
		stringBuffer.append(linePrefix).append("   ").append(data.getClass().getSimpleName()).append("\n");
		stringBuffer.append(linePrefix).append("   ").append(data.noHearbeatFromClientTask).append("\n");
		stringBuffer.append(linePrefix).append("   ").append(data.warnAboutNoActivityTask).append("\n");
		stringBuffer.append(linePrefix).append("   ").append(data.noActivityOnClientTask).append("\n ");
	}
	
	public void executeTask_WarnAboutNoActivity(String communicationChannelId) {
		getDetailsFromChannelId(communicationChannelId).warnAboutNoActivityTask.timeout();
	}

	public void executeTask_NoActivityOnClient(String communicationChannelId) {
		getDetailsFromChannelId(communicationChannelId).noActivityOnClientTask.timeout();
	}
	
	private HeartbeatDetails getDetailsFromChannelId(String communicationChannelId) {
		BlazedsCommunicationChannel channel = (BlazedsCommunicationChannel) CommunicationPlugin.getInstance().getCommunicationChannelManager().getCommunicationChannelById(communicationChannelId);
		if (channel == null) {
			throw new IllegalArgumentException("BlazedsCommunicationChannel not found for id = " + communicationChannelId);
		}
		HeartbeatDetails details = clients.get(channel);
		if (details == null) {
			throw new IllegalArgumentException("ChannelObserverDetails not found for id = " + communicationChannelId);
		}
		return details;
	}

	@Override
	public Collection<String> getStatefulClientIdsForCommunicationChannel(CommunicationChannel communicationChannel) {
		return Collections.singleton(CLIENT_ID);
	}
	
	///////////////////////////////////////////////////////////////
	// Normal methods
	///////////////////////////////////////////////////////////////
		
	protected String getStatefulServiceId() {
		return SERVICE_ID;
	}
	
	public HeartbeatStatefulService() {
		HeartbeatProperties.initializeCommunicationProperties();
		clients = new ConcurrentHashMap<BlazedsCommunicationChannel, HeartbeatDetails>(); // 2 clients may arrive at the same time.
		CommunicationPlugin.getInstance().getCommunicationChannelManager().addCommunicationLifecycleListener(this);
	}

	public void dispose() {
		HeartbeatDetails.channelObserverTimeoutManager.shutdown();
	}	
	
	/**
	 * All incoming objects :
	 * <li>
	 * 	<ul> update last traveling data time stamp
	 *  <ul> update last client activity time stamp except for heartbeat signal
	 * </li>
	 * 
	 */
	public void notifyObjectReceived(BlazedsCommunicationChannel channel, Object object) {
		logObject(object, false);
		
		if (object instanceof InvokeServiceMethodServerCommand) { // check if heartbeat method to process it later.
			InvokeServiceMethodServerCommand command = (InvokeServiceMethodServerCommand) object;
			if (command.getServiceId().equals(SERVICE_ID) && command.getMethodName().equals("signalHeartbeat"))
				return; // ignore it, because it will be processed when invoking the service method.
		}
		
		HeartbeatDetails details = clients.get(channel);
		details.updateTasks(true /* update traveling data */, true /* update activity */);
	}

	/**
	 * All outgoing objects :
	 * <li>
	 *  <ul> update last traveling data time stamp
	 *  <ul> don't count as activity of client.
	 * </li>
	 * 
	 */
	public void notifyObjectSent(BlazedsCommunicationChannel channel, Object object) {
		logObject(object, true);
		
		HeartbeatDetails details = clients.get(channel);
		details.updateTasks(true /* update traveling data */, false /* no activity */);
	}
	
	private void logObject(Object object, boolean sending) {
		boolean channelObserverSignal = false;
		if (object == null)
			channelObserverSignal = true;
		else if (object instanceof InvokeServiceMethodServerCommand) {
			InvokeServiceMethodServerCommand command = (InvokeServiceMethodServerCommand) object;
			if (command.getServiceId().equals(SERVICE_ID)) 
				if (command.getMethodName().equals("signalHeartbeat") || command.getMethodName().equals("signalActivity"))
					channelObserverSignal = true; 
		}
		String logMessage = sending ? "Sent: {}" : "Received: {}";
		if (logger.isDebugEnabled() && !channelObserverSignal)
			logger.debug(logMessage, object);
		else if (logger.isTraceEnabled() && channelObserverSignal)
			logger.trace(logMessage, object);			
	}
	
	/**
	 * Opens a dialog on client side notifying that it will be disconnected in <code>secondsUntilDisconnect</code> seconds.
	 * 
	 */
	public void warnAboutNoActivity(BlazedsCommunicationChannel channel, long secondsUntilDisconnect) {
		invokeClientMethod(channel, CLIENT_ID, "warnAboutNoActivity", new Object[] { secondsUntilDisconnect });
	}
	
	protected HeartbeatDetails getDataFromStatefulClientLocalState(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		BlazedsCommunicationChannel channel = (BlazedsCommunicationChannel) context.getCommunicationChannel();
		
		if (context.getCommand() != null && "unsubscribe".equals(context.getCommand().getMethodName()))
			return clients.get(channel); // Return the already exiting one.
		
		return new HeartbeatDetails(channel);
	}
	
	/**
	 * Subscribes as soon as the channel is created.  
	 * 
	 */
	public void communicationChannelCreated(CommunicationChannel communicationChannel) {
		subscribe(new StatefulServiceInvocationContext(communicationChannel, null, null), null);
	}
	
	public void communicationChannelDestroyed(CommunicationChannel webCommunicationChannel) {
		unsubscribe(new StatefulServiceInvocationContext(webCommunicationChannel, null, null), null);
	}
	
	///////////////////////////////////////////////////////////////
	//@RemoteInvocation methods
	///////////////////////////////////////////////////////////////

	/**
	 * Update last traveling data time stamp.
	 * 
	 */
	@RemoteInvocation
	public void signalHeartbeat(StatefulServiceInvocationContext context) {
		HeartbeatDetails details = clients.get(context.getCommunicationChannel());
		details.updateTasks(true /* update traveling data */, false /* no activity */);
	}

	/**
	 * Update last client activity time stamp.
	 * Also count as updating last traveling data time stamp. 
	 * 
	 */
	@RemoteInvocation
	public void signalActivity(StatefulServiceInvocationContext context) {
		HeartbeatDetails details = clients.get(context.getCommunicationChannel());
		details.updateTasks(true /* update traveling data */, true /* update activity */);
	}
	
	
	@Override
	@RemoteInvocation
	public void subscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		if (context.getStatefulClientId() != null) // Subscription has already been done when the channel was created.
			return;
		
		super.subscribe(context, statefulClientLocalState);
		
		HeartbeatDetails details = clients.get(context.getCommunicationChannel());
		details.schedule();
	}

	@Override
	@RemoteInvocation
	public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		HeartbeatDetails details = clients.get(context.getCommunicationChannel());
		details.unschedule();

		super.unsubscribe(context, statefulClientLocalState);
	}
}