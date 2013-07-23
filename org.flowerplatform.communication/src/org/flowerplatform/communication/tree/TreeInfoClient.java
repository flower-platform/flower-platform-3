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

/**
 * Represents a generic tree on server side.
 * The <code>communicationChannel</code> and <code>statefulClientId</code> 
 * are sufficient to determine a specific tree.
 * 
 * <p>
 * Note: <br>
 * We don't use the tree number instead of <code>statefulClientId</code> 
 * because there can be trees (only server side trees for the moment) that aren't numbered.
 * 
 * @see GenericTreeStatefulService
 * 
 * @author Cristina
 */
public class TreeInfoClient {

	private CommunicationChannel communicationChannel;
	
	private String statefulClientId;

	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}

	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;
	}
	
	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}

	public TreeInfoClient(CommunicationChannel communicationChannel, String statefulClientId) {		
		this.communicationChannel = communicationChannel;
		this.statefulClientId = statefulClientId;	
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}
		TreeInfoClient other = (TreeInfoClient) obj;
		return this.getCommunicationChannel().equals(other.getCommunicationChannel()) && (this.getStatefulClientId() != null ? this.getStatefulClientId().equals(other.getStatefulClientId()) : true);
	}

	@Override
	public int hashCode() {
		return communicationChannel.hashCode() + (statefulClientId != null ? statefulClientId.hashCode() : 0);
	}

	@Override
	public String toString() {
		return String.format("TreeInfNd[%s,treNb=%s]", getCommunicationChannel(), getStatefulClientId());
	}
	
}