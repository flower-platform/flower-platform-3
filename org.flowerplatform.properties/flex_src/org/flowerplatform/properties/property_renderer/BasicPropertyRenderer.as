package org.flowerplatform.properties.property_renderer {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.properties.PropertiesItemRenderer;
	import org.flowerplatform.properties.PropertiesList;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.layouts.HorizontalLayout;
	/**
	 * @author Razvan Tache
	 */
	public class BasicPropertyRenderer extends DataRenderer {
						
		public function BasicPropertyRenderer() {
			super();
		}
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);	
		}
		
		protected function sendChangedValuesToServer(event:Event):void {
			var selectionOfItems:Object = PropertiesList(PropertiesItemRenderer(parent).owner).getSelectionForServer();
			if (!data.readOnly) {
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand(
						"propertiesService",
						"setProperties",
						[selectionOfItems, data.name, data.value]
					)
				);
			}	
		}
		
		override protected function createChildren():void {
			super.createChildren();
			layout = new HorizontalLayout;
			percentWidth = 50;
			percentHeight = 100;
		}
		
		
	}
}