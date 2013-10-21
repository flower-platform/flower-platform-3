package org.flowerplatform.properties.ui.property_renderer {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.properties.ui.PropertiesItemRenderer;
	import org.flowerplatform.properties.ui.PropertiesList;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.layouts.HorizontalLayout;

	public class BasicPropertyRenderer extends DataRenderer {
						
		public function BasicPropertyRenderer() {
			super();
		}
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);	
		}
		
		protected function sendChangedValuesToServer(event:Event):void {
			var selectionOfItems:Object = PropertiesList(PropertiesItemRenderer(parent).owner).getSelectedItemsForProperties();
			if (!data.readOnly) {
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand(
						"propertiesProviderService",
						"setProperties",
						[data, selectionOfItems]
					)
				);
			}	
		}
		
		override protected function createChildren():void {
			super.createChildren();
			
			percentWidth = 50;
			percentHeight = 100;
		}
		
		
	}
}