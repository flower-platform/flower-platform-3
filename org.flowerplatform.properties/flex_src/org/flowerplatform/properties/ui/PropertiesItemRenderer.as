package org.flowerplatform.properties.ui {
	import flash.events.FocusEvent;
	import flash.utils.getQualifiedClassName;
	
	import mx.controls.TextInput;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.ClassFactory;
	import mx.core.IDataRenderer;
	import mx.states.AddChild;
	
	import org.flowerplatform.properties.PropertiesPlugin;
	import org.flowerplatform.properties.ui.property_renderer.BasicPropertyRenderer;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.components.List;
	import spark.components.RadioButton;
	import spark.components.supportClasses.ItemRenderer;
	import spark.layouts.HorizontalLayout;
	
	public class PropertiesItemRenderer extends DataRenderer {
		public var nameOfProperty:Label;
				
		public var itemRenderer:Class;
		
		public var itemRenderedInstance:BasicPropertyRenderer;
		
		public function PropertiesItemRenderer() {
			super();					
		}	
		
		override public function set data(value:Object):void {
			if (super.data == value) {
				return;
			}
			super.data = value;
			nameOfProperty.text = data.name;
			
			if (itemRenderedInstance != null) {
				removeElement(itemRenderedInstance);
			}
			
			// TODO create data.type
			itemRenderer = PropertiesPlugin.getInstance().propertyRendererClasses[getQualifiedClassName(data.value)];
			if (itemRenderer == null) {
				// TODO create default item renderer
				itemRenderer = PropertiesPlugin.getInstance().propertyRendererClasses["String"];
			}
			if (itemRenderer != null) {
				itemRenderedInstance = new itemRenderer(value);
			}
			addElement(itemRenderedInstance);
			
		}
		
		override protected function createChildren():void {
			super.createChildren();		
			
			layout = new HorizontalLayout;		
			nameOfProperty = new Label;

			nameOfProperty.percentWidth = 50;
			nameOfProperty.percentHeight = 100;
			
			addElement(nameOfProperty);
		}
		
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);
		}
		
	}
}