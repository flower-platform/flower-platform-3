package org.flowerplatform.communication.progress_monitor.remote;

import org.flowerplatform.communication.stateful_service.IStatefulClientLocalState;

/**
 * State given by a ProgressMonitorStatefulClient when a subscription or unsubscription happens.
 * @author Sorin
 */
public class ProgressMonitorStatefulLocalClient implements IStatefulClientLocalState {
	
	public String title;
	
	/**
	 * @see ProgressMonitorStatefulClient#getCurrentStatefulClientLocalState()
	 */
	public boolean afterReconnect;
	
	/**
	 * If <code>true</code>, a cancel button will be displayed on client side 
	 * to allow cancellation.
	 * 
	 * @author Cristina
	 */
	public boolean allowCancellation;
	
	public ProgressMonitorStatefulLocalClient() {
	}
	
	/**
	 * Emulate a subscription made from server side
	 * @see ProgressMonitorStatefulService#createProgressMonitor() 
	 */
	public ProgressMonitorStatefulLocalClient(String title) {
		this.title = title;
	}
		
	public ProgressMonitorStatefulLocalClient(String title, boolean allowCancellation) {
		this(title);
		this.allowCancellation = allowCancellation;
	}
	
}
