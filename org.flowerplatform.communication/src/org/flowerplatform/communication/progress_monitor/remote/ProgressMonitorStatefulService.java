package org.flowerplatform.communication.progress_monitor.remote;


import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.channel.ICommunicationChannelLifecycleListener;
import org.flowerplatform.communication.progress_monitor.ProgressMonitor;
import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;
import org.flowerplatform.communication.stateful_service.IStatefulServiceMXBean;
import org.flowerplatform.communication.stateful_service.InvokeStatefulClientMethodClientCommand;
import org.flowerplatform.communication.stateful_service.RegularStatefulService;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sorin
 * @flowerModelElementId _aKTkYBLfEeKIW4So6X04UQ
 */
public class ProgressMonitorStatefulService extends RegularStatefulService<CommunicationChannel, HashMap<String, ProgressMonitor>>
				implements IStatefulServiceMXBean, ICommunicationChannelLifecycleListener {

	public static final Logger logger = LoggerFactory.getLogger(ProgressMonitorStatefulService.class);
	
	/**
	 * @flowerModelElementId _3OUGIBLfEeKIW4So6X04UQ
	 */
	public static final String SERVICE_ID = "ProgressMonitorStatefulService";
	
	// TODO Sorin : De rezolvat : pe server cand se detecteaza deconectarea se pastreaza progressMonitors pentru operatiile care nu pot fi oprite.
	// Insa deconectarea pe server se detecteaza abia dupa 30 secunde, pe cand pe client se detecteaza imediat si se poate conecta inapoi.
	// In acest caz la subscribe in urma reconectarii progressMonitors inca nu se afla in acest map iar pe client o sa se inchida dialogul
	// desi exista asociat altui canal de comunicare care inca nu stie ca a fost terminat.
	private HashMap<String, ProgressMonitor> canceledProgressMonitorsFromDisconnect = new HashMap<String, ProgressMonitor>();
		
	///////////////////////////////////////////////////////////////
	// JMX Methods
	///////////////////////////////////////////////////////////////

	/**
	 * @flowerModelElementId _YKcd0BOBEeK1ssFHNoNwQg
	 */
	protected void printStatefulDataForClient(StringBuffer stringBuffer, String linePrefix, CommunicationChannel client, HashMap<String, ProgressMonitor> data) {
		super.printStatefulDataForClient(stringBuffer, linePrefix, client, data);
		
		for (ProgressMonitor progressMonitor : data.values())
			stringBuffer.append(linePrefix).append("   ").append(progressMonitor).append("\n");
	}
	
	@Override
	public Collection<String> getStatefulClientIdsForCommunicationChannel(CommunicationChannel communicationChannel) {
		HashMap<String, ProgressMonitor> channelProgressMonitors = clients.get(communicationChannel); 
		if (channelProgressMonitors == null)
			return Collections.emptyList();
		else
			return channelProgressMonitors.keySet();
	}

	///////////////////////////////////////////////////////////////
	// Normal methods
	///////////////////////////////////////////////////////////////
		
	/**
	 * @flowerModelElementId _1RkFkBLfEeKIW4So6X04UQ
	 */
	protected String getStatefulServiceId() {
		return SERVICE_ID;
	}

	/**
	 * @flowerModelElementId _Ah6YcBLgEeKIW4So6X04UQ
	 */
	public ProgressMonitorStatefulService() {
		clients = new ConcurrentHashMap<CommunicationChannel, HashMap<String, ProgressMonitor>>();
	}

	/**
	 * @flowerModelElementId _E0EKIBLgEeKIW4So6X04UQ
	 */
	public void dispose() {
		ProgressMonitor.scheduler.shutdown();
		if (!canceledProgressMonitorsFromDisconnect.isEmpty())
			logger.warn("The following progress monitors could not be stopped : " + canceledProgressMonitorsFromDisconnect.values());
	}

	/**
	 * @flowerModelElementId _DCyH0BLiEeKIW4So6X04UQ
	 */
	public void beginProgressMonitor(CommunicationChannel channel, String statefulClientId, String name, int totalWork) {
		invokeClientMethod(channel, statefulClientId, "beginProgressMonitor", new Object[] {name, totalWork});
	}

	/**
	 * @flowerModelElementId _NhneMBLiEeKIW4So6X04UQ
	 */
	public void updateProgressMonitor(CommunicationChannel channel, String statefulClientId, String name, double workUntilNow) {
		HashMap<String, ProgressMonitor> channelProgressMonitors = clients.get(channel);
		if (channelProgressMonitors != null) // Update needed to be done only while the client is present.
			invokeClientMethod(channel, statefulClientId, "updateProgressMonitor", new Object[] {name, workUntilNow});
	}

	/**
	 * Makes the client to hide it's dialog and unregister it's ProgressMonitorStatefulClient but wont unsubscribe from
	 * server because it is directly done here.
	 * @flowerModelElementId _fMyfcBLiEeKIW4So6X04UQ
	 */
	public void closeProgressMonitor(CommunicationChannel channel, String statefulClientId) {
		HashMap<String, ProgressMonitor> channelProgressMonitors = clients.get(channel);
		if (channelProgressMonitors != null) // Operation terminated before the client left.
			invokeClientMethod(channel, statefulClientId, "closeProgressMonitor", null);
		unsubscribe(new StatefulServiceInvocationContext(channel, null, statefulClientId), new ProgressMonitorStatefulLocalClient() /* to differentiate between server disconnect = null */);
	}

	/**
	 * The sending must be done instantly because otherwise when operations are done in the same thread with the channel, client updated wont be perceived
	 * until the operation finalizes (due to fact that update wait to travel with http response). 
	 */
	@Override
	protected void invokeClientMethod(CommunicationChannel statefulClientCommunicationChannel, String statefulClientId, String methodName, Object[] parameters) {
		statefulClientCommunicationChannel.sendCommandWithPush(new InvokeStatefulClientMethodClientCommand(statefulClientId, methodName, parameters));
	}

	/**
	 * Forces the client to create a ProgressMonitorStatefulClient. It returns the WebProgressMonitor
	 * associated to that stateful client.
	 */
	public ProgressMonitor createProgressMonitor(String title, CommunicationChannel channel) {
		return subscribeInternal(new StatefulServiceInvocationContext(channel), new ProgressMonitorStatefulLocalClient(title, true), true /* created by server */);
	}
	
	/**
	 * Creates a non-cancelable progress monitor (without cancel button displayed).
	 * @author Cristina
	 */
	public ProgressMonitor createNonCancelableProgressMonitor(String title, CommunicationChannel channel) {
		return subscribeInternal(new StatefulServiceInvocationContext(channel), new ProgressMonitorStatefulLocalClient(title, false), true /* created by server */);
	}
	
	/**
	 * Returns the {@link WebProgressMonitor} at which the client previously subscribed
	 * using the <code>progressMonitorStatefulClientId</code>. 
	 */
//	public ProgressMonitor getProgressMonitor(String progressMonitorStatefulClientId) {
//		CommunicationChannel channel = CommunicationChannel.threadLocalInstance.get();
//		HashMap<String, ProgressMonitor> channelProgressMonitors = clients.get(channel);
//		return channelProgressMonitors.get(progressMonitorStatefulClientId);
//	}

	///////////////////////////////////////////////////////////////
	// @RemoteInvocation methods
	///////////////////////////////////////////////////////////////
	
	@Override
	@RemoteInvocation
	public void subscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		subscribeInternal(context, (ProgressMonitorStatefulLocalClient) statefulClientLocalState, false /* created by client */);
	}
	
	/**
	 * Supports the following cases:
	 * - fake subscription by server which requests the client to create and register a new ProgressMonitorStatefulClient
	 * - subscription by client for a new progress monitor 
	 * - subscription by client after a reconnect which tries to recover the progress monitor if it could not be stopped or orders the client to close it's dialog.  
	 */
	private ProgressMonitor subscribeInternal(StatefulServiceInvocationContext context, ProgressMonitorStatefulLocalClient statefulClientLocalState, boolean subscribeByServer) {
		CommunicationChannel channel = (CommunicationChannel) context.getCommunicationChannel();
		
		HashMap<String, ProgressMonitor> channelProgressMonitors = clients.get(channel); 
		if (channelProgressMonitors == null)  {
			channelProgressMonitors = new HashMap<String, ProgressMonitor>();
			clients.put(channel, channelProgressMonitors);
		}

		ProgressMonitor progressMonitor = null;
		if (statefulClientLocalState.afterReconnect) { 
			progressMonitor = canceledProgressMonitorsFromDisconnect.remove(context.getStatefulClientId());
			if (progressMonitor == null) { // Could not recover client progress monitor
				closeProgressMonitor(context.getCommunicationChannel(), context.getStatefulClientId());
				return null;
			} else {
				progressMonitor.setChannel(context.getCommunicationChannel()); // Updating channel to the new one
				if (logger.isTraceEnabled()) 
					logger.trace("Restoring to {} with {}", getStatefulServiceId(), progressMonitor.getStatefulClientId());
			}
		} else { // First subscribe from server or client
			progressMonitor = new ProgressMonitor(channel, statefulClientLocalState.title, context.getStatefulClientId(), subscribeByServer);
			if (subscribeByServer) {
				channel.sendCommandWithPush(new CreateProgressMonitorStatefulClientCommand(statefulClientLocalState.title, progressMonitor.getStatefulClientId(), statefulClientLocalState.allowCancellation));
			}
		}

		logger.info("Subscribing to {} with client {}", getStatefulServiceId(), progressMonitor.getStatefulClientId());
		
		// New from server or client, or progress monitor could not be stopped.
		channelProgressMonitors.put(progressMonitor.getStatefulClientId(), progressMonitor);
		return progressMonitor;
	}
	
	/** 
	 * Supports the following cases :
	 * - channel destroyed - which cancels all progress monitors and remembers them as proposed to be canceled;
	 * - operation done - while a channel is present or not (work done by server while client still present or left)
	 * @flowerModelElementId _3XRU8BN7EeK1ssFHNoNwQg
	 */
	@RemoteInvocation
	public void unsubscribe(StatefulServiceInvocationContext context, IStatefulClientLocalState statefulClientLocalState) {
		HashMap<String, ProgressMonitor> channelProgressMonitors = clients.get(context.getCommunicationChannel());
		
		if (statefulClientLocalState == null) { // channel disconnect
			if (channelProgressMonitors == null) { // no progress monitors registered yet
				return;
			}
			logger.info("Unsubscribing from {} with {}", getStatefulServiceId(), channelProgressMonitors.keySet());

			for (ProgressMonitor progressMonitor : channelProgressMonitors.values()) 
				progressMonitor.setCanceled(true); // Just suggests terminating the operation.
			clients.remove(context.getCommunicationChannel());
			canceledProgressMonitorsFromDisconnect.putAll(channelProgressMonitors);
		} else { // operation done

			// verify if operation was while the client is present
			if (channelProgressMonitors != null && channelProgressMonitors.remove(context.getStatefulClientId()) != null) { // was anything removed from "channel's progress monitors list" ?
				if (logger.isTraceEnabled())
					logger.trace("Unsubscribing from {} with {} before client left the server", getStatefulServiceId(), context.getStatefulClientId());
				
			// or verify that the operation was done after the client left the server
			} else if (canceledProgressMonitorsFromDisconnect.remove(context.getStatefulClientId()) != null) { // was anything removed from "progress monitors proposed to be canceled" ?
				if (logger.isTraceEnabled())
					logger.warn("Unsubscribing from {} with {} after client left the server", getStatefulServiceId(), context.getStatefulClientId());
			}
			
			logger.info("Unsubscribing from {} with client {}", getStatefulServiceId(), context.getStatefulClientId());
		}
	}
	
	/**
	 * Updates the {@link WebProgressMonitor} so that the operation to know that is should terminate.
	 * Note: the operation may choose to ignore the #canceled flag.
	 * @flowerModelElementId _4i9BABLjEeKIW4So6X04UQ
	 */
	@RemoteInvocation
	public void attemptCancelProgressMonitor(StatefulServiceInvocationContext context) {
		HashMap<String, ProgressMonitor> channelProgressMonitors = clients.get(context.getCommunicationChannel()); 
		ProgressMonitor progressMonitor = channelProgressMonitors.get(context.getStatefulClientId());
		if (progressMonitor != null) // Operation may have already terminated when the client requests to cancel it.
			progressMonitor.setCanceled(true);
	}

	@Override
	public void communicationChannelCreated(CommunicationChannel webCommunicationChannel) {		
	}
	
}