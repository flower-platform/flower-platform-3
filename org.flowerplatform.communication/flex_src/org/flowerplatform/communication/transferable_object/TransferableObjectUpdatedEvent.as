package org.flowerplatform.communication.transferable_object {
	import flash.events.Event;

	/**
	 * Dispatched when a <code>TransferableObject</code> has been
	 * updated (from Java). The difference between this event and PropertyChangeEvent 
	 * is that this event is dispatched only once per object, regardless of
	 * the fact that the copied values are new or not. PropertyChangeEvent
	 * is dispatched once per property (and not per object) only if the new
	 * value is different from the old one.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _62iPAEznEeGsUPSh9UfXpw
	 */
	public class TransferableObjectUpdatedEvent extends Event {
		
		public static const OBJECT_UPDATED:String = "TransferableObjectUpdatedEvent";
		
		public var object:Object;
		
		public function TransferableObjectUpdatedEvent(object:Object) {
			super(OBJECT_UPDATED, false, false);
			this.object = object;
		}
		
	}
}