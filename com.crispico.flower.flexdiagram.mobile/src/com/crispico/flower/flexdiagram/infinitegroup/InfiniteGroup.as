package com.crispico.flower.flexdiagram.infinitegroup {
	import spark.components.Group;
	
	/**
	 * @author Cristina
	 */ 
	public class InfiniteGroup extends Group {
		
		/**
		 * The current number of steps made to scroll up/left
		 * after reaching the (0, 0) point (increments only for negative values).
		 */ 
		public var horizontalOffset:Number = 0;		
		public var verticalOffset:Number = 0;
		
		/**
		 * The maximum contentHeight/contentWidth for this group.
		 * When set, the group will take them in consideration and not
		 * the sizes given by its children.
		 * 
		 * @see setContentSize()
		 */ 
		public var maxContentWidth:Number = NaN;		
		public var maxContentHeight:Number = NaN;
		
		/**
		 * The minimum x/y coordonates for content group.
		 * Used to set a limit to horizontal/vertical scroll left/top limit. 
		 */ 
		public var minContentX:Number = 0;		
		public var minContentY:Number = 0;
				
		public function setContentArea(x:int, y:int, width:int, height:int):void {
			this.minContentX = x;
			this.minContentY = y;
			
			this.maxContentWidth = width;
			this.maxContentHeight = height;
			
			invalidateDisplayList();			
		}
		
		override public function setContentSize(width:Number, height:Number):void {
			if (!isNaN(maxContentWidth)) {
				width = maxContentWidth;
			}
			if (!isNaN(maxContentHeight)) {
				height = maxContentHeight;
			}
			super.setContentSize(width, height);
		}		
	}
	
}