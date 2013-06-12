package org.flowerplatform.communication.stateful_service;

import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;

public class StatefulServiceInvocationContext extends ServiceInvocationContext {

	private String statefulClientId;

	public StatefulServiceInvocationContext(CommunicationChannel communicationChannel) {
		this(communicationChannel, null);
	}

	public StatefulServiceInvocationContext(CommunicationChannel communicationChannel, InvokeServiceMethodServerCommand command, String statefulClientId) {
		super(communicationChannel, command);
		this.statefulClientId = statefulClientId;
	}
	
	public StatefulServiceInvocationContext(CommunicationChannel communicationChannel, String statefulClientId) {
		this(communicationChannel, null, statefulClientId);
	}

	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}
}
