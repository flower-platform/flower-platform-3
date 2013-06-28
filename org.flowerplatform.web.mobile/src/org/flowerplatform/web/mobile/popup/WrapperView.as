package org.flowerplatform.web.mobile.popup {
	import spark.components.Scroller;
	import spark.core.IViewport;
	
	import org.flowerplatform.flexutil.popup.IPopupContent;
	
	public class WrapperView extends WrapperViewBase {
		
		protected var popupContent:IPopupContent;
		
		/**
		 * @author Cristi
		 * @author Mariana
		 */
		override protected function createChildren():void {
			super.createChildren();
			
			var scroller:Scroller = new Scroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			addElement(scroller);
			
			popupContent = IPopupContent(data.popupContent);
			popupContent.popupHost = this;
			popupContent.percentHeight = 100;
			popupContent.percentWidth = 100;
			scroller.viewport = IViewport(popupContent);
			
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