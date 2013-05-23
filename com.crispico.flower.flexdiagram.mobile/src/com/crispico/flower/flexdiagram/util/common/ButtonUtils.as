package com.crispico.flower.flexdiagram.util.common {
	import flash.display.DisplayObject;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Button;
	import mx.core.IFlexDisplayObject;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * This utility class provides new functionality for the Button class and its subclasses.	
	 * @author Cristina
	 */ 
	public class ButtonUtils {
		
		/**
		 * Stores the name of the icons URL property.
		 */ 
		public static var iconURLStyleName:String = "iconURL";
				
		/**
		 * The method is created so that a button can work with embedded icons and also with icons URLs.
		 */ 
		public static function viewIconForPhase(button:Button, tempIconName:String):IFlexDisplayObject {
			// get button's icon URL
			var iconURLStyle:Object = button.getStyle(iconURLStyleName);
			
			// verify if it's already a child
			var newIcon:IFlexDisplayObject = IFlexDisplayObject(button.getChildByName(iconURLStyleName));
			// if it isn't, create it
			if (newIcon == null) {
				newIcon = IFlexDisplayObject(new BitmapContainer(iconURLStyle));				
				button.addChild(DisplayObject(newIcon));
				newIcon.name = iconURLStyleName;
			} else {
				BitmapContainer(newIcon).retrieveImage(iconURLStyle);
			}							
			// hide the old icon.
		    if (button.currentIcon != null)
		    	button.currentIcon.visible = false;
			// keep track of which icon is current.
		    button.currentIcon = newIcon;
		      
		    // show the new icon.
		    if (button.currentIcon != null)
		    	button.currentIcon.visible = true;
		           
		    return newIcon;
		}	
	}
	
}