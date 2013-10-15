package org.flowerplatform.flexutil.action {
	import flash.events.Event;
	
	/**
	 * Dispatched by application when the menu is closed.
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class MenuClosedEvent extends Event {
		
		public static const MENU_CLOSED:String = "MENU_CLOSED";
				
		public function MenuClosedEvent() {
			super(MENU_CLOSED);
		}
	}
}