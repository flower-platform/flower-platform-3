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
 * @flowerModelElementId _e3-igBLhEeKIW4So6X04UQ
 */
public class ProgressMonitor implements IProgressMonitor {
	
	public static final Logger logger = LoggerFactory.getLogger(ProgressMonitor.class);
	
	/**
	 * Public visibility for ProgressMonitorStatefulService to dispose when server stops.
	 * @flowerModelElementId _KlRaIBLoEeKIW4So6X04UQ
	 */
	public static final ScheduledExecutorService scheduler = CommunicationPlugin.getInstance().getScheduledExecutorServiceFactory().createScheduledExecutorService();
	
	private static final int UPDATE_CLIENT_PERIOD = 500; // TODO configurabil din properties

	/**
	 * @flowerModelElementId _CFF7EBLjEeKIW4So6X04UQ
	 */
	private static int lastUsedId = 0;
	
	private String title;
	
	/**
	 * @flowerModelElementId _Gxc8gBLjEeKIW4So6X04UQ
	 */
	private String statefulClientId;
	
	/**
	 * Package visibility for ProgressMonitorStatefulService for client method invocation.
	 * @flowerModelElementId _ksphgBN-EeK1ssFHNoNwQg
	 */
	private CommunicationChannel channel;
	
	/**
	 * @flowerModelElementId _bvo0YBLoEeKIW4So6X04UQ
	 */
	private String lastName = "";
	
	/**
	 * @flowerModelElementId _e-dg0BLoEeKIW4So6X04UQ
	 */
	private double workUntilNow = 0;
	
	/**
	 * @flowerModelElementId _ztx3QBLiEeKIW4So6X04UQ
	 */
	private boolean canceled = false;
	
	private boolean terminated = false;
	
	/**
	 * Represents the repetitive task that updates the client.
	 * Referenced to stop when the operation is done.
	 * @flowerModelElementId _zB9EUBLoEeKIW4So6X04UQ
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
	 * @flowerModelElementId _myeecBLiEeKIW4So6X04UQ
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
	 * @flowerModelElementId _oWkHYBLiEeKIW4So6X04UQ
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
	 * @flowerModelElementId _qo7LkBLiEeKIW4So6X04UQ
	 */
	public void setTaskName(String name) {
		subTask(name);
	}

	/**
	 * @flowerModelElementId _tvhwIBLiEeKIW4So6X04UQ
	 */
	public void worked(int work) {
		internalWorked(work);
	}

	/**
	 * @flowerModelElementId _sCQWABLiEeKIW4So6X04UQ
	 */
	public void subTask(String name) {
		if (name == null || name.trim().equals(""))
			return; // Actually nothing to update.
		lastName = name;

		if (logger.isTraceEnabled())
			logger.trace("Web Progress Monitor " + statefulClientId + ", name = " + lastName);
	}

	/**
	 * @flowerModelElementId _vQU0gBLiEeKIW4So6X04UQ
	 */
	public void internalWorked(double work) {
		workUntilNow += work;

		if (logger.isTraceEnabled())
			logger.trace("Web Progress Monitor " + statefulClientId + ", workUntilNow = " + workUntilNow + ", added work = " + work);
	}

	/**
	 * @flowerModelElementId _xSrt0BLiEeKIW4So6X04UQ
	 */
	public void setCanceled(boolean value) {
		canceled = value;
	}

	/**
	 * @flowerModelElementId _yo89MBLiEeKIW4So6X04UQ
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