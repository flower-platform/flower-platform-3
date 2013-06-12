package org.flowerplatform.flexdiagram.util.infinitegroup {
	
	import flash.events.MouseEvent;
	import flash.geom.Rectangle;
	
	import spark.components.Button;
	import spark.core.IViewport;
	
	/**
	 * The "Go Back" button displayed in case of a "forced" scrolling.
	 * 
	 * @see InfiniteScrollerSkin
	 * 
	 * @author Cristina Constantinescu
	 */ 
	public class GoBackButton extends Button {
		
		// available positions
		public static const MIDDLE:int = 0;
		public static const TOP_LEFT:int = 1;
		public static const TOP_RIGHT:int = 2;
		public static const BOTTOM_LEFT:int = 3;
		public static const BOTTOM_RIGHT:int = 4;
		public static const TOP:int = 5;
		public static const BOTTOM:int = 6;
		public static const LEFT:int = 7;
		public static const RIGHT:int = 8;
	
		public var position:int;
		
		public var viewport:InfiniteDataRenderer;
		
		public function GoBackButton() {
			addEventListener(MouseEvent.CLICK, mouseClickHandler);	
		}
		
		private function mouseClickHandler(event:MouseEvent):void {
			// reset scroll positions
			viewport.horizontalScrollPosition = viewport.contentRect.x;
			viewport.verticalScrollPosition = viewport.contentRect.y;
		}
		
		/**
		 * @see InfiniteScroller.viewport_propertyChangeHandler()
		 */ 
		public function calculatePosition():void {
			if (viewport == null || viewport.contentRect == null) {
				return;
			}
			var hpos:Number = viewport.horizontalScrollPosition;
			var vpos:Number = viewport.verticalScrollPosition;
			var rect:Rectangle = viewport.contentRect;
			
			if (hpos < rect.x) {
				if (vpos < rect.y) {
					position = BOTTOM_RIGHT;
				} else if (vpos > rect.y + rect.height) {
					position = TOP_RIGHT;
				} else {
					position = RIGHT;
				}				
			} else if (hpos > rect.x + rect.width) {
				if (vpos < rect.y) {
					position = BOTTOM_LEFT;
				} else if (vpos > rect.y + rect.height) {
					position = TOP_LEFT;
				} else {
					position = LEFT;
				}
			} else {
				if (vpos < rect.y) {
					position = BOTTOM;
				} else if (vpos > rect.y + rect.height) {
					position = TOP;
				} else {
					position = MIDDLE;
				}
			}
		}
				
	}
}