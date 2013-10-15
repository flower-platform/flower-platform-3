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
package org.flowerplatform.editor.model.renderer {
	
	import flash.display.GradientType;
	import flash.display.Graphics;
	import flash.display.InterpolationMethod;
	import flash.display.SpreadMethod;
	import flash.filters.BitmapFilter;
	import flash.filters.DropShadowFilter;
	import flash.geom.Matrix;
	
	import mx.core.UIComponent;
	
	public class ShapeDrawer {
		
		// constants that define the shape to be rendered
		public static const RECTANGLE:int = 1;
		public static const ROUND_RECTANGLE:int = 2;
		public static const ELLIPSE:int = 3;
		public static const ACTOR:int = 4;
		public static const CIRCLE:int = 5;
		public static const ENSCRIBED_CIRCLES:int = 6;
		public static const RHOMBUS:int = 7;
		public static const NOTE:int = 8;
		
		// gradient parameters used when isGradientEnabled = true
		public var type:String = GradientType.LINEAR;
		public var gradientColors:Array = [0x99BAF3, 0xE0FFFF];
		public var tx:Number = 0;
		public var ty:Number = 0;
		public var alphas:Array = [0.9, 0.5];
		public var ratios:Array = [0, 255];
		public var spreadMethod:String = SpreadMethod.PAD;
		public var interp:String = InterpolationMethod.LINEAR_RGB;
		public var focalPtRatio:Number = 0;
		
		public var filters:Array;
		
		private var figure:UIComponent;
		
		public var color:uint = 0xFFFFFF;
		
		private var option:int;
		
		private var isGradientEnabled:Boolean;	
		
		private var shadowEnabled:Boolean;
		
		private var gradientMatrix:Matrix;
		
		public function ShapeDrawer(option: int, figure:UIComponent = null, enableGradientColor:Boolean = true, shadowEnabled:Boolean = false) {
			if (enableGradientColor) {
				gradientMatrix = new Matrix();
				isGradientEnabled = true;
			}
			this.figure = figure;
			this.shadowEnabled = shadowEnabled;
			
			this.option = option;
		}
		
		public function createGradientBox(width:Number, height:Number, rotation:Number):void {
			if (isGradientEnabled) {
				gradientMatrix.createGradientBox(width, height, rotation, tx, ty);
			}
		}
		
		private function drawActor(graphics: Graphics, width:Number, height:Number):void {
			var actorWidth:Number = width;
			var actorHeight:Number = height;
			
			// when height is too small, width must be half height
			if (width > height/2) {
				actorWidth = height/2;
				// when width is too small, height must be double width 	
			} else if (width < height / 2) {
				actorHeight = width * 2;
			}
			graphics.drawCircle(width/2, actorHeight/6, actorHeight/6);
			
			graphics.lineStyle(1);
			
			graphics.moveTo(width/2, actorHeight/3); // neck start
			graphics.lineTo(width/2, actorHeight*2/3); // body
			
			graphics.moveTo(width/2 - actorWidth/2, actorHeight* 4/9); // postion the hands
			graphics.lineTo(width/2 + actorWidth/2, actorHeight* 4/9); // hands
			
			graphics.moveTo(width/2, actorHeight* 2/3); // legs start
			graphics.lineTo(width/2 - actorWidth/2, actorHeight);
			
			// draw legs
			graphics.moveTo(width/2, actorHeight* 2/3);
			graphics.lineTo(width/2 + actorWidth/2, actorHeight);
		}
		
		private function drawCircle(graphics: Graphics, width:Number, height:Number):void {
			// choose the radius the smallest dimension to make sure it fits in the figure
			var radius: Number = width <= height ? width : height;
			graphics.drawCircle(width/2, height/2, radius/2); 
		}
		
		private function drawEnscribedCircles(graphics: Graphics, width:Number, height:Number):void {
			// first draw the filled smaller circle in the inside	
			var radius: Number = width <= height ? width : height;
			graphics.drawCircle(width/2, height/2, radius * 3/8);
			graphics.endFill();
			// then draw the circle on the outside
			graphics.drawCircle(width/2, height/2, radius/2);
		}
		
		private function drawRhombus(graphics: Graphics, width:Number, height:Number):void {
			var xCenter:Number = width/2, yCenter:Number = height/2;
			
			if (width > 2 * height) {
				width = 2 * height;
			} else if (height > 2 * width) {
				height = 2 * width;
			}
			
			var halfWidth:Number = width/2, halfHeight:Number = height/2;
			
			graphics.moveTo(xCenter, yCenter - halfHeight);
			graphics.lineTo(xCenter - halfWidth, yCenter);
			graphics.lineTo(xCenter, yCenter + halfHeight);
			graphics.lineTo(xCenter + halfWidth, yCenter);
			graphics.lineTo(xCenter, yCenter - halfHeight);
		}
		
		private function drawNote(graphics: Graphics, width:Number, height:Number):void {
			var midX:int = width-15; //15 pixels 
			var midY:int = 15;
			
			graphics.moveTo(0,0);
			graphics.lineTo(midX,0);
			graphics.lineTo(width,midY);
			graphics.lineTo(width,height);
			graphics.lineTo(0,height);
			graphics.lineTo(0,0);
			
			graphics.beginFill(0xffffff,1);
			graphics.moveTo(midX,0);
			graphics.lineTo(midX,midY);
			graphics.lineTo(width,midY);
		}
		
		protected function drawShape(graphics: Graphics, width:Number, height:Number):void {
			switch(option) {
				case RECTANGLE:
					graphics.drawRect(0, 0, width, height);
					break;
				case ROUND_RECTANGLE:
					graphics.drawRoundRectComplex(0, 0, width, height, 15, 15, 15, 15);
					break;
				case ELLIPSE:
					graphics.drawEllipse(0, 0, width, height);
					break
				case CIRCLE:
					drawCircle(graphics, width, height);
					break;
				case ENSCRIBED_CIRCLES:
					drawEnscribedCircles(graphics, width, height);
					break;
				case RHOMBUS:
					drawRhombus(graphics, width, height);
					break;
				case ACTOR:
					drawActor(graphics, width, height);
					break;
				case NOTE:
					drawNote(graphics, width, height);
					break;	
			}		
		}
		
		private function resetFilters(figure: UIComponent):void {
			for each (var filter:BitmapFilter in figure.filters) {
				if (filter is DropShadowFilter) {
					var shadow:DropShadowFilter = DropShadowFilter(filter);
					// default parameters
					shadow.alpha = 0.55;
					shadow.angle = 35;
					shadow.blurX = 5;
					shadow.blurY = 5;
					shadow.color = 0x000000;  
					shadow.distance = 6;
					
				}
			}
			//reset filters 
			var newFilters:Array = new Array();
			for each (var f:BitmapFilter in figure.filters) {
				newFilters.push(f);
			}
			figure.filters = newFilters;
		}
		
		public function drawFigure():void {
			drawFigure2(figure.width, figure.height);
		}
		
		public function drawFigure2(width:Number, height:Number):void {	
			if (shadowEnabled) {
				resetFilters(figure); 
			}
			var graphics:Graphics =  figure.graphics;
			graphics.clear();
			graphics.beginFill(0, 0);
			graphics.drawRect(0, 0,  width, height);
			graphics.endFill();
			graphics.beginFill(0xFFFFFF, 1);			
			drawShape(graphics,  width, height);
			graphics.endFill();
			if (isGradientEnabled) { 
				gradientMatrix.createGradientBox(width, height, figure.rotation, tx, ty);
				graphics.beginGradientFill(type, gradientColors,
					alphas, ratios, gradientMatrix, 
					spreadMethod, interp, focalPtRatio);
				
			} else {
				graphics.beginFill(color);
			}
			
			graphics.lineStyle(1);
			
			drawShape(graphics, width, height);		
		}
		
		public function draw(graphics: Graphics, width:Number, height:Number):void {
			graphics.clear();
			graphics.beginFill(0, 0);
			graphics.drawRect(0, 0, width, height);
			graphics.endFill();
			graphics.beginFill(0xFFFFFF, 1);			
			drawShape(graphics, width, height);
			graphics.endFill();
			if (isGradientEnabled) {
				graphics.beginGradientFill(type, gradientColors, alphas, ratios, gradientMatrix, spreadMethod, interp, focalPtRatio);
			} else {
				graphics.beginFill(color);
			}
			graphics.lineStyle(1);
			drawShape(graphics, width, height);
		} 
		
	}
}