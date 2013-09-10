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
package org.flowerplatform.flexutil.text {
	import flash.events.Event;
	
	import spark.components.TextInput;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AutoGrowTextInput extends TextInput {
		
		private var newSegmentWidth:Number = 5;
		
		public var maxGrowWidth:Number;
			
		public function AutoGrowTextInput()	{
			super();
			addEventListener(Event.CHANGE, changedHandler);
		}
		
		protected function changedHandler(event:Event):void {			
			var textWidth:Number = measureText(this.text).width + 10; // 10 represents borders
			var newWidth:Number = textWidth + newSegmentWidth;
			if (newWidth >= width && newWidth < maxWidth) {
				setLayoutBoundsSize(newWidth, NaN);								
			}
		}
	}
}