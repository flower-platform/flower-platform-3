package org.flowerplatform.properties.property_renderer {
	import flash.events.FocusEvent;
	import flash.events.MouseEvent;
	import flash.sampler.NewObjectSample;
	
	import mx.binding.utils.BindingUtils;
	
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.properties.PropertyItemRenderer;
	
	import spark.components.Button;
	import spark.components.HGroup;
	import spark.components.TextInput;

	public class StringWithButtonPropertyRenderer extends BasicPropertyRenderer implements IDialogResultHandler {
		
		[Bindable]
		public var propertyValue:TextInput;
		
		public var button:Button;
		
		public var clickHandler:Function;
		
		/**
		 * Signature: function getNewPropertyValueHandler(dialogResult:Object):String
		 * @author Cristina Constantinescu
		 */ 
		public var getNewPropertyValueHandler:Function;
		
		public function StringWithButtonPropertyRenderer() {
			super();
		}
		
		override protected function createChildren():void {
			super.data = PropertyItemRenderer(HGroup(parent).owner).data;
			super.createChildren();
			
			propertyValue = new TextInput();
			button = new Button();
			
			propertyValue.percentWidth = 90;
			propertyValue.percentHeight = 100;		
			propertyValue.text = data.value;
			propertyValue.editable = !data.readOnly;
			button.percentHeight = 100;
			button.percentWidth = 10;
			button.label = "...";
			button.addEventListener(MouseEvent.CLICK, clickHandlerInternal);
			
			if (!data.readOnly) {
				propertyValue.addEventListener(FocusEvent.FOCUS_OUT, sendChangedValuesToServer);		
				BindingUtils.bindProperty( data, "value", propertyValue, "text" );
			}
			
			addElement(propertyValue);
			addElement(button);
		}
		
		private function clickHandlerInternal(event:MouseEvent):void {
			clickHandler(this, data.name, data.value);
		}
		
		public function handleDialogResult(result:Object):void {
			// set new value after closing dialog
			propertyValue.text = getNewPropertyValueHandler(result);
		}
		
		
	}
}