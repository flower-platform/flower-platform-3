package org.flowerplatform.flexutil.popup {
	
	public interface IPopupHost {
		function get activePopupContent():IPopupContent;
		function set activePopupContent(value:IPopupContent):void;
		function setLabel(value:String):void;
		function setIcon(value:Object):void;
		function refreshActions(popupContent:IPopupContent):void;	
		
		function displayCloseButton(value:Boolean):void;
		function addToControlBar(value:Object):void;
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		function showSpinner(text:String):void;
		function hideSpinner():void;
	}
}