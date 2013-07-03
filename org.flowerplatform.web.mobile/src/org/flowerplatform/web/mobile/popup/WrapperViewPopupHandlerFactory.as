package org.flowerplatform.web.mobile.popup {
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.popup.IPopupHandlerFactory;
	
	import spark.components.Scroller;
	import spark.components.View;
	import spark.components.ViewNavigator;
	
	public class WrapperViewPopupHandlerFactory implements IPopupHandlerFactory {
		
		public function createPopupHandler():IPopupHandler {
			return new WrapperViewPopupHandler();
		}
		
		public function removePopup(popupContent:IVisualElement):void {
			var viewNavigator:ViewNavigator = ViewNavigator(FlexGlobals.topLevelApplication.navigator);
			var view:View = View(viewNavigator.activeView);
			if (view is WrapperView && view.numElements > 0 && Scroller(view.getElementAt(0)).viewport == popupContent) {
				viewNavigator.popView();
			}
		}
		
	}
}