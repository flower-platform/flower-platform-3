package org.flowerplatform.communication.stateful_service;

import java.util.Arrays;

import org.flowerplatform.communication.command.AbstractClientCommand;

public class InvokeStatefulClientMethodClientCommand extends
		AbstractClientCommand {
	
	private String statefulClientId;
	
	private String methodName;
	
	private Object[] parameters;

	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String method) {
		this.methodName = method;
	}

	public Object[] getParameters() {
		return parameters;
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public InvokeStatefulClientMethodClientCommand(String statefulClientId,
			String method, Object[] parameters) {
		super();
		this.statefulClientId = statefulClientId;
		this.methodName = method;
		this.parameters = parameters;
	}

	@Override
	public String toString() {
		return super.toString() + String.format(" statefulClientId = %s methodName = %s parameters = %s", statefulClientId, methodName, Arrays.toString(parameters));
	}

}
