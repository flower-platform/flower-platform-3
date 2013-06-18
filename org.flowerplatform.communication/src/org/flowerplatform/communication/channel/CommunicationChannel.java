package org.flowerplatform.communication.channel;

import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.communication.command.CompoundClientCommand;

public abstract class CommunicationChannel {

	/**
	 * If this command is not <code>null</code>, it means that we are on the thread
	 * that is processing the HTTP request.
	 */
	protected CompoundClientCommand queueForCurrentResponse;
	
	protected long cachedUserId;
	
	/**
	 * Should be called when we are in the "normal" case. I.e. on the thread that 
	 * is processing the HTTP request (executing a "main" server command). The commands are queued 
	 * and when the "main" server command finishes, the queue is sent to the client, in
	 * the HTTP response.  
	 * 
	 * <p>
	 * This method is synchronized because other threads may call this (indirectly, through
	 * {@link #appendOrSendCommand()}), exactly at the instance when {@link #handleReceivedObjectEnded()}.
	 * 
	 * @see #handleReceivedObjectEnded()
	 */
	public synchronized void appendCommandToCurrentHttpResponse(AbstractClientCommand command) {
		if (queueForCurrentResponse == null) {
			throw new IllegalStateException("Trying to append command to current response, but no client request processing in progress!");
		} else {
			queueForCurrentResponse.appendCommand(command);
		}
	}
	
	/**
	 * Here, the <code>synchronized</code> shouldn't be necessary, as in theory there cannot
	 * be 2 threads invoking this method at the same time.
	 */
	public synchronized void handleReceivedObjectWillStart() {
		queueForCurrentResponse = new CompoundClientCommand();
	}
	
	/**
	 * The "main" command (in the thread handling the HTTP request) has finished its job, so we want
	 * to empty the queue and send it to the client, as part of the current HTTP response.
	 * 
	 * @see #appendCommandToCurrentHttpResponse()
	 */
	public synchronized AbstractClientCommand handleReceivedObjectEnded() {
		AbstractClientCommand result;
		if (queueForCurrentResponse.getCommandsList().isEmpty()) {
			result = null;
		} else if (queueForCurrentResponse.getCommandsList().size() == 1) {
			result = queueForCurrentResponse.getCommandsList().get(0);
		} else {
			result = queueForCurrentResponse;
		}
		queueForCurrentResponse = null;
		return result;
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
	
	public abstract boolean isDisposed();
	
	public abstract void dispose();
}
