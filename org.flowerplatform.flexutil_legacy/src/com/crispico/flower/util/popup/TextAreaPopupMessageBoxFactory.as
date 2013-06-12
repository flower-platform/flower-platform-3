package com.crispico.flower.util.popup {
	import org.flowerplatform.flexutil.popup.IMessageBox;
	import org.flowerplatform.flexutil.popup.IMessageBoxFactory;
	
	public class TextAreaPopupMessageBoxFactory implements IMessageBoxFactory {
		
		public function createMessageBox():IMessageBox {
			return new TextAreaPopup();
		}
	}
}