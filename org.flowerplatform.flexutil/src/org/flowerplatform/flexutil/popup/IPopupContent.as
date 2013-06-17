package org.flowerplatform.flexutil.popup {
	import mx.collections.IList;
	import mx.core.IVisualElement;

	public interface IPopupContent extends IVisualElement, IActionProvider {
		function set popupHost(popupHost:IPopupHost):void;
		function getSelection():IList;
	}
}