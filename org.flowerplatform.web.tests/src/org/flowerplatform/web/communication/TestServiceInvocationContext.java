package org.flowerplatform.web.communication;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;

public class TestServiceInvocationContext extends ServiceInvocationContext implements IRecordingTestWebCommunicationChannelProvider {

	public TestServiceInvocationContext(CommunicationChannel communicationChannel) {
		super(communicationChannel);
	}
	
	public TestServiceInvocationContext(CommunicationChannel communicationChannel, InvokeServiceMethodServerCommand command) {
		super(communicationChannel, command);
	}
	
	/**
	 * @return A CC casted to {@link RecordingTestWebCommunicationChannel}, for convenience.
	 */
	public RecordingTestWebCommunicationChannel getRecordingTestWebCommunicationChannel() {
		return (RecordingTestWebCommunicationChannel) getCommunicationChannel();
	}	
}
