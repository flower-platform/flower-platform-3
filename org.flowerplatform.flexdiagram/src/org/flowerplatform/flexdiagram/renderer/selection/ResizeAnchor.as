package org.flowerplatform.flexdiagram.renderer.selection {
	
	import mx.controls.Alert;
	import mx.core.UIComponent;
	
	/**	
	 * @author Cristina Constantinescu
	 */
	public class ResizeAnchor extends UIComponent {
			
		/**
		 * Wide of the anchor.		
		 */
		private var wide:int = 2;
		
		/**
		 * Constant for 2 colors used when displaying anchors.	
		 */
		protected const BLACK:uint = 0x000000;
		
	
		protected const WHITE:uint = 0xffffff;
		
		/**
		 * Updates display for resize anchor.
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			graphics.clear();
			
			// inside anchor color
			var inside:uint = WHITE;
			// outside anchor color
			var margin:uint = BLACK;
			if (parent != null && AnchorsSelectionRenderer(parent).getMainSelection()) {
				inside = BLACK;
				margin = WHITE;
			}
			graphics.beginFill(inside);
			graphics.drawRect(-wide, -wide, +wide*2+1, +wide*2+1);
			graphics.endFill();
			graphics.lineStyle(1,margin);
			graphics.drawRect(-wide-1, -wide-1, +wide*2+2, +wide*2+2);			
		}
	}
}