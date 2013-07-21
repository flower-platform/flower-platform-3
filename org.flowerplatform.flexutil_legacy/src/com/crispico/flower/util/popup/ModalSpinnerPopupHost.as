package com.crispico.flower.util.popup {
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	public class ModalSpinnerPopupHost extends ModalSpinner implements IPopupHost {
		
		public function ModalSpinnerPopupHost(popupContent:IPopupContent) {
			super();
			dontShowSpinner = true;
			childrenUnderSpinner = [popupContent];
		}
		
		public function get activePopupContent():IPopupContent {
			return IPopupContent(childrenUnderSpinner[0]);
		}
		
		public function set activePopupContent(value:IPopupContent):void {		
		}
		
		public function refreshActions(popupContent:IPopupContent):void {
			// doesn't support this			
		}
		
		public function setIcon(value:Object):void {
			// doesn't support this			
		}
		
		public function setLabel(value:String):void {
			// doesn't support this			
		}
		
		/**
		 * @author Mariana
		 */
		public function displayCloseButton(value:Boolean):void {
			// doesn't support this	
		}
		
		/**
		 * @author Mariana
		 */
		public function addToControlBar(value:Object):void {
			// doesn't support this	
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function showSpinner(text:String):void {			
		}
		
		/**
		 * @author Cristina Constantinescu
		 */
		public function hideSpinner():void {			
		}
	}
}