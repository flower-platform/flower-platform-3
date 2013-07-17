package org.flowerplatform.flexdiagram.ui {
	import flash.display.GradientType;
	import flash.events.Event;
	import flash.geom.Matrix;
	
	import mx.containers.VBox;
	import mx.controls.Label;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MoveResizePlaceHolder extends UIComponent {
		
		public var colors:Array = new Array();
		public var alphas:Array = new Array();
		
		public var ratios:Array = new Array();
		public var gradientBoxRotation:Number = 0;
		
		public function MoveResizePlaceHolder() {
			super();
			
			// default
			colors.push(0xCCCCCC);
			alphas.push(0.4);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();	
			
			if (colors.length == 1) {
				graphics.lineStyle(1);
				graphics.beginFill(colors.pop(), alphas.pop());
			} else {
				var matrix:Matrix = new Matrix();
				matrix.createGradientBox(unscaledWidth, unscaledHeight, gradientBoxRotation);
				graphics.beginGradientFill(GradientType.LINEAR, colors, alphas, ratios, matrix);
			}		
			drawPlaceHolder(unscaledWidth, unscaledHeight);
		}
		
		protected function drawPlaceHolder(width:Number, height:Number):void {
			graphics.drawRect(0, 0, width, height);
		}
	}
}