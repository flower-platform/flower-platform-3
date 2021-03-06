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
 * 
 */
public class ServiceInvocationContext {

	/**
	 * @see Getter doc.
	 * 
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
	 * 
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
	 * 
	 */
	public ServiceInvocationContext(CommunicationChannel communicationChannel, InvokeServiceMethodServerCommand command) {	
		this.communicationChannel = communicationChannel;
		this.command = command;
	}

}