package com.crispico.flower.util.popup {
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	public class ResizablePopupWindowPopupHandler extends ResizablePopupWindow implements IPopupHandler, IPopupHost	{
		
		protected var popupContent:IVisualElement;
		
		public function ResizablePopupWindowPopupHandler() {
			super();
		}
		
		public function setHeight(value:int):IPopupHandler	{
			height = value;
			return this;
		}
		
		public function setTitle(value:String):IPopupHandler {
			title = value;
			return this;
		}
		
		public function setPopupContentClass(value:Class):IPopupHandler {
			popupContent = IVisualElement(new value());
			popupContent.percentHeight = 100;
			popupContent.percentWidth = 100;
			addElement(popupContent);
			return this;
		}
		
		public function getPopupContent():IVisualElement {
			return popupContent;
		}
		
		public function setWidth(value:int):IPopupHandler {
			width = value;
			return this;
		}
		
		public function show(modal:Boolean = true):void {
			if (popupContent is IPopupContent) {
				IPopupContent(popupContent).popupHost = this;
			}
			showPopup(NaN, NaN, null, modal);
		}
		
		/**
		 * In this case, this class, ResizablePopupWindow is not used any more.
		 * The existing ModalSpinner mechanism is used for displaying the popup content.
		 */
		public function showModalOverAllApplication():void {
			var modalSpinner:ModalSpinner = new ModalSpinner();
			modalSpinner.dontShowSpinner = true;
			modalSpinner.childrenUnderSpinner = [popupContent];
			ModalSpinner.addGlobalModalSpinner(null, modalSpinner);
		}
		
		public function refreshActions(popupContent:IPopupContent):void {
			// TODO Auto Generated method stub
			
		}
		
		public function setIcon(value:Object):void {
			titleIconURL = String(value);
		}
		
		public function setLabel(value:String):void {
			title = "Login";
		}
		
	}
}