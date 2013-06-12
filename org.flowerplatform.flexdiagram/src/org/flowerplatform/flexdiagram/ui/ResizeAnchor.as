package org.flowerplatform.flexdiagram.ui {
	
	import flash.events.MouseEvent;
	
	import mx.controls.Alert;
	import mx.core.UIComponent;
	
	import org.flowerplatform.flexdiagram.renderer.selection.AnchorsSelectionRenderer;
	
	/**	
	 * @author Cristina Constantinescu
	 */
	public class ResizeAnchor extends UIComponent {
		
		public static const LEFT_UP:String = "left_up";		
		public static const MIDDLE_UP:String = "middle_up";		
		public static const RIGHT_UP:String = "right_up";		
		public static const RIGHT_MIDDLE:String = "right_middle";		
		public static const RIGHT_DOWN:String = "right_down";		
		public static const MIDDLE_DOWN:String = "middle_down";		
		public static const LEFT_DOWN:String = "left_down";		
		public static const LEFT_MIDDLE:String = "left_middle";
			
		[Inspectable(category="General", enumeration="left_up,middle_up,right_up,right_middle,right_down,middle_down,left_down,left_middle")]
		public var type:String;
		
		/**
		 * Wide of the anchor.		
		 */
		public var wide:int = 5;
		
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