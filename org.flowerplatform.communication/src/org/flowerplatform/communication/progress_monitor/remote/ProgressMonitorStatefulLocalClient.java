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