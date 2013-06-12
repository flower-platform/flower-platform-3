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
