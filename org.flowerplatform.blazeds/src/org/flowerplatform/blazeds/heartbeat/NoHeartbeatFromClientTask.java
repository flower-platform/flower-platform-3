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
 * Channel Observer Task scheduled to disconnect client because it did not signal it's existance.
 * @author Sorin
 * @flowerModelElementId _qnl-8AlXEeK1a-Ic5xjg1Q
 */
public class NoHeartbeatFromClientTask extends HeartbeatTask {

	public NoHeartbeatFromClientTask(BlazedsCommunicationChannel channel) {
		super("Disconnect because No Heartbeat received from the Client", channel, noHeartbeatFromClientInterval);
	}

	@Override
	public void runWithChannelLocked() {
		if (logger.isDebugEnabled())
			logger.debug("Client : {} did not signal it's existance for at least {} ms. The client will be disconnected.", channel, noHeartbeatFromClientInterval);
		
		channel.disconnect();
	}
}