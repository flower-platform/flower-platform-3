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