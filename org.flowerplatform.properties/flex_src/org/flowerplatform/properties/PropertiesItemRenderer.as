package org.flowerplatform.properties {
	import flash.events.FocusEvent;
	import flash.utils.getQualifiedClassName;
	
	import mx.controls.TextInput;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.ClassFactory;
	import mx.core.IDataRenderer;
	import mx.states.AddChild;
	
	import org.flowerplatform.flexutil.FactoryWithInitialization;
	import org.flowerplatform.properties.PropertiesPlugin;
	import org.flowerplatform.properties.property_renderer.BasicPropertyRenderer;
	
	import spark.components.DataRenderer;
	import spark.components.Label;
	import spark.components.List;
	import spark.components.RadioButton;
	import spark.components.supportClasses.ItemRenderer;
	import spark.layouts.HorizontalLayout;

	/**
	 * @author Razvan Tache
	 */
	public class PropertiesItemRenderer extends DataRenderer {
		public var nameOfProperty:Label;
				
		public var itemRenderer:Class;
		
		public var itemRenderedInstance:BasicPropertyRenderer;
		
		public function PropertiesItemRenderer() {
			super();					
		}	
		
		override public function set data(value:Object):void {
			// data being bindable, whenenver it changes it enters here and loses all past informations
			// by checkig this, we make sure that the past event doesn't happen
			if (super.data == value) {
				return;
			}
			
			super.data = value;
			nameOfProperty.text = data.name;
			
			if (itemRenderedInstance != null) {
				removeElement(itemRenderedInstance);
			}
			var factory:FactoryWithInitialization = FactoryWithInitialization(PropertiesPlugin.getInstance().propertyRendererClasses[data.type]);
			if (factory == null) {
				factory = PropertiesPlugin.getInstance().propertyRendererClasses[null];
			}
			if (factory != null) {
				itemRenderedInstance = factory.newInstance(false);
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