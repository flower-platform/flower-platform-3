package com.crispico.flower.flexdiagram.util.common {

	import mx.collections.ArrayCollection;
	import mx.controls.LinkButton;
	import mx.core.IFlexDisplayObject;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	[Style(name="iconURL", type="Object", inherit="no", states="up, over, down, disabled, selectedUp, selectedOver, selectedDown, selectedDisabled")]
	/**
	 * Using this extended class, a new property "iconURL" can be added to a link button.
	 * It is neccesary when we want to work with URL for icons instead of embedded icons.
	 * 
	 * @author Cristina
	 * @see FlowerButton
	 */	
	public class FlowerLinkButton extends LinkButton {
		
		override mx_internal function viewIconForPhase(tempIconName:String):IFlexDisplayObject {
			// get button's icon URL
			var iconURLStyle:Object = getStyle("iconURL");
			
			// if the style exists
			if (iconURLStyle != null) {
				return ButtonUtils.viewIconForPhase(this, tempIconName);	
			} else {
				// if the iconURL style doesn't exist, try to get its embedded icon
				return super.viewIconForPhase(tempIconName);
			}
		}
	}
	
}