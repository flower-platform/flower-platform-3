package org.flowerplatform.properties.ui.property_renderer {
	import flash.events.FocusEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.core.IVisualElement;
	
	import spark.components.DataRenderer;
	import spark.components.TextInput;
	
	public class StringPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var propertyValue:TextInput = new TextInput();
		
		public function StringPropertyRenderer(data) {
			super();
			super.data = data;
			
			propertyValue.percentWidth = 100;
			propertyValue.percentHeight = 100;
			
			propertyValue.text = data.value;
			propertyValue.editable = !data.readOnly;
			
			if(!data.readOnly) {
				propertyValue.addEventListener(FocusEvent.FOCUS_OUT, sendChangedValuesToServer);		
				BindingUtils.bindProperty( data, "value", propertyValue, "text" );
			}
			
			addElement(propertyValue);
		}
			
	}
}