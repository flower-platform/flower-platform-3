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

import static org.flowerplatform.blazeds.heartbeat.HeartbeatStatefulService.logger;
import static org.flowerplatform.blazeds.heartbeat.HeartbeatProperties.CLIENT_NO_ACTIVITY_PERIOD;
import static org.flowerplatform.blazeds.heartbeat.HeartbeatProperties.SERVER_HEARTBEAT_PERIOD;
import static org.flowerplatform.blazeds.heartbeat.HeartbeatProperties.WARN_ABOUT_NO_ACTIVITY_INTERVAL;
import static java.lang.Integer.valueOf;

import java.text.DecimalFormat;
import java.util.concurrent.Future;

import org.flowerplatform.blazeds.channel.BlazedsCommunicationChannel;
import org.flowerplatform.common.CommonPlugin;

import flex.messaging.util.TimeoutAbstractObject;

/**
 * Channel Observer task that can run logic on a locked channel.
 * Usefull for not disposing while processing a new object.
 * @author Sorin
 * @flowerModelElementId _1puFEAlWEeK1a-Ic5xjg1Q
 */
public abstract class HeartbeatTask extends TimeoutAbstractObject {

	// Easier to access this way.
	protected static long noHeartbeatFromClientInterval;
	protected static long warnAboutNoActivityInterval;
	protected static long noActivityOnClientInterval;
	
	static {
		noHeartbeatFromClientInterval = valueOf(CommonPlugin.getInstance().getFlowerWebProperties()
				.getProperty(SERVER_HEARTBEAT_PERIOD));
		warnAboutNoActivityInterval = valueOf(CommonPlugin.getInstance().getFlowerWebProperties()
				.getProperty(WARN_ABOUT_NO_ACTIVITY_INTERVAL));
		noActivityOnClientInterval = valueOf(CommonPlugin.getInstance().getFlowerWebProperties()
				.getProperty(CLIENT_NO_ACTIVITY_PERIOD));			
	} 
	
	private boolean taskExecuted = false;
	
	private String name;

	protected BlazedsCommunicationChannel channel;

	public HeartbeatTask(String name, BlazedsCommunicationChannel channel, long timeoutPeriod) {
		this.name = name;
		this.channel = channel;
		
		setTimeoutPeriod(timeoutPeriod);
		updateLastUse();
	}
	
	@Override
	final public void timeout() {
		if (taskExecuted)
			return;
		try {
			// All disposing operations must be done by blocking the channel because otherwise
			// an object may be processed while disposing a channel.
			synchronized (channel.lock) { 
				if (channel.isDisposed())
					return;
				runWithChannelLocked();
			}
			taskExecuted = true;
		} catch (Exception e) {
			logger.error("Problem kicking (warning) out client.", e);
		}		
	}
	
	protected abstract void runWithChannelLocked();
	
	@SuppressWarnings("rawtypes")
	@Override
	public void setTimeoutFuture(Future timeoutFuture) {
		super.setTimeoutFuture(timeoutFuture);
		taskExecuted = false;
	}

	/**
	 * Because a blazeDS task doesn't know if it was executed or not, we keep this flag.
	 * It is needed to reschedule after {@link WarnAboutNoActivityTask} has executed and the client
	 * sent a signal of activity. 
	 * @return
	 */
	public boolean isTaskExecuted() {
		return taskExecuted;
	}
	
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.### s");
		double timeoutPeriod = getTimeoutPeriod() / 1000.0;
		double untilTimeout = (getLastUse() + getTimeoutPeriod() - System.currentTimeMillis()) / 1000.0;
		return "Task '" + name + "' : timeout period = " + df.format(timeoutPeriod) + ", until timeout = " + df.format(untilTimeout);
	}
}