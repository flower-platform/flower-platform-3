/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.security.ui {
	
	import flash.display.DisplayObject;
	import flash.text.TextField;
	
	import mx.controls.CheckBox;
	
	/**
	 * Custom data grid item renderer used for boolean items
	 * to display a disabled, centered checkbox.
	 * 
	 * @author Cristina
	 */ 
	public class CenteredCheckBoxItemRenderer extends CheckBox {
		
		public var selectedState:String;
		
		override public function set data(value:Object):void {		
			this.selected = value[selectedState];
			this.enabled = false;
		}
				
		// center the checkbox icon
		override protected function updateDisplayList(w:Number, h:Number):void {
			super.updateDisplayList(w, h);
				
			var n:int = numChildren;
			for (var i:int = 0; i <n; i++) {
				var c:DisplayObject = getChildAt(i);
				// CheckBox component is made up of box skin and label TextField
				if (!(c is TextField)) {
					c.x = (w - c.width) / 2;
					c.y = (h - c.height) / 2;					
				}
			}
		}
	}
}