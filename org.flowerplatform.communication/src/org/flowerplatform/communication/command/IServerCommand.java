package org.flowerplatform.communication.command;

import org.flowerplatform.communication.channel.CommunicationChannel;

/**
 * Interface implemented by all the objects that come from the 
 * <code>CommunicationChannel</code>. 
 * 
 * @author Cristi
 * @flowerModelElementId _njS9JsGjEd6IZ7pEPOW5cA
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
