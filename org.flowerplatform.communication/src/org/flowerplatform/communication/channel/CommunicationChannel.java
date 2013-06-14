package org.flowerplatform.communication.channel;

import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.communication.command.CompoundClientCommand;

public abstract class CommunicationChannel {

	protected CompoundClientCommand queueForCurrentResponse;
	
	protected long cachedUserId;
	
	public void appendCommandToCurrentHttpResponse(AbstractClientCommand command) {
		if (queueForCurrentResponse == null) {
			throw new IllegalStateException("Trying to append command to current response, but no client request processing in progress!");
		} else {
			queueForCurrentResponse.appendCommand(command);
		}
	}
	
	public void handleReceivedObjectWillStart() {
		queueForCurrentResponse = new CompoundClientCommand();
	}
	
	public AbstractClientCommand handleReceivedObjectEnded() {
		if (queueForCurrentResponse.getCommandsList().isEmpty()) {
			return null;
		} else if (queueForCurrentResponse.getCommandsList().size() == 1) {
			return queueForCurrentResponse.getCommandsList().get(0);
		} else {
			return queueForCurrentResponse;
		}
	}
	
	public long getCachedUserId() {
		return cachedUserId;
	}
	
	public void appendOrSendCommand(AbstractClientCommand command) {
		if (queueForCurrentResponse != null) {
			appendCommandToCurrentHttpResponse(command);
		} else {
			sendCommandWithPush(command);
		}
	}
	
	public abstract void sendCommandWithPush(AbstractClientCommand command);

	public abstract Object getId();
	
	public abstract IPrincipal getPrincipal();

	/**
	 * @author Mariana
	 */
	public abstract void disconnect();
	
	/**
	 * @author Mariana
	 */
	public static IPrincipal getCurrentPrincipal() {
		throw new UnsupportedOperationException();
	}
	
	public abstract boolean isDisposed();
	
	public abstract void dispose();
}
