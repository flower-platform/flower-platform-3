package org.flowerplatform.flexutil.selectiontrianglelist.mobile {
		
	import org.flowerplatform.flexutil.selectiontrianglelist.GradientListSkinLogic;
	
	import spark.skins.mobile.ListSkin;
	 
	/**
	 * ListSkin for mobile, with gradient background (using <code>GradientListSkinLogic</code>).
	 * 
	 * @author Mariana  
	 */ 
	public class GradientBackgroundMobileListSkin extends ListSkin {
		
		override protected function initializationComplete():void {
			super.initializationComplete();
			GradientListSkinLogic.initializationComplete(hostComponent);
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			GradientListSkinLogic.updateDisplayList(unscaledWidth, unscaledHeight, hostComponent);
		}	
		
	}
}