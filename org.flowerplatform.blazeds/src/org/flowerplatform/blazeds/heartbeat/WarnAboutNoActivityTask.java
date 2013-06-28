package org.flowerplatform.blazeds.heartbeat;

import static org.flowerplatform.blazeds.heartbeat.HeartbeatStatefulService.logger;

import org.flowerplatform.blazeds.channel.BlazedsCommunicationChannel;
import org.flowerplatform.communication.CommunicationPlugin;

/**
 * Channel Observer Task scheduled to warn the client that it will be disconnected.
 * @author Sorin
 * @flowerModelElementId _aC95kAlYEeK1a-Ic5xjg1Q
 */
public class WarnAboutNoActivityTask extends HeartbeatTask {
	
	public WarnAboutNoActivityTask(BlazedsCommunicationChannel channel) {
		super("Warn because No Activity happened on the Client", channel, noActivityOnClientInterval - warnAboutNoActivityInterval);
	}

	/**
	 * @flowerModelElementId _a2XscAlYEeK1a-Ic5xjg1Q
	 */
	@Override
	public void runWithChannelLocked() {
		if (logger.isDebugEnabled())
			logger.debug("Client : {} did not have any activity for at least {} ms. Warning client about disconnection", channel, noActivityOnClientInterval - warnAboutNoActivityInterval);
		
		HeartbeatStatefulService heartbeatStatefulService = (HeartbeatStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(HeartbeatStatefulService.SERVICE_ID);
		heartbeatStatefulService.warnAboutNoActivity(channel, warnAboutNoActivityInterval / 1000); // number of seconds until disconnect
	}
}