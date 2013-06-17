package org.flowerplatform.web.mobile.popup {
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	
	public class WrapperViewPopupHandler implements IPopupHandler {
		
		protected var _popupContent:IPopupContent;
		
		public function setTitle(value:String):IPopupHandler {
			return this;
		}
		
		public function setWidth(value:int):IPopupHandler {
			return this;
		}
		
		public function setHeight(value:int):IPopupHandler {
			return this;
		}
		
		public function setPopupContent(value:IPopupContent):IPopupHandler {
			_popupContent = value;
			return this;
		}
		
		public function show(modal:Boolean=true):void {
			showInternal(false);
		}
		
		public function showModalOverAllApplication():void {
			showInternal(true);		
		}
		
		private function showInternal(modalOverAllApplication:Boolean):void {
			FlexGlobals.topLevelApplication.navigator.pushView(WrapperView, { 
				popupContent: _popupContent,
				popupHandler: this, 
				modalOverAllApplication: modalOverAllApplication
			});
		}
		
	}
}