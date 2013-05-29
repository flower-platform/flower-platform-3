package com.crispico.flower.flexdiagram.samples.basic.figure {
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.text.TextFieldType;
	import flash.ui.Keyboard;
	
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	
	import spark.components.IconItemRenderer;
	
	public class GeneralItemRenderer extends IconItemRenderer {
		
		public function GeneralItemRenderer() {
			super();
				
			// used only for web app
			minHeight = 0;			
		}
	
		override protected function drawBorder(unscaledWidth:Number, unscaledHeight:Number):void {	
//			Example setting the bottom border 
//			var bottomSeparatorColor:uint;
//			var bottomSeparatorAlpha:Number;
//			
//			bottomSeparatorColor = 0x000000;
//			bottomSeparatorAlpha = .3;
//						
//			graphics.beginFill(bottomSeparatorColor, bottomSeparatorAlpha);
//			graphics.drawRect(0, unscaledHeight - (mx_internal::isLastItem ? 0 : 1), unscaledWidth, 1);
//			graphics.endFill();
		}
		
		override protected function drawBackground(unscaledWidth:Number, unscaledHeight:Number):void	{
//			drawBorder(unscaledWidth, unscaledHeight);
		}
		
	}
}