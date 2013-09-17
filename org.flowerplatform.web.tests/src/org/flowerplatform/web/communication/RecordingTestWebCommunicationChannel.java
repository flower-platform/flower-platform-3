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
		} else {
			recordedCommands.add(command);
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