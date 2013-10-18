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
		
		public var nameOfProperty:Label = new Label();
				
		private var itemRenderer:Class;
		
		private var itemRenderedInstance:BasicPropertyRenderer;
		
		public function PropertiesItemRenderer() {
			super();		
			
			layout = new HorizontalLayout;

			nameOfProperty.percentWidth = 50;
			nameOfProperty.percentHeight = 100;
			
			addElement(nameOfProperty);
			
		}	
		
		override public function set data(value:Object):void {	
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
		
		override protected function focusOutHandler(event:FocusEvent):void {
			super.focusOutHandler(event);
		}
		
	}
}