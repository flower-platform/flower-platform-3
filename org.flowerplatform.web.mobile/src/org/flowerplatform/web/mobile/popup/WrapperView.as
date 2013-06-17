package org.flowerplatform.web.mobile.popup {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionUtil;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.components.Button;
	import spark.components.Label;
	import spark.components.View;
	import spark.primitives.BitmapImage;
	
	public class WrapperView extends WrapperViewBase {
		
		protected var popupContent:IPopupContent;
		
		override protected function createChildren():void {
			super.createChildren();

			popupContent = IPopupContent(data.popupContent);
			popupContent.popupHost = this;
			popupContent.percentHeight = 100;
			popupContent.percentWidth = 100;
			addElement(popupContent);
			
			if (data.modalOverAllApplication) {
				navigationContent = [];
			}

			refreshActions(activePopupContent);
		}
		
		override public function get activePopupContent():IPopupContent {
			return popupContent;
		}
		
	}
}