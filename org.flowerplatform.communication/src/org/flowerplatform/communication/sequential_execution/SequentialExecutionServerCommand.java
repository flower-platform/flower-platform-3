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
