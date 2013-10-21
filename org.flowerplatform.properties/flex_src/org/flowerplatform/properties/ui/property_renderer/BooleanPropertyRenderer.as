package org.flowerplatform.properties.ui.property_renderer {
	import flash.events.Event;
	
	import mx.binding.utils.BindingUtils;
	
	import spark.components.CheckBox;
	/**
	 * @author Razvan Tache
	 */
	public class BooleanPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var checkBox:CheckBox;
		
		public function BooleanPropertyRenderer(data:Object) {
			super();	
			super.data = data;	
		}
		
		override protected function createChildren():void {
			super.createChildren();

			checkBox = new CheckBox();
			
			checkBox.percentHeight = 100;
			checkBox.percentWidth = 100;
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