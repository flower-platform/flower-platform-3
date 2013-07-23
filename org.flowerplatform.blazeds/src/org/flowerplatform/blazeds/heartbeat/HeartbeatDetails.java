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

import org.flowerplatform.blazeds.channel.BlazedsCommunicationChannel;

import flex.messaging.util.TimeoutManager;

/**
 * @author Sorin
 * @flowerModelElementId _ZozYAAlWEeK1a-Ic5xjg1Q
 */
public class HeartbeatDetails {
	
	/**
	 * Package visibility for ChannelObserverStatefulService to dispose it 
	 */ 
	static final TimeoutManager channelObserverTimeoutManager = new TimeoutManager();
	
	private BlazedsCommunicationChannel channel;

	/* package */ /**
	 * @flowerModelElementId _lchy0AlYEeK1a-Ic5xjg1Q
	 */
	HeartbeatTask noHearbeatFromClientTask;
	
	/* package */ HeartbeatTask warnAboutNoActivityTask;
	
	/* package */ HeartbeatTask noActivityOnClientTask;
	
	/**
	 * Creates tasks for a client. 
	 */
	public HeartbeatDetails(BlazedsCommunicationChannel channel) {
		this.channel = channel;
		
		noHearbeatFromClientTask = new NoHeartbeatFromClientTask(channel);
		warnAboutNoActivityTask = new WarnAboutNoActivityTask(channel);
		noActivityOnClientTask = new NoActivityOnClientTask(channel);
	}
	
	/**
	 * Schedules tasks for a client according to the timeout period set inside the tasks.
	 */
	public void schedule() {
		channelObserverTimeoutManager.scheduleTimeout(noHearbeatFromClientTask);
		channelObserverTimeoutManager.scheduleTimeout(warnAboutNoActivityTask);
		channelObserverTimeoutManager.scheduleTimeout(noActivityOnClientTask);
	}

	/**
	 * Depending on the flag parameters update channel observer tasks.
	 * @flowerModelElementId _Di2iVQ0sEeKBbcAV0j7rNw
	 */
	public void updateTasks(boolean updateLastTravelingDataTimeStamp, boolean updateLastClientActivityTimeStamp) {
		// Handle channel processing only after locking it.
		synchronized (channel.lock) {
			if (channel.isDisposed())
				return;

			if (updateLastTravelingDataTimeStamp) {
				noHearbeatFromClientTask.updateLastUse();
			}
			if (updateLastClientActivityTimeStamp) {
				noActivityOnClientTask.updateLastUse();
				warnAboutNoActivityTask.updateLastUse();
				if (warnAboutNoActivityTask.isTaskExecuted()) { // Task was already executed, we need to schedule again because activity signal was received.
					channelObserverTimeoutManager.scheduleTimeout(warnAboutNoActivityTask);
				}
			}
		}
	}
	
	/**
	 * Unschedules tasks for a client.
	 */
	public void unschedule() {
		noHearbeatFromClientTask.cancelTimeout();
		warnAboutNoActivityTask.cancelTimeout();
		noActivityOnClientTask.cancelTimeout();
	}
}