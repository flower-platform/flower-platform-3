package org.flowerplatform.properties.property_renderer {
	import flash.events.Event;
	
	import mx.binding.utils.BindingUtils;
	
	import org.flowerplatform.properties.PropertiesItemRenderer;
	
	import spark.components.CheckBox;
	import spark.components.HGroup;

	/**
	 * @author Razvan Tache
	 */
	public class BooleanPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var checkBox:CheckBox;
		
		public var checkBoxContainer:HGroup;
		
		public function BooleanPropertyRenderer() {
			super();	
		}
		
		override protected function createChildren():void {
			super.data = PropertiesItemRenderer(parent).data;
			super.createChildren();

			checkBox = new CheckBox();
			checkBoxContainer =  new HGroup();
			
			checkBoxContainer.percentHeight = 100;
			checkBoxContainer.percentWidth = 100;
			checkBoxContainer.horizontalAlign = "center";
			
			checkBox.selected = data.value;
			checkBox.enabled = !data.readOnly;
			
			if (!data.readOnly) {
				checkBox.addEventListener(Event.CHANGE, sendChangedValuesToServer);
				BindingUtils.bindProperty(data, "value", checkBox, "selected");
			}
			
			checkBoxContainer.addElement(checkBox);
			addElement(checkBoxContainer);
		}
	}
}