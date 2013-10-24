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
package org.flowerplatform.flexutil.iframe {
	import flash.events.Event;
	
	import mx.events.FlexEvent;
	
	/**
	 * <code>IFrame</code> code was copied from here:
	 * http://code.google.com/p/flex-iframe
	 * 
	 * <p>
	 * This class adds specific flower behavior and styles.
	 * @author Cristina Constantinescu
	 */
	public class FlowerIFrame extends IFrame {
		
		public function FlowerIFrame(id:String = null) {
			super(id);
			
			setStyle("paddingLeft", 0);
			setStyle("paddingRight", 0);
			setStyle("paddingBottom", 0);
			setStyle("paddingTop", 0);
			
			overlayDetection = true;			
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}
				
		protected function creationCompleteHandler(event:FlexEvent):void {
			addEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
		}
		
		protected function removedFromStageHandler(event:Event):void {
			removeIFrame();
		}		
	}
}