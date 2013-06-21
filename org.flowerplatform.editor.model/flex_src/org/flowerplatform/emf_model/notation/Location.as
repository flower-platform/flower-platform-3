package org.flowerplatform.emf_model.notation {
	import org.flowerplatform.communication.transferable_object.TransferableObject;
	
	/**
	 * @author Cristi
	 */
	[Bindable]
	[RemoteClass]
	public class Location extends TransferableObject implements LayoutConstraint {
		
		public var x:int;
		
		public var y:int;
		
	}
}