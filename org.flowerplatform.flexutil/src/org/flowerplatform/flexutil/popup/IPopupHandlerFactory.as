package org.flowerplatform.flexutil.popup {
	import mx.core.IVisualElement;

	public interface IPopupHandlerFactory {
		function createPopupHandler():IPopupHandler;
		function removePopup(popupContent:IVisualElement):void;
	}
}