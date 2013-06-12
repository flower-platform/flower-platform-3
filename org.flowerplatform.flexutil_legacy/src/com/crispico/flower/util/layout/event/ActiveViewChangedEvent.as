package com.crispico.flower.util.layout.event {
	import flash.display.DisplayObject;
	import flash.events.Event;
	
	import mx.core.UIComponent;
	
	/**
	 * Dispatched by <code>Workbench</code> each time the active view changes.
	 * 
	 * @see ActiveViewList
	 * 
	 * @author Cristina
	 */
	public class ActiveViewChangedEvent extends Event {
	
		public static const ACTIVE_VIEW_CHANGED:String = "ActiveViewChangedEvent";
		
		public var newView:UIComponent;
		
		public var oldView:UIComponent;
		
		public function ActiveViewChangedEvent(newView:UIComponent, oldView:UIComponent):void {
			super(ACTIVE_VIEW_CHANGED);
			this.oldView = oldView;
			this.newView = newView;
		}

	}
}