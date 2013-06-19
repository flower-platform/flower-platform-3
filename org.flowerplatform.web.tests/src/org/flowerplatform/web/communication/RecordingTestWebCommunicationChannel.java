package org.flowerplatform.web.communication;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.blazeds.channel.BlazedsCommunicationChannel;
import org.flowerplatform.blazeds.channel.ServerSnapshotClientCommand;
import org.flowerplatform.communication.command.AbstractClientCommand;
import org.flowerplatform.web.security.sandbox.FlowerWebPrincipal;

/**
 * A {@link WebCommunicationChannel} used to intercept sent commands. 
 * 
 * @author Mariana
 * @author Cristi
 */
public class RecordingTestWebCommunicationChannel extends BlazedsCommunicationChannel {

	public RecordingTestWebCommunicationChannel() {
		super(null, null);
		principal = new FlowerWebPrincipal(6);
	}

	private List<Object> recordedCommands = new ArrayList<Object>();
	
	public List<Object> getRecordedCommands() {
		return recordedCommands;
	}
	
	/**
	 * Records the command.
	 */
	@Override
	public void sendCommandWithPush(AbstractClientCommand command) {
		if (command instanceof ServerSnapshotClientCommand) {
			ServerSnapshotClientCommand cmd = (ServerSnapshotClientCommand) command;
			recordedCommands.add(cmd.getWrappedCommand());
		}
	}

	@Override
	public boolean isDisposed() {
		return false;
	}
	
	private FlowerWebPrincipal principal;
	
	@Override
	public FlowerWebPrincipal getPrincipal() {
		return principal;
	}
	
	public void setPrincipal(FlowerWebPrincipal principal) {
		this.principal = principal;
	}
}
