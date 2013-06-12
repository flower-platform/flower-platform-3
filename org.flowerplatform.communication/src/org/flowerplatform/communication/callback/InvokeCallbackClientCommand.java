package org.flowerplatform.communication.callback;

import org.flowerplatform.communication.command.AbstractClientCommand;

/**
 * Invokes a callback that has been registered with Flex.
 * 
 * @author Cristi
 * @flowerModelElementId _0X32bMkLEd6ahcn2Khjy2A
 */
public class InvokeCallbackClientCommand extends AbstractClientCommand {
	
	private long callbackId;
	
	private Object result;
	
	private boolean disposeCallbackWithoutInvocation = false;

	public long getCallbackId() {
		return callbackId;
	}

	public void setCallbackId(long callbackId) {
		this.callbackId = callbackId;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean isDisposeCallbackWithoutInvocation() {
		return disposeCallbackWithoutInvocation;
	}

	public void setDisposeCallbackWithoutInvocation(
			boolean disposeCallbackWithoutInvocation) {
		this.disposeCallbackWithoutInvocation = disposeCallbackWithoutInvocation;
	}

	public InvokeCallbackClientCommand(long callbackId, Object result) {
		super();
		this.callbackId = callbackId;
		this.result = result;
	}

	public InvokeCallbackClientCommand(long callbackId, Object result,
			boolean disposeCallbackWithoutInvocation) {
		super();
		this.callbackId = callbackId;
		this.result = result;
		this.disposeCallbackWithoutInvocation = disposeCallbackWithoutInvocation;
	}
	
}
