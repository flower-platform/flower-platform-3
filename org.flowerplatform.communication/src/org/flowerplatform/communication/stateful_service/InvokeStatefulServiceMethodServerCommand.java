package org.flowerplatform.communication.stateful_service;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;

public class InvokeStatefulServiceMethodServerCommand extends
		InvokeServiceMethodServerCommand {

	private String statefulClientId;
	
	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}

	@Override
	protected ServiceInvocationContext createServiceInvocationContext(
			CommunicationChannel communicationChannel,
			InvokeServiceMethodServerCommand command) {
		return new StatefulServiceInvocationContext(communicationChannel, command, getStatefulClientId());
	}

}
