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
package org.flowerplatform.communication.progress_monitor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.progress_monitor.remote.ProgressMonitorStatefulLocalClient;
import org.flowerplatform.communication.progress_monitor.remote.ProgressMonitorStatefulService;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sorin
 * 
 */
public class ProgressMonitor implements IProgressMonitor {
	
	public static final Logger logger = LoggerFactory.getLogger(ProgressMonitor.class);
	
	/**
	 * Public visibility for ProgressMonitorStatefulService to dispose when server stops.
	 * 
	 */
	public static final ScheduledExecutorService scheduler = CommunicationPlugin.getInstance().getScheduledExecutorServiceFactory().createScheduledExecutorService();
	
	private static final int UPDATE_CLIENT_PERIOD = 500; // TODO configurabil din properties

	/**
	 * 
	 */
	private static int lastUsedId = 0;
	
	private String title;
	
	/**
	 * 
	 */
	private String statefulClientId;
	
	/**
	 * Package visibility for ProgressMonitorStatefulService for client method invocation.
	 * 
	 */
	private CommunicationChannel channel;
	
	/**
	 * 
	 */
	private String lastName = "";
	
	/**
	 * 
	 */
	private double workUntilNow = 0;
	
	/**
	 * 
	 */
	private boolean canceled = false;
	
	private boolean terminated = false;
	
	/**
	 * Represents the repetitive task that updates the client.
	 * Referenced to stop when the operation is done.
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private ScheduledFuture updateProgressMonitorTask;

	public ProgressMonitor(CommunicationChannel channel, String title, String statefulClientId, boolean createdByServer) {
		this.channel = channel;
		this.title = title;
		this.statefulClientId = createdByServer ? "ServerProgressMonitor" + (++ lastUsedId) : statefulClientId;
	}
	
	/**
	 * Forces the client to create a ProgressMonitorStatefulClient. It returns the WebProgressMonitor
	 * associated to that stateful client.
	 */
	public static ProgressMonitor create(String title, CommunicationChannel channel) {
		return getService().createProgressMonitor(title, channel);
	}
	
	/**
	 * Creates a non-cancelable progress monitor (without cancel button displayed).
	 * @author Cristina
	 */
	public static ProgressMonitor createNonCancelableProgressMonitor(String title, CommunicationChannel channel) {
		return getService().createNonCancelableProgressMonitor(title, channel);
	}
	
	/**
	 * 
	 */
	public void beginTask(String name, int totalWork) {
		if (updateProgressMonitorTask != null) {
			throw new RuntimeException("Progress monitor already started!");
		}
		if (logger.isTraceEnabled())
			logger.trace("Web Progress Monitor " + statefulClientId + ", Begin name = " + name + " totalWork = " + totalWork);
			
		getService().beginProgressMonitor(channel, statefulClientId, name, totalWork);
		
		updateProgressMonitorTask = scheduler.scheduleWithFixedDelay( // Schedule repetitive task to update client
				new Runnable() {
			
					@Override
					public void run() {
						if (logger.isTraceEnabled())
							logger.trace("Web Progress Monitor " + statefulClientId + ", Update Client lastName = " + lastName + " workUntilNow = " + workUntilNow);

						getService().updateProgressMonitor(channel, statefulClientId, lastName, workUntilNow);
					}
				},
				UPDATE_CLIENT_PERIOD / 5, UPDATE_CLIENT_PERIOD,TimeUnit.MILLISECONDS);
	}


	/**
	 * 
	 */
	public void done() {
		if (terminated)
			return;
		terminated = true;
		
		if (logger.isTraceEnabled())
			logger.trace("Web Progress Monitor " + statefulClientId + ", Done");

		//cancel the task of the progress monitor  only if it started
		if (updateProgressMonitorTask != null) {
			updateProgressMonitorTask.cancel(true);
		}
		getService().closeProgressMonitor(channel, statefulClientId);
	}
	
	/**
	 * 
	 */
	public void setTaskName(String name) {
		subTask(name);
	}

	/**
	 * 
	 */
	public void worked(int work) {
		internalWorked(work);
	}

	/**
	 * 
	 */
	public void subTask(String name) {
		if (name == null || name.trim().equals(""))
			return; // Actually nothing to update.
		lastName = name;

		if (logger.isTraceEnabled())
			logger.trace("Web Progress Monitor " + statefulClientId + ", name = " + lastName);
	}

	/**
	 * 
	 */
	public void internalWorked(double work) {
		workUntilNow += work;

		if (logger.isTraceEnabled())
			logger.trace("Web Progress Monitor " + statefulClientId + ", workUntilNow = " + workUntilNow + ", added work = " + work);
	}

	/**
	 * 
	 */
	public void setCanceled(boolean value) {
		canceled = value;
	}

	/**
	 * 
	 */
	public boolean isCanceled() {
		return canceled;
	}
	
	/**
	 * Package visibility because it's used only by the StatefulService.
	 */
	public String getStatefulClientId() {
		return statefulClientId;
	}
	
	public CommunicationChannel getChannel() {
		return channel; 
	}
	
	public void setChannel(CommunicationChannel channel) {
		this.channel = channel; 
	}
	
	private static ProgressMonitorStatefulService getService() {
		return (ProgressMonitorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(ProgressMonitorStatefulService.SERVICE_ID);
	}
	
	@Override
	public String toString() {
		return ProgressMonitor.class.getSimpleName() + 
					 " statefulClientId = " 	+ statefulClientId + 
					", title = "				+ title + 
					", lastName = "   			+ lastName + 
					", workUntilNow = " 		+ workUntilNow + 
					", canceled = "   			+ canceled + 
					", done = " 				+ terminated;
	}
}