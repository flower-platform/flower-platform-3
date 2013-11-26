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
package org.flowerplatform.codesync.regex.ide.ui {
	
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.IconItemRenderer;
	import spark.components.LabelItemRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class RegexItemRenderer extends IconItemRenderer implements IFocusManagerComponent {
		
		public function RegexItemRenderer() {
			minHeight = 22;
			setStyle("verticalAlign", "middle");
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {			
		}
		
	}
}