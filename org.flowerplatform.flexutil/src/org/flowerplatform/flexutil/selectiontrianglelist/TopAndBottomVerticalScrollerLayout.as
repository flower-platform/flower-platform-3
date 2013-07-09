package org.flowerplatform.flexutil.selectiontrianglelist {
	import org.flowerplatform.flexutil.selectiontrianglelist.TopAndBottomVerticalScrollBarSkin;
	
	import spark.components.Scroller;
	import spark.components.supportClasses.ScrollerLayout;
	import spark.components.supportClasses.Skin;
	import spark.core.IViewport;
	
	/**
	 * Custom layout for vertical scrolling. This is meant to be used together with the
	 * <code>CustomVScrollBarSkin</code>. 
	 * 
	 * @author Mariana
	 */ 
	public class TopAndBottomVerticalScrollerLayout extends ScrollerLayout {
		
		/**
		 * Position the viewport inside the scroller. 
		 * 
		 * <p>
		 * If the vertical scrollbars are visible, make sure the scroll buttons don't cover up
		 * the viewport.
		 */ 
		override public function updateDisplayList(w:Number, h:Number):void {
			super.updateDisplayList(w, h);
			
			var g:Skin = target as Skin;
			var scroller:Scroller = (g && ("hostComponent" in g)) ? Object(g).hostComponent as Scroller : null;
		
			if (scroller) {
				var viewport:IViewport = scroller.viewport;
				if (viewport && scroller.verticalScrollBar && scroller.verticalScrollBar.visible) {
					// move the viewport just under the up scroll button
					viewport.setLayoutBoundsPosition(0, TopAndBottomVerticalScrollBarSkin.BUTTON_SIZE);
					// set the height of the viewport so it doesn't get covered up by the down button
					viewport.setLayoutBoundsSize(viewport.width, viewport.height - 2 * TopAndBottomVerticalScrollBarSkin.BUTTON_SIZE);
				}
			}
		}
		
	}
}