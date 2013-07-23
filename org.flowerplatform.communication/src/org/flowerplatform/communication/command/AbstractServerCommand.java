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
 * A command that doesn't affect the EMF command stack.
 * 
 * @author Cristi
 * @flowerModelElementId _dk8xIMbnEd6X47mKLkTdUQ
 */
public abstract class AbstractServerCommand implements IServerCommand {

	/**
	 * @flowerModelElementId _0Xk7Y8kLEd6ahcn2Khjy2A
	 */
	protected CommunicationChannel communicationChannel;
	
	/**
	 * @flowerModelElementId _QKOBMPYuEd-tOavYQFNY0g
	 */
	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}
	
	/**
	 * @flowerModelElementId _0Xk7ZskLEd6ahcn2Khjy2A
	 */
	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
	}

}