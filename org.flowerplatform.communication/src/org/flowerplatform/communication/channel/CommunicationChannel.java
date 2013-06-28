package org.flowerplatform.communication.channel;

import org.flowerplatform.communication.IPrincipal;
import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.communication.command.CompoundClientCommand;

public abstract class CommunicationChannel {

	/**
	 * If this command is not <code>null</code>, it means that we are on the thread
	 * that is processing the HTTP request.
	 */
	protected ThreadLocal<CompoundClientCommand> queueForCurrentResponse = new ThreadLocal<CompoundClientCommand>();
	
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
	public void appendCommandToCurrentHttpResponse(AbstractClientCommand command) {
		if (queueForCurrentResponse.get() == null) {
			throw new IllegalStateException("Trying to append command to current response, but no client request processing in progress!");
		} else {
			queueForCurrentResponse.get().appendCommand(command);
		}
	}
	
	/**
	 * Here, the <code>synchronized</code> shouldn't be necessary, as in theory there cannot
	 * be 2 threads invoking this method at the same time.
	 */
	public void handleReceivedObjectWillStart(Object object) {
		queueForCurrentResponse.set(new CompoundClientCommand());
	}
	
	/**
	 * The "main" command (in the thread handling the HTTP request) has finished its job, so we want
	 * to empty the queue and send it to the client, as part of the current HTTP response.
	 * 
	 * @see #appendCommandToCurrentHttpResponse()
	 */
	public AbstractClientCommand handleReceivedObjectEnded(Object object) {
		AbstractClientCommand result;
		if (queueForCurrentResponse.get().getCommandsList().isEmpty()) {
			result = null;
		} else if (queueForCurrentResponse.get().getCommandsList().size() == 1) {
			result = queueForCurrentResponse.get().getCommandsList().get(0);
		} else {
			result = queueForCurrentResponse.get();
		}
		queueForCurrentResponse.set(null);
		return result;
	}
	
	public long getCachedUserId() {
		return cachedUserId;
	}
	
	public void appendOrSendCommand(AbstractClientCommand command) {
		if (queueForCurrentResponse.get() != null) {
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
