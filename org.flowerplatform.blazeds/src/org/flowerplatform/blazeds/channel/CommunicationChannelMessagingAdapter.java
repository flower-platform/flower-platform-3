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
package org.flowerplatform.blazeds.channel;


import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.channel.CommunicationChannelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import flex.messaging.FlexContext;
import flex.messaging.MessageClient;
import flex.messaging.MessageClientListener;
import flex.messaging.messages.Message;
import flex.messaging.services.messaging.adapters.MessagingAdapter;

/**
 * Singleton that manages the incoming and outgoing data, from server to client.
 * 
 * <p>
 * Starts to exist as soon as the first client connects, long after the server was started 
 * and initialized and after the {@link CommunicationChannelManager} was initialized.
 * 
 * <p>
 * This should not keep any logic for interpreting the data, just logic for sending and receiving it
 * and maybe other logic related to BlazeDS way of keeping it's clients. 
 * 
 * <p>
 * Note: {@link MessageClient} is the only blazeDS object allowed to be used outside of this adapter
 * and {@link CommunicationChannelManager}.
 * 
 * @see CommunicationChannelManager
 * @author Sorin
 */
public class CommunicationChannelMessagingAdapter extends MessagingAdapter implements MessageClientListener {
	
	private static Logger logger = LoggerFactory.getLogger(CommunicationChannelMessagingAdapter.class);
	
	@Override
	public void start() {
		MessageClient.addMessageClientCreatedListener(this);
		super.start();
	}
	
	@Override
	public void stop() {
		MessageClient.removeMessageClientCreatedListener(this);
		super.stop();
	}

	@Override
	public void messageClientCreated(MessageClient messageClient) {
		messageClient.addMessageClientDestroyedListener(this);
		CommunicationPlugin.getInstance().getCommunicationChannelManager().messageClientCreated(messageClient.getFlexClient().getId(), new BlazedsCommunicationChannel(this, messageClient));
	}

	@Override
	public void messageClientDestroyed(MessageClient messageClient) {
		messageClient.removeMessageClientDestroyedListener(this);
		CommunicationPlugin.getInstance().getCommunicationChannelManager().messageClientDestroyed(messageClient.getFlexClient().getId());
	}

	@Override
	public Object invoke(Message message) {
		return CommunicationPlugin.getInstance().getCommunicationChannelManager().handleReceivedObject(message.getHeader(Message.FLEX_CLIENT_ID_HEADER), (IPrincipal) FlexContext.getUserPrincipal(), message.getBody());
	}
	
}