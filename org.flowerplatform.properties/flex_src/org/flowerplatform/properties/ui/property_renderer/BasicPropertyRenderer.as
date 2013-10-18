package org.flowerplatform.properties.ui.property_renderer {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.properties.ui.PropertiesList;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.layouts.HorizontalLayout;

	public class BasicPropertyRenderer extends DataRenderer {
				
		protected var _parent:Object;
		
		public function BasicPropertyRenderer() {
			percentWidth = 50;
			percentHeight = 100;
		}
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);	
		}
		
		public function setParent(value:Object):void {
			_parent = value;
		}
		
		protected function sendChangedValuesToServer(event:Event):void {
			var selectionOfItems:Object = PropertiesList(_parent.owner).getSelectedItemsForProperties();
			if(!data.readOnly) {
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand(
						"propertiesProviderService",
						"setProperties",
						[data, selectionOfItems]
					)
				);
			}	
		}
	}
}