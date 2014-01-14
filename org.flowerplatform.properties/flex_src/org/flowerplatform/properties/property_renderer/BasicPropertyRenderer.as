package org.flowerplatform.properties.property_renderer {
	import flash.events.Event;
	import flash.events.FocusEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.controls.List;
	import mx.events.FlexEvent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.properties.PropertiesPlugin;
	import org.flowerplatform.properties.PropertiesView;
	import org.flowerplatform.properties.PropertyItemRenderer;
	import org.flowerplatform.properties.remote.Property;
	
	import spark.components.DataRenderer;
	import spark.components.HGroup;
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
				
 		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);	
		}
		
		protected function sendChangedValuesToServer(event:Event):void {
			var selectionOfItems:Object = PropertiesPlugin.getInstance().propertiesView.selectionForServer;
			if (!data.readOnly) {
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand(
						"propertiesService",
						"setProperties",
						[selectionOfItems, data.name, getValue()]
					)
				);
			}	
		}

		/**
		 * @author Cristina Constantinescu
		 */ 
		protected function getValue():Object {
			return Property(data).value;	
		}
		
		/**
		 * Registers the objectToListen to the Event event, and removes the listner when the parentRenderer is removed
		 * 
		 */
		protected function handleListeningOnEvent(event:String, parentRenderer:BasicPropertyRenderer, objectToListen:Object):void {
			objectToListen.addEventListener(event, sendChangedValuesToServer);		
			parentRenderer.addEventListener(FlexEvent.REMOVE, function(flexEvent:FlexEvent):void {
				objectToListen.removeEventListener(event, sendChangedValuesToServer);
				trace("Listener removed");
			});
		}
		
		override protected function createChildren():void {
			super.createChildren();
		}
	}
}