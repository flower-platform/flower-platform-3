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