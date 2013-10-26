package org.flowerplatform.properties.property_renderer {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import mx.controls.List;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.properties.PropertiesItemRenderer;
	import org.flowerplatform.properties.PropertiesView;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.layouts.HorizontalLayout;

	/**
	 * @author Razvan Tache
	 */
	public class BasicPropertyRenderer extends DataRenderer {
						
		public function BasicPropertyRenderer() {
			super();
			layout = new HorizontalLayout;
			percentWidth = 50;
			percentHeight = 100;
		}
		
		/**
		 *	@return the PropertiesView of the item renderer
		 */
		public function get propertiesView():PropertiesView {
			return PropertiesView(PropertiesItemRenderer(parent).owner.parent);
		}
		
 		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);	
		}
		
		protected function sendChangedValuesToServer(event:Event):void {
			var selectionOfItems:Object = propertiesView.getSelectionForServer();
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
		}
		
		
	}
}