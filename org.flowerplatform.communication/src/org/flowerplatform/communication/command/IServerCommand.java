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

import org.flowerplatform.communication.channel.CommunicationChannel;

/**
 * Interface implemented by all the objects that come from the 
 * <code>CommunicationChannel</code>. 
 * 
 * @author Cristi
 * 
 */
public interface IServerCommand {

	/**
	 * Every command is aware of the <code>CommunicationChannel</code>
	 * that it came from. 
	 */
	void setCommunicationChannel(CommunicationChannel communicationChannel);
	
	/**
	 * We don't call it "execute" to avoid a naming conflict
	 * with EMF's Command.execute().
	 */
	void executeCommand();
	
}