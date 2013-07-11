package org.flowerplatform.emf_model.notation {
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.TransferableObject;

	[Bindable]
	[RemoteClass]
	public class View extends TransferableObject {
		public var viewType:String;
		public var persistentChildren_RH:IList;
		
		[Transient]
		public var viewDetails:Object;
	}
}