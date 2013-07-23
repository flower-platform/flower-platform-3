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
package org.flowerplatform.communication.command;

import java.util.List;

/**
 * Command received from client.
 * Executes each {@link IServerCommand command} found in {@link #commandsList}.
 * 
 * @author Cristi
 * @author Cristina
 */
public class CompoundServerCommand extends AbstractServerCommand {

	private List<IServerCommand> commandsList;
	
	public List<IServerCommand> getCommandsList() {
		return commandsList;
	}

	public void setCommandsList(List<IServerCommand> commandsList) {
		this.commandsList = commandsList;
	}
	
	public void executeCommand() {
		for (IServerCommand command : commandsList) {
			command.setCommunicationChannel(getCommunicationChannel());
			command.executeCommand();
		}	
	}

}