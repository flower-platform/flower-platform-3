package org.flowerplatform.flexutil.popup {
	public interface IMessageBox {
		function setTitle(value:String):IMessageBox;
		function setText(value:String):IMessageBox;
		function setWidth(value:int):IMessageBox;
		function setHeight(value:int):IMessageBox;
		function setWordWrap(value:Boolean):IMessageBox;
		
		function showMessageBox(modal:Boolean = true):void;
	}
}