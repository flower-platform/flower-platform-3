package org.flowerplatform.flexutil.popup {
	public interface IPopupHost {
		function get activePopupContent():IPopupContent;
		function setLabel(value:String):void;
		function setIcon(value:Object):void;
		function refreshActions(popupContent:IPopupContent):void;	
	}
}