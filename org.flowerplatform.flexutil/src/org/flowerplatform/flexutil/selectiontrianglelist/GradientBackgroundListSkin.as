package org.flowerplatform.flexutil.selectiontrianglelist {
	import mx.core.ClassFactory;
	import mx.graphics.GradientEntry;
	import mx.graphics.LinearGradient;
	import mx.graphics.SolidColor;
	
	import org.flowerplatform.flexutil.selectiontrianglelist.TopAndBottomVerticalScrollBarSkin;
	
	import spark.components.Scroller;
	import spark.components.supportClasses.GroupBase;
	import spark.skins.spark.ListSkin;
	 
	/**
	 * ListSkin for desktop, with gradient background (using <code>GradientListSkinLogic</code>)
	 * and custom vertical scrollbars.
	 * 
	 * @author Mariana
	 */ 
	public class GradientBackgroundListSkin extends ListSkin {
		
		override protected function initializationComplete():void {
			super.initializationComplete();
			GradientListSkinLogic.initializationComplete(hostComponent);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			GradientListSkinLogic.updateDisplayList(unscaledWidth, unscaledHeight, hostComponent, background);
			
			// the scroller layout takes care of the position of the viewport inside the scroller
			GroupBase(scroller.skin).layout = new TopAndBottomVerticalScrollerLayout();
			
			// set the skin factory for the vertical scrollbar
			if (scroller.verticalScrollBar) {
				var factory:ClassFactory = new ClassFactory(TopAndBottomVerticalScrollBarSkin);
				// this will set the width button to the width of the component
				factory.properties = { componentWidth : this.unscaledWidth };
				scroller.verticalScrollBar.setStyle("skinFactory", factory);
			}
		}
		
	}
}