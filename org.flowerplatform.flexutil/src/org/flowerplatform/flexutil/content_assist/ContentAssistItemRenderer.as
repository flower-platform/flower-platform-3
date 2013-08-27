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
package org.flowerplatform.flexutil.content_assist {
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	import spark.components.IconItemRenderer;
	import spark.components.Label;
	
	/**
	 * @author Mariana Gheorghe
	 */
	public class ContentAssistItemRenderer extends IconItemRenderer {
		
		protected var extraLabel:Label;
		
		public function ContentAssistItemRenderer() {
			super();
			
			labelField = "mainString";
			iconField = "iconUrl";
			
			extraLabel = new Label();
			extraLabel.setStyle("color", "#808080");
			addChild(extraLabel);
		
			if (!FlexUtilGlobals.getInstance().isMobile) {
				minHeight = 22;
			}
		}
		
		override protected function layoutContents(unscaledWidth:Number, unscaledHeight:Number):void {
			var offset:int = measureAndPositionElement(labelDisplay, 0);
			offset = measureAndPositionElement(extraLabel, offset);
		}
		
		private function measureAndPositionElement(element:Object, offset:int):int {
			if (element == null) {
				return offset;
			}
			var elementWidth:int = getElementPreferredWidth(element);
			var elementHeight:int = getElementPreferredHeight(element);
			setElementSize(element, elementWidth, elementHeight);
			setElementPosition(element, offset, height / 2 - elementHeight / 2);
			return elementWidth;
		}
		
		override public function set data(value:Object):void {
			super.data = value;
			
			if (value && value.extraString) {
				extraLabel.text = " - " + value.extraString;
			} else {
				extraLabel.text = null;
			}
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {			
		}
	}
}