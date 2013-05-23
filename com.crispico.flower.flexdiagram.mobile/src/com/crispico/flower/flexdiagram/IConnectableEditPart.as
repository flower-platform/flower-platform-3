package com.crispico.flower.flexdiagram {
	
	import mx.collections.ArrayCollection;
		
	/**
	 * Defines an EditPart that accepts connections.
	 * 
	 * @author Luiza
	 * @flowerModelElementId _-samID3BEeCdYZQGeBVpLw
	 */ 
	public interface IConnectableEditPart {
		
		/**
		 * Returns a collection of EditPart classes representing the accepted outgoing relations
		 * for this EditPart. 
		 */ 
		function getAcceptedOutgoingConnectionEditParts():ArrayCollection;
		
		/**
		 * Returns a collection of EditPart classes representing the accepted incomming relations
		 * for this EditPart.
		 */
		function getAcceptedIncommingConnectionEditParts():ArrayCollection;
		
	}
}