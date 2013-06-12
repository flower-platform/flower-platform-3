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
