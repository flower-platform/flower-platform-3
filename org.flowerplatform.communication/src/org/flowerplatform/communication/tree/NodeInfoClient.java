package org.flowerplatform.communication.tree;


import org.flowerplatform.communication.channel.CommunicationChannel;
import org.flowerplatform.communication.tree.remote.GenericTreeStatefulService;

/**
 * @see NodeInfo
 * 
 * @author Cristi
 * @author Cristina
 * @flowerModelElementId _NmOiIKTpEeGJQ4vD1xX4gA
 */
public class NodeInfoClient {
	
	/**
	 * @flowerModelElementId _NmOiIqTpEeGJQ4vD1xX4gA
	 */
	private CommunicationChannel communicationChannel;
	
	/**
	 * @flowerModelElementId _NmPJMKTpEeGJQ4vD1xX4gA
	 */
	private int treeNumber = -1;
	
	/**
	 * @flowerModelElementId _i1v6AsBrEeG5PP70DrXYIQ
	 */
	public CommunicationChannel getCommunicationChannel() {
		return communicationChannel;
	}

	/**
	 * @flowerModelElementId _i1v6BMBrEeG5PP70DrXYIQ
	 */
	public void setCommunicationChannel(CommunicationChannel communicationChannel) {
		this.communicationChannel = communicationChannel;		
	}
	
	/**
	 * @flowerModelElementId _yOo3UBFDEeKNlYFNXVVOOw
	 */
	public int getTreeNumber() {
		return treeNumber;
	}

	/**
	 * @flowerModelElementId _yOtIwBFDEeKNlYFNXVVOOw
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
	 * @flowerModelElementId _i1xIIcBrEeG5PP70DrXYIQ
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