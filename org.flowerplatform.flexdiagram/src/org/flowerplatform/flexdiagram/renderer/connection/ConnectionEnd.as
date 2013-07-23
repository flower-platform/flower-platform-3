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
package org.flowerplatform.flexdiagram.renderer.connection {
	
	import mx.core.UIComponent;

	/**
	 *  
	 * UIComponent that represents the arrow/rhomb/triangle for a 
	 * connection figure. The class receives the coordinates for a point,
	 * an angle and the type of figure that needs to be drawn.
	 * @flowerModelElementId _b2GZML8REd6XgrpwHbbsYQ
	 */ 
	public class ConnectionEnd extends UIComponent {
	
		/**
		 * @flowerModelElementId _b2GZNb8REd6XgrpwHbbsYQ
		 */
		private const ARROW_WIDTH:int = 4;
		
		/**
		 * @flowerModelElementId _b2GZOb8REd6XgrpwHbbsYQ
		 */
		private const ARROW_HEIGHT:int = 8;
		
		/**
		 * @flowerModelElementId _b2GZPb8REd6XgrpwHbbsYQ
		 */
		public static const SIMPLE_ARROW:String = "arrow";
		
		/**
		 * @flowerModelElementId _b2GZQb8REd6XgrpwHbbsYQ
		 */
		public static const CLOSED_ARROW:String = "closed_arrow";
		
		/**
		 * @flowerModelElementId _b2GZRb8REd6XgrpwHbbsYQ
		 */
		public static const CLOSED_FILLED_IN_ARROW:String = "closed_filled_in_arrow";
		
		/**
		 * @flowerModelElementId _b2GZSb8REd6XgrpwHbbsYQ
		 */
		public static const RHOMB:String = "shared";
		
		/**
		 * @flowerModelElementId _b2GZTb8REd6XgrpwHbbsYQ
		 */
		public static const FILLED_IN_RHOMB:String = "composite";
		
		/**
		 * Represents the source/target point for a connection 
		 * (where the figure must be drawn).
		 */
		public var point:BindablePoint; 
		
		/**
		 * The required angle.
		 */
		public var angle:Number; 
		
		/**
		 * The type of figure. Can be one of the constants
		 * defined above.
		 */
		public var type:String;
		
		public function ConnectionEnd() {
			super();
		}
		
		private function getParent():ConnectionFigure {
			return ConnectionFigure(parent);
		}
		
		/**
		 * The simple arrow marks the navigability property of an 
		 * attribute by drawing an arrow on the association line 
		 * that connects two classes.
		 */
		private function drawSimpleArrow():void {
			graphics.moveTo(point.x, point.y);
			graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) - ARROW_WIDTH*Math.sin(angle),
  									point.y + ARROW_HEIGHT*Math.sin(angle) + ARROW_WIDTH*Math.cos(angle));
  			graphics.lineTo(point.x, point.y);
  	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) + ARROW_WIDTH*Math.sin(angle),
  	  								point.y + ARROW_HEIGHT*Math.sin(angle) - ARROW_WIDTH*Math.cos(angle));
		}
		
		/**
		 * Similar to simpleArrow function but it fills the
		 * interior of the arrow with a given color. If no color
		 * is sent, the used color is white.
		 * 
		 * used by generalizations, realizations and interface implementations.
		 */
		private function drawClosedArrow(fillColor:uint = 0xffffff):void {
			graphics.beginFill(fillColor);
  			graphics.moveTo(point.x, point.y);
	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) - ARROW_WIDTH*Math.sin(angle),
  									point.y + ARROW_HEIGHT*Math.sin(angle) + ARROW_WIDTH*Math.cos(angle));
  	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) + ARROW_WIDTH*Math.sin(angle),	
  									point.y + ARROW_HEIGHT*Math.sin(angle) - ARROW_WIDTH*Math.cos(angle));		
  			graphics.lineTo(point.x, point.y);					
			graphics.endFill();
		}
		
		/**
		 * Draws a rhomb - for associations (shared/composite)
		 * Used by associations.
		 */
		private function drawRhomb(fillColor:uint = 0xffffff):void {
			graphics.beginFill(fillColor);
			graphics.moveTo(point.x, point.y);
			graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) - ARROW_WIDTH*Math.sin(angle),
  									point.y + ARROW_HEIGHT*Math.sin(angle) + ARROW_WIDTH*Math.cos(angle));
  			graphics.lineTo(point.x, point.y);
  	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) + ARROW_WIDTH*Math.sin(angle),
  	  								point.y + ARROW_HEIGHT*Math.sin(angle) - ARROW_WIDTH*Math.cos(angle));
  	  		graphics.lineTo(point.x + 2*ARROW_HEIGHT*Math.cos(angle), point.y + 2*ARROW_HEIGHT*Math.sin(angle));
  	  		graphics.lineTo(point.x + ARROW_HEIGHT*Math.cos(angle) - ARROW_WIDTH*Math.sin(angle),
  	  								point.y + ARROW_HEIGHT*Math.sin(angle) + ARROW_WIDTH*Math.cos(angle));
  	  		graphics.endFill();
		}
		
		/**
		 * The method calls a method to draw an arrow/triangle/diamond.
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			graphics.lineStyle(getParent().thickness, getParent().color);
			switch (type) {
				case SIMPLE_ARROW:
					drawSimpleArrow();
					break;
				case CLOSED_ARROW:
					drawClosedArrow();
					break;
				case CLOSED_FILLED_IN_ARROW:
					drawClosedArrow(getParent().color);
					break;
				case RHOMB:
					drawRhomb();
					break;
				case FILLED_IN_RHOMB:
					drawRhomb(getParent().color);
					break;
				default:
					break;
			}
		}
	}
}