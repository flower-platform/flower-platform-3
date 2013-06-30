package org.flowerplatform.communication.transferable_object {
	import flash.events.Event;

	/**
	 * Dispatched when <code>TransferableObject</code>s are being disposed.
	 *  
	 * @author Cristi
	 */
	public class TransferableObjectDisposedEvent extends Event {
		
		public static const OBJECT_DISPOSED:String = "TransferableObjectDisposedEvent";
		
		public var object:Object;
		
		public function TransferableObjectDisposedEvent(object:Object) {
			super(OBJECT_DISPOSED, false, false);
			this.object = object;
		}
		
	}
}