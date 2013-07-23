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

import org.flowerplatform.blazeds.channel.BlazedsCommunicationChannel;

/**
 * Channel Observer Task scheduled to disconnect client due to no activity.
 * @author Sorin
 * @flowerModelElementId _FXWmIAlYEeK1a-Ic5xjg1Q
 */
public class NoActivityOnClientTask extends HeartbeatTask {
	
	public NoActivityOnClientTask(BlazedsCommunicationChannel channel) {
		super("Disconnect because No Activity happened on the Client", channel, noActivityOnClientInterval);
	}
	
	@Override
	public void runWithChannelLocked() {
		if (logger.isDebugEnabled())
			logger.debug("Client : {} did not have any activity for at least {} ms. The client will be disconnected.", channel, noActivityOnClientInterval);

		channel.disconnect();
	}
}