package com.crispico.flower.util {
	import mx.controls.Button;
	import mx.core.IFlexDisplayObject;
	import mx.core.UIComponent;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Button that hosts any type of UIComponent as icon.
	 * 
	 * @author Cristi
	 */
	public class ButtonWithIconComponent extends Button	{
		
		public var iconComponent:UIComponent;
		
		override mx_internal function viewIconForPhase(tempIconName:String):IFlexDisplayObject {
			if (currentIcon == null && iconComponent != null) {
				currentIcon = iconComponent;
				addChild(iconComponent);
			}
			return currentIcon;
		}
	}
}