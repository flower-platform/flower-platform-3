package org.flowerplatform.web.security.ui {
	
	import com.crispico.flower.util.spinner.ModalSpinner;
	import com.crispico.flower.util.spinner.ModalSpinnerSupport;
	
	import flash.events.Event;
	
	import mx.collections.IList;
	import mx.containers.VBox;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	import org.flowerplatform.web.common.entity.dto.Dto;
		
	/**
	 * Base form panel.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @author Mariana
	 * @flowerModelElementId _LA5mkFc1EeG6S8FiFZ8nVA
	 */
	public class BaseForm extends VBox implements IPopupContent, ModalSpinnerSupport {
				
		/**
		 * @flowerModelElementId _gJ5hsFfGEeGs_rPsEt9lFQ
		 */
		private var _dto:Dto;
		
		/**
		 * @flowerModelElementId _CnbI0FyIEeGwx-0cTKUc5w
		 */
		private var _entityId:Number;
		
		/**
		 * @flowerModelElementId _CnbI0VyIEeGwx-0cTKUc5w
		 */
		private var _parentListPanel:BaseListPanel;
		
		private var _popupHost:IPopupHost;
				
		/**
		 * @flowerModelElementId _KKbXkFlkEeGRrZ75u0k71A
		 */
		public function get dto():Dto {
			return _dto;
		}   	
		
		/**
		 * @flowerModelElementId _KKbXkllkEeGRrZ75u0k71A
		 */
		public function set dto(_dto:Dto):void {
			this._dto = _dto;
			dispatchEvent(new Event("dtoChangedEvent"));
		}
		
		/**
		 * @flowerModelElementId _CnbI0lyIEeGwx-0cTKUc5w
		 */
		[Bindable]
		public function get entityId():Number {
			return _entityId;
		}
		
		/**
		 * @flowerModelElementId _CnbI1FyIEeGwx-0cTKUc5w
		 */
		public function set entityId(_entityId:Number):void {
			this._entityId = _entityId;
		}
            	
		/**
		 * @flowerModelElementId _CnbI11yIEeGwx-0cTKUc5w
		 */
		public function get parentListPanel():BaseListPanel {
			return _parentListPanel;
		}
		
		/**
		 * @flowerModelElementId _CnbI2VyIEeGwx-0cTKUc5w
		 */
		public function set parentListPanel(value:BaseListPanel):void {
			this._parentListPanel = value;
		}
            	
		public function getActions(selection:IList):Vector.<IAction> {
			return null;
		}
		
		public function getSelection():IList {
			return null;
		}
		
		public function set popupHost(value:IPopupHost):void {
			_popupHost = value;
		}
		
		public function get popupHost():IPopupHost {
			return _popupHost;
		}
		
		/**
		 * @flowerModelElementId _mVQecFfLEeGs_rPsEt9lFQ
		 */
		protected function formOkHandler():void {
			FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(this);
		}
		
		/**
		 * @author Mariana
		 */
		protected function formCancelHandler():void {
			FlexUtilGlobals.getInstance().popupHandlerFactory.removePopup(this);
		}
		
		protected function exceptionCallback(exception:Object):void {
			ModalSpinner.removeModalSpinner(this);
		}

		private var _modalSpinner:ModalSpinner;
		
		public function get modalSpinner():ModalSpinner	{
			return _modalSpinner;
		}
		
		public function set modalSpinner(value:ModalSpinner):void {
			_modalSpinner = value;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if (modalSpinner != null) {
				modalSpinner.setActualSize(unscaledWidth, unscaledHeight);
			}
		}
		
	}
}