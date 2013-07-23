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
package org.flowerplatform.communication.sequential_execution;

import org.flowerplatform.communication.callback.InvokeCallbackClientCommand;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.command.IServerCommand;

/**
 * After executing the wrapped command, sends a signal to the client side.
 * When the client side receives the signal, it should have already processed the commands
 * that were sent by the wrapped command.
 * 
 * @see SequentialExecutionQueue.as
 * 
 * @author Sorin
 */
public class SequentialExecutionServerCommand extends AbstractServerCommand {
	
	public long callbackId;

	public IServerCommand command;
	
	public void executeCommand() { 
		try {
			command.setCommunicationChannel(getCommunicationChannel());
			command.executeCommand();
		} finally {
			// At this moment all results from processing the wrapped command should be deposited into the communication channel.
			communicationChannel.appendCommandToCurrentHttpResponse(new InvokeCallbackClientCommand(callbackId, null));
		}
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + " with command = " + command;
	}
}