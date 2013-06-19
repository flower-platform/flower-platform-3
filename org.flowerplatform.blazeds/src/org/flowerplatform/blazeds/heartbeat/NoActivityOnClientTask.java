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
