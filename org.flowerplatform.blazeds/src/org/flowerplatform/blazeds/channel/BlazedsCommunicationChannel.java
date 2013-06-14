package org.flowerplatform.blazeds.channel;

import java.util.Collections;
import java.util.Set;

import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.command.AbstractClientCommand;

import flex.messaging.MessageClient;
import flex.messaging.messages.AsyncMessage;
import flex.messaging.services.MessageService;
import flex.messaging.util.UUIDUtils;

public class BlazedsCommunicationChannel extends CommunicationChannel {

	protected CommunicationChannelMessagingAdapter communicationChannelMessagingAdapter;
	
	protected MessageClient messageClient;
	
	public BlazedsCommunicationChannel(
			CommunicationChannelMessagingAdapter communicationChannelMessagingAdapter,
			MessageClient messageClient) {
		super();
		this.communicationChannelMessagingAdapter = communicationChannelMessagingAdapter;
		this.messageClient = messageClient;
//		FlexClient flexClient = communicationChannelMessagingAdapter.getDestination().getService().getMessageBroker().getFlexClientManager().getFlexClient((String) flexClientId);
//		messageClient = flexClient.getMessageClients().get(0);  // We have the convention that a Flex Client will only have just one subscriber (MessageClient).
	}

	@Override
	public void sendCommandWithPush(AbstractClientCommand command) {
		if (command == null)
			return;
		command = new ServerSnapshotClientCommand(command);
		
		AsyncMessage asyncMessage = new AsyncMessage();
		asyncMessage.setDestination(messageClient.getDestinationId());
        asyncMessage.setMessageId(UUIDUtils.createUUID());
        asyncMessage.setBody(command);

        MessageService messageService = (MessageService) messageClient.getDestination().getService();
        Set<Object> messageClientIds = Collections.singleton(messageClient.getClientId());
        messageService.pushMessageToClients(messageClientIds, asyncMessage, false);
	}

	@Override
	public void appendCommandToCurrentHttpResponse(AbstractClientCommand command) {
		super.appendCommandToCurrentHttpResponse(new ServerSnapshotClientCommand(command));
	}

	@Override
	public Object getId() {
		return messageClient == null ? null : messageClient.getFlexClient().getId();
	}

	@Override
	public IPrincipal getPrincipal() {
		return null;
	}

	@Override
	public boolean isDisposed() {
		return messageClient == null;
	}

	@Override
	public void dispose() {
		messageClient = null;
	}

}
