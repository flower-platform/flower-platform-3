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
package org.flowerplatform.flexdiagram.renderer {
	import flash.display.Graphics;
	import flash.events.Event;
	import flash.text.TextFieldAutoSize;
	
	import org.flowerplatform.flexdiagram.tool.event.ZoomPerformedEvent;
	import org.flowerplatform.flexdiagram.util.MultilineLabelItemRenderer;
	
	import spark.components.DataRenderer;
	import spark.components.IconItemRenderer;
	import spark.components.LabelItemRenderer;
	import spark.components.supportClasses.StyleableTextField;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class NoteRenderer extends MultilineLabelItemRenderer {
				
		private static const MIN_WIDTH_DEFAULT:Number = 100;
		private static const MIN_HEIGHT_DEFAULT:Number = 15;
		
		private static const BACKGROUND_COLOR_DEFAULT:uint = 0xFFFFFF;
		
		public var color:uint = BACKGROUND_COLOR_DEFAULT;
		
		public function NoteRenderer() {
			super();
			
			setStyle("verticalAlign", "top");	
			setStyle("horizontalAlign", "left");
			setStyle("paddingTop", "20");
			setStyle("paddingBottom", "5");
			setStyle("paddingLeft", "5");
			setStyle("paddingRight", "5");
			
			minWidth = MIN_WIDTH_DEFAULT;
			minHeight = MIN_HEIGHT_DEFAULT;
			
			addEventListener(ZoomPerformedEvent.ZOOM_PERFORMED, zoomPerformedHandler);
		}
						
		protected function zoomPerformedHandler(event:ZoomPerformedEvent):void {
			invalidateSize();
			// use calllater because the updateList must be done after recalculating size
			callLater(invalidateDisplayList);
		}
				
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			
			drawNoteBorder(graphics, unscaledWidth, unscaledHeight);
		}
		
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {				
		}
		
		override protected function drawBackground(unscaledWidth:Number, unscaledHeight:Number):void {			
		}	
		
		private function drawNoteBorder(graphics: Graphics, width:Number, height:Number):void {
			var midX:int = width - 15; //15 pixels 
			var midY:int = 15;
			
			graphics.clear();
			graphics.beginFill(color, 1);
			graphics.lineStyle(1);
			graphics.moveTo(0, 0);
			graphics.lineTo(midX, 0);
			graphics.lineTo(width, midY);
			graphics.lineTo(width, height);
			graphics.lineTo(0, height);
			graphics.lineTo(0, 0);
			graphics.endFill();
			
			graphics.beginFill(color, 1);
			graphics.moveTo(midX, 0);
			graphics.lineTo(midX, midY);
			graphics.lineTo(width, midY);
			graphics.endFill();
		}
				
	}
}