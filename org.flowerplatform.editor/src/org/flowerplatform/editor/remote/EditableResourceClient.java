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
package org.flowerplatform.editor.remote;

import org.flowerplatform.communication.channel.CommunicationChannel;

/**
 * Represents a client that is connected to an {@link EditableResource}.
 * 
 * <p>
 * Besides {@link #communicationChannel}, all other fields exist in the
 * corresponding AS class.
 * 
 * @see EditableResource#getClients()
 * @author Cristi
 * 
 */
public class EditableResourceClient {
	
//	private String cachedClientId;
//	
//	private String cachedLogin;
//	
//	private String cachedName;
	
	/**
	 * 
	 */
	private CommunicationChannel communicationChannel;
	
	private String statefulClientId;
	
	public EditableResourceClient(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
		
//		cachedClientId = communicationChannel.getClientId();
//		
//		IPrincipal principal = communicationChannel.getPrincipal();
//		if (principal != null) {
//			IUser user = communicationChannel.getPrincipal().getUser();
//			cachedLogin = user.getLogin();
//			cachedName = user.getName();
//		}
	}
	
	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}

	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
	}
	
	/**
	 * 
	 */
	public String getCommunicationChannelId() {
		return (String) communicationChannel.getId();
	}
	
//	/**
//	 * 
//	 */
//	public String getLogin() {
//		return cachedLogin;
//	}
//	
//	/**
//	 * 
//	 */
//	public String getName() {
//		return cachedName;
//	}

	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}

	@Override
	public String toString() {
		return String.format("EdResCl[%s,stClId=%s]", getCommunicationChannel(), getStatefulClientId());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EditableResourceClient) {
			return communicationChannel.equals(((EditableResourceClient) obj).getCommunicationChannel());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return communicationChannel.hashCode();
	}
}