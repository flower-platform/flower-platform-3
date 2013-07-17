package org.flowerplatform.flexutil.layout.event {
	
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;

	/**
	 * Dispatched when a view is removed from workbench.
	 * 
	 * @author Cristina
	 */ 
	public class ViewRemovedEvent extends Event {
		
		public static const VIEW_REMOVED:String = "view_removed";
		
		public var dontRemoveView:Boolean;
				
		public function ViewRemovedEvent() {
			super(VIEW_REMOVED);	
			
			dontRemoveView = false;
		}		
	}
	
}