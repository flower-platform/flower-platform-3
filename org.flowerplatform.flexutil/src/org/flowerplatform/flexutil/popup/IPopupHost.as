package org.flowerplatform.flexutil.popup {
	public interface IPopupHost {
		function refreshActions(popupContent:IPopupContent):void;	
		function setLabel(value:String):void;
		function setIcon(value:Object):void;
	}
}