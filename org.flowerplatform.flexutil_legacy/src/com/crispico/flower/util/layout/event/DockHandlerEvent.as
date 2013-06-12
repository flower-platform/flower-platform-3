package com.crispico.flower.util.layout.event
{
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	import flash.events.Event;
	
	import mx.core.UIComponent;

	/**
	 * Dispatched by <code>Workbench</code> when the "Dock" button from
	 * <code>ViewPopupWindow</code> is pressed.
	 * 
	 * <p>
	 * Allows adding custom behavior for adding the view back to workbench. <br>
	 * It is cancelable, so other events can prevent default behavior to be executed.
	 * 
	 * @author Cristina
	 */ 
	public class DockHandlerEvent extends Event	{
		
		public static const CLICK:String = "DockClick";
				
		public var component:UIComponent;
		
		public var viewLayoutData:ViewLayoutData;
		
		public function DockHandlerEvent(type:String, viewLayoutData:ViewLayoutData, component:UIComponent) {
			super(type, false, true);
			this.viewLayoutData = viewLayoutData;
			this.component = component;
		}
	}
}