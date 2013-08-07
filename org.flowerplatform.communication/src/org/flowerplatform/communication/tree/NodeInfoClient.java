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
package org.flowerplatform.communication.tree;


import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;

/**
 * @see NodeInfo
 * 
 * @author Cristi
 * @author Cristina
 * 
 */
public class NodeInfoClient {
	
	/**
	 * 
	 */
	private CommunicationChannel communicationChannel;
	
	/**
	 * 
	 */
	private int treeNumber = -1;
	
	/**
	 * 
	 */
	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}

	/**
	 * 
	 */
	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;		
	}
	
	/**
	 * 
	 */
	public int getTreeNumber() {
		return treeNumber;
	}

	/**
	 * 
	 */
	public void setTreeNumber(int treeNumber) {
		this.treeNumber = treeNumber;
	}

	public String getStatefulClientId(GenericTreeStatefulService service) {
		return service.getStatefulClientPrefixId() + " " + treeNumber;		
	}
	
	/**
	 * TODO test - delete
	 * @param service
	 * @return
	 */
	public String getStatefulClientId(org.flowerplatform.communication.temp.tree.remote.GenericTreeStatefulService service) {
		return service.getStatefulClientPrefixId() + " " + treeNumber;	
	}
	
	/**
	 * 
	 */
	public NodeInfoClient(CommunicationChannel communicationChannel, String statefulClientId, GenericTreeStatefulService service) {		
		this.communicationChannel = communicationChannel;
		try {
			this.treeNumber = Integer.parseInt(statefulClientId.substring(statefulClientId.length() - 1));		
		} catch (NumberFormatException e) {
			//
		}
	}
		
	@Override
	public String toString() {
		return String.format("NodeInfoCl[%s,treNb=%s]", getCommunicationChannel(), getTreeNumber());
	}
	
}