package org.flowerplatform.flexdiagram.util.infinitegroup {
	import flash.events.Event;
	import flash.geom.Rectangle;
	
	import spark.components.DataRenderer;
	import spark.components.Group;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class InfiniteDataRenderer extends DataRenderer {
				
		private var _contentRect:Rectangle;
				
		/**
		 * The group's coordonates and dimensions.
		 */ 
		[Bindable]
		public function get contentRect():Rectangle {       
			return _contentRect;
		}
		
		public function set contentRect(value:Rectangle):void {
			if (value == _contentRect)
				return;
			
			_contentRect = value;
		}
		
		private var _scrollByToolInProgress:Boolean = false;
		
		/**
		 * Must be modified only when working on mobile.
		 * If <code>true</code> (the scrolling will be done by move/resize tool),
		 * scrollers will not be displayed. 
		 * 
		 * @see InfiniteScrollerLayout
		 */ 
		[Bindable]
		public function get scrollByToolInProgress():Boolean {       
			return _scrollByToolInProgress;
		}
		
		public function set scrollByToolInProgress(value:Boolean):void {
			if (value == _scrollByToolInProgress)
				return;
			
			_scrollByToolInProgress = value;
		}
		
		public function InfiniteDataRenderer() {
			super();
			
			layout = new InfiniteGroupBasicLayout();
			
		}
	
		/**
		 * Don't allow changing contentWidth/contentHeight.
		 * 
		 * <p>
		 * The ones from <code>contentRect</code> must be used.
		 */ 
		override public function setContentSize(width:Number, height:Number):void {
			width = _contentRect ? _contentRect.width : width;
			height = _contentRect ? _contentRect.height : height;
				
			super.setContentSize(width, height);
		}
	}
}