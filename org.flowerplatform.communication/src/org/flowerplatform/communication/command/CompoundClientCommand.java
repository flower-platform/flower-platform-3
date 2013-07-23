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

import java.util.ArrayList;
import java.util.List;

/**
 * This is a command containing a list of commands sent from Java and on Flex side it iterates
 * through every command and executes them. 
 * @author Sorin
 * @flowerModelElementId _n8t-QPuBEd6xsfFLsx1UvQ
 */
public class CompoundClientCommand extends AbstractClientCommand {

	private List<AbstractClientCommand> commandsList = new ArrayList<AbstractClientCommand>();

	public List<AbstractClientCommand> getCommandsList() {
		return commandsList;
	}

	public void setCommandsList(List<AbstractClientCommand> commandsList) {
		this.commandsList = commandsList;
	}
	
	public CompoundClientCommand appendCommand(AbstractClientCommand command) {
		commandsList.add(command);
		return this;
	}
	
	public CompoundClientCommand appendCommand(int index, AbstractClientCommand command) {
		commandsList.add(index, command);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuffer result = new StringBuffer(super.toString());
		result.append("[");
		for (int i = 0; i < commandsList.size(); i++) {
			AbstractClientCommand command = commandsList.get(i);
			// TODO temp cristi:
			// adaugat cand au venit francezii si lucram pe web; dupa cateva minute de utilizare, cu diagrame use case / activity incepeam sa am mereu o eroare venind de aici
			if (command != null)
				result.append(command.toString());
			if (i < commandsList.size() - 1)
				result.append(", ");
		}
		result.append("]");
		return result.toString();
	}
}