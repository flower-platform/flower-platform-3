package org.flowerplatform.properties.property_renderer {
	import flash.events.FocusEvent;
	
	import mx.binding.utils.BindingUtils;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.properties.PropertyItemRenderer;
	
	import spark.components.DataRenderer;
	import spark.components.HGroup;
	import spark.components.TextInput;

	/**
	 * @author Razvan Tache
	 */
	public class StringPropertyRenderer extends BasicPropertyRenderer {
		
		[Bindable]
		public var propertyValue:TextInput;
		
		public function StringPropertyRenderer() {
			super();
		}
		
		override protected function createChildren():void {
			super.data = PropertyItemRenderer(HGroup(parent).owner).data;
			super.createChildren();
			
			propertyValue = new TextInput();
			
			propertyValue.percentWidth = 100;
			propertyValue.percentHeight = 100;		
			propertyValue.text = data.value;
			propertyValue.editable = !data.readOnly;
			//propertyValue.setStyle("borderVisible", false);
			if (!data.readOnly) {
				propertyValue.addEventListener(FocusEvent.FOCUS_OUT, sendChangedValuesToServer);		
				BindingUtils.bindProperty( data, "value", propertyValue, "text" );
			}
			
			addElement(propertyValue);
		}
	}
}