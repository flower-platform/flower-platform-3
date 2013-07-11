package org.flowerplatform.communication.service;


import java.util.Map;

import org.flowerplatform.communication.channel.CommunicationChannel;

/**
 * Passes as parameter to service methods if they want to, i.e.
 * their first argument has this class as first parameter.
 * 
 * <p>
 * This might be useful for services that want to control the 
 * communication channel: i.e. dispatch commands, etc.
 * 
 * @author Cristi
 * @flowerModelElementId _lVycQFZcEeGgtLw8YArqtQ
 */
public class ServiceInvocationContext {

	/**
	 * @see Getter doc.
	 * @flowerModelElementId _sgiywFZcEeGgtLw8YArqtQ
	 */
	private CommunicationChannel communicationChannel;

	/**
	 * @see Getter doc.
	 */
	private InvokeServiceMethodServerCommand command;
	
	private Map<String, Object> additionalData;
	
	/**
	 * The {@link CommunicationChannel} the
	 * service invocation arrived from.
	 * 
	 * @flowerModelElementId _z-S9wFZcEeGgtLw8YArqtQ
	 */
	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}
	
	/**
	 * The {@link InvokeServiceMethodServerCommand} 
	 * that created this context.
	 * 
	 * <p>
	 * There are cases when we want to run it again.
	 */
	public InvokeServiceMethodServerCommand getCommand() {
		return command;
	}

	public Map<String, Object> getAdditionalData() {
		return additionalData;
	}

	public void setAdditionalData(Map<String, Object> additionalData) {
		this.additionalData = additionalData;
	}

	public ServiceInvocationContext(CommunicationChannel communicationChannel) {
		super();
		if (communicationChannel == null) {
			throw new IllegalArgumentException("Trying to create an instance with a null CommunicationChannel");
		}
		this.communicationChannel = communicationChannel;		
	}
	
	/**
	 * @flowerModelElementId _ujG7cFbgEeGL3vi-zPhopA
	 */
	public ServiceInvocationContext(CommunicationChannel communicationChannel, InvokeServiceMethodServerCommand command) {	
		this.communicationChannel = communicationChannel;
		this.command = command;
	}

}