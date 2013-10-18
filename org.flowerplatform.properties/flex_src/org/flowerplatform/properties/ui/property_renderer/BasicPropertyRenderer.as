package org.flowerplatform.properties.ui.property_renderer {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.layouts.HorizontalLayout;

	public class BasicPropertyRenderer extends DataRenderer {
		
		public function BasicPropertyRenderer() {
			percentWidth = 50;
			percentHeight = 100;
		}
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);	
		}
		
		protected function sendChangedValuesToServer(event:Event):void {
			trace("Sending :" + data.name + " -> " + data.value);
		}
	}
}