package org.flowerplatform.flexutil.resources {
	
	import flash.events.Event;
	
	/**
	 * @author Mariana
	 */
	public class ResourceUpdatedEvent extends Event {
		
		public static const RESOURCE_UPDATED:String = "resourceUpdated";
		
		public function ResourceUpdatedEvent() {
			super(RESOURCE_UPDATED);
		}
	}
}