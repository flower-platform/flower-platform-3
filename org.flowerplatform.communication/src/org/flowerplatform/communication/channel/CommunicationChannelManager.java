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
package org.flowerplatform.communication.channel;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;

import org.flowerplatform.common.log.AuditDetails;
import org.flowerplatform.common.log.LogUtil;
import org.flowerplatform.common.util.RunnableWithParam;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.command.IServerCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 */
public class CommunicationChannelManager {

	public static Logger logger = LoggerFactory.getLogger(CommunicationChannelManager.class);
	
	public static final String LOGOUT_AUDIT_CATEGORY = "LOGOUT";
	
	/**
	 * It's synchronized because of the methods that iterate on it. Iterating this map
	 * should be done rarely, as this blocks the connection/disconnection of new clients.
	 * 
	 * @author Cristi
	 * @see #iterateCommunicationChannels(RunnableWithParam)
	 * 
	 */
	protected Map<Object, CommunicationChannel> idToCommunicationChannelMap = Collections.synchronizedMap(new HashMap<Object, CommunicationChannel>());
	
	/**
	 * 
	 */
	private List<ICommunicationChannelLifecycleListener> communicationLifecycleListeners = new ArrayList<ICommunicationChannelLifecycleListener>();
	
	/**
	 * Invoked when a new client connects.
	 * 
	 * <p>
	 * Creates a new {@link WebCommunicationChannel}, adds
	 * it in the map and registers this as a destroy listener.
	 * 
	 */
	public void messageClientCreated(Object communicationChannelId, CommunicationChannel communicationChannel) {
		AuditDetails auditDetails = new AuditDetails(logger, "LOGIN");
		
		idToCommunicationChannelMap.put(communicationChannelId, communicationChannel);
		if (logger.isDebugEnabled())
			logger.debug("------->>>> CLIENT " + communicationChannelId + " ADDED size = " + idToCommunicationChannelMap.size());
		// notify all listeners
		for (ICommunicationChannelLifecycleListener listener : communicationLifecycleListeners) {
			try {
				listener.communicationChannelCreated(communicationChannel);
			} catch (Throwable e) {
				// We catch it but continue so that the system doesn't trip.
				logger.error("Exception thrown by a webCommunicationChannelCreated() handler.", e); 
			}
		}
		
		LogUtil.audit(auditDetails);
	}
	
	/**
	 * Invoked when a client disconnects (probably due to a time out).
	 * 
	 * @author Cristi
	 * 
	 */
	public void messageClientDestroyed(Object communicationChannelId) {
		CommunicationChannel communicationChannel = idToCommunicationChannelMap.remove(communicationChannelId);
		communicationChannel.dispose();
		AuditDetails auditDetails = new AuditDetails(logger, LOGOUT_AUDIT_CATEGORY, communicationChannel.getCachedUserId());
		
		if (logger.isDebugEnabled())
			logger.debug("------->>>> CLIENT " + communicationChannelId + " REMOVED size = " + idToCommunicationChannelMap.size());
		
		// notify all listeners, with a copy
		for (ICommunicationChannelLifecycleListener listener : communicationLifecycleListeners) {
			try {
				listener.communicationChannelDestroyed(communicationChannel);
			} catch (Throwable e) {
				// We catch it but continue so that the system doesn't trip.
				logger.error("Exception thrown by a communicationChannelDestroyed() handler.", e); 
			} 
		}
		
		LogUtil.audit(auditDetails);
	}
	
	protected void handleReceivedObject(CommunicationChannel communicationChannel, Object object) {
		IServerCommand command = (IServerCommand) object;
		command.setCommunicationChannel(communicationChannel);
		command.executeCommand();
	}
	
	/**
	 * This method is involved in the behavior of responding to the client side with objects to be processed.
	 * When this method returns it will use the communication channel of the owner of the message in order to obtain deferred objects
	 * that can be sent as a http response.
	 * 
	 * @see WebCommunicationChannel
	 * @see WebCommunicationChannel#sendStackedObjects()
	 * 
	 */
	public Object handleReceivedObject(Object messageClientId, IPrincipal principal, final Object object) {
		final CommunicationChannel communicationChannel = idToCommunicationChannelMap.get(messageClientId);
		try {
			CommunicationPlugin.tlCurrentPrincipal.set(principal);
		
			// Message arrived too late, a channel for corresponding client does not exist anymore.
			if (communicationChannel == null) 
				return null;
			
			PrivilegedAction<Void> priviledgedAction = new PrivilegedAction<Void>() {
				@Override
				public Void run() {
					handleReceivedObject(communicationChannel, object);
					return null;
				}
			};
	
			communicationChannel.handleReceivedObjectWillStart(object);
			Object response = null;
			try {
				if (principal == null) {
					priviledgedAction.run();
				} else {
					Subject.doAsPrivileged(principal.getSubject(), priviledgedAction, null);
				}
			} finally {
				response = communicationChannel.handleReceivedObjectEnded(object);
			}
			return response;
		} finally {
			CommunicationPlugin.tlCurrentPrincipal.set(null);
		}
	}
	
	public void addWebCommunicationLifecycleListener(ICommunicationChannelLifecycleListener listener) {
		communicationLifecycleListeners.add(listener);
	}
	
	/**
	 * 
	 */
	public void removeWebCommunicationLifecycleListener(ICommunicationChannelLifecycleListener listener) {
		communicationLifecycleListeners.remove(listener);
	}
	
	/**
	 * Iterates on all the communication channels. This method should be used rarely, because it locks the
	 * {@link #idToCommunicationChannelMap}, meaning that will block user connection/disconnection.
	 * In most cases, there is for sure another way of achieving the task, than iterating on all communication
	 * channels.
	 * 
	 * <p>
	 * At the moment of writing, this is used by JMX methods and when a User is modified, so it is not often used.
	 * 
	 * @author Cristi
	 * @see #idToCommunicationChannelMap
	 * @param runnableWithParam - Takes the current {@link WebCommunicationChannel} as parameter. If the runnable
	 * 		returns a non <code>null</code> value => the iteration stops and this method returns it.
	 * @return What the runnable has found, or <code>null</code>.
	 */
	public <R> R iterateCommunicationChannels(RunnableWithParam<R, CommunicationChannel> runnableWithParam) {
		synchronized (idToCommunicationChannelMap) {
			for (CommunicationChannel channel : idToCommunicationChannelMap.values()) {
				R result = runnableWithParam.run(channel);
				if (result != null) {
					return result;
				}
			}
			return null;
		}
	}

	public CommunicationChannel getCommunicationChannelById(Object communicationChannelId) {
		return idToCommunicationChannelMap.get(communicationChannelId);
	}
}