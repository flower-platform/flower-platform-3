package org.flowerplatform.properties.ui.property_renderer {
	import flash.events.Event;
	
	import mx.binding.utils.BindingUtils;
	
	import spark.components.CheckBox;
	
	public class BooleanPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var checkBox:CheckBox = new CheckBox();
		
		public function BooleanPropertyRenderer(data) {
			super();	
			super.data = data;
			
			checkBox.selected = data.value;
			checkBox.enabled = !data.readOnly;
			
			if(!data.readOnly) {
				checkBox.addEventListener(Event.CHANGE, sendChangedValuesToServer);
				BindingUtils.bindProperty( data, "value", checkBox, "selected" );
			}
			
			addElement(checkBox);
		}
		
	}
}