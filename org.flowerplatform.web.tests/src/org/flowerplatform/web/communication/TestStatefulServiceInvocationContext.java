package org.flowerplatform.web.communication;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.stateful_service.StatefulServiceInvocationContext;

/**
 * @author Cristi
 */
public class TestStatefulServiceInvocationContext extends StatefulServiceInvocationContext implements IRecordingTestWebCommunicationChannelProvider{

	public TestStatefulServiceInvocationContext(
			CommunicationChannel communicationChannel,
			InvokeServiceMethodServerCommand command, String statefulClientId) {
		super(communicationChannel, command, statefulClientId);
	}

	public TestStatefulServiceInvocationContext(
			CommunicationChannel communicationChannel) {
		super(communicationChannel);
	}

	/**
	 * @return A CC casted to {@link RecordingTestWebCommunicationChannel}, for convenience.
	 */
	public RecordingTestWebCommunicationChannel getRecordingTestWebCommunicationChannel() {
		return (RecordingTestWebCommunicationChannel) getCommunicationChannel();
	}

}
