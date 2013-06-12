package org.flowerplatform.flexutil.popup {
	public interface IPopupContent {
		function set popupHost(popupHost:IPopupHost):void;
		function getActions():Vector.<IAction>;
	}
}