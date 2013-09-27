package org.flowerplatform.eclipse.communication;

import org.flowerplatform.communication.channel.CommunicationChannel;

public interface IEclipseChannelListener {

	void newClientInitializationsSent(CommunicationChannel channel);
}
