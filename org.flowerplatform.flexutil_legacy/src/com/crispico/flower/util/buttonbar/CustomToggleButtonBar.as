package com.crispico.flower.util.buttonbar {
	
	import com.crispico.flower.flexdiagram.util.common.FlowerButton;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Button;
	import mx.controls.ToggleButtonBar;
	import mx.core.ClassFactory;
	import mx.core.IFlexDisplayObject;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	import mx.skins.halo.ButtonBarButtonSkin;

	use namespace mx_internal;
	
	/**
	 * ToggleButtonBar that uses FlowerButton. The items in the data provider must 
	 * have the <code>label</code> and <code>iconURL</code> fields set.
	 * 
	 * @author Mariana
	 */ 
	public class CustomToggleButtonBar extends ToggleButtonBar {
	
		/**
		 * Whether to display the button labels. Note: labels must still be set, even
		 * if they won't be displayed, to find the correct iconURL from the data provider.
		 */
		public var showLabel:Boolean = true;
		
		public function CustomToggleButtonBar() {
			super();
			this.mx_internal::navItemFactory = new ClassFactory(FlowerButton);
		}
		
		override protected function createNavItem(label:String, icon:Class = null):IFlexDisplayObject {
			var b:Button = Button(super.createNavItem(showLabel ? label : null, icon));
			var dp:ArrayCollection = dataProvider as ArrayCollection;
			for (var i:int = 0; i < dp.length; i++) {
				var obj:Object = dp.getItemAt(i);
				if (obj["label"] == label) {
					b.setStyle("iconURL", obj["iconURL"]);
					b.setStyle("skin", mx.skins.halo.ButtonBarButtonSkin); 
				}
			}
			return b;
		}
	}
}