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
import org.flowerplatform.communication.CommunicationPlugin;

/**
 * Channel Observer Task scheduled to warn the client that it will be disconnected.
 * @author Sorin
 * 
 */
public class WarnAboutNoActivityTask extends HeartbeatTask {
	
	public WarnAboutNoActivityTask(BlazedsCommunicationChannel channel) {
		super("Warn because No Activity happened on the Client", channel, noActivityOnClientInterval - warnAboutNoActivityInterval);
	}

	/**
	 * 
	 */
	@Override
	public void runWithChannelLocked() {
		if (logger.isDebugEnabled())
			logger.debug("Client : {} did not have any activity for at least {} ms. Warning client about disconnection", channel, noActivityOnClientInterval - warnAboutNoActivityInterval);
		
		HeartbeatStatefulService heartbeatStatefulService = (HeartbeatStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(HeartbeatStatefulService.SERVICE_ID);
		heartbeatStatefulService.warnAboutNoActivity(channel, warnAboutNoActivityInterval / 1000); // number of seconds until disconnect
	}
}