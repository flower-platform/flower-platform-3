package org.flowerplatform.flexutil.popup {
	import org.flowerplatform.flexutil.popup.IMessageBox;
	import org.flowerplatform.flexutil.popup.IMessageBoxFactory;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AlertPopupMessageBoxFactory implements IMessageBoxFactory {
		
		public function createMessageBox():IMessageBox {
			return new AlertPopup();
		}
	}
}