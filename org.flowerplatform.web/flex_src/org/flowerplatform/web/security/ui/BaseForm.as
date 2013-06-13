package org.flowerplatform.web.security.ui {
	
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import flash.events.Event;
	
	import org.flowerplatform.web.entity.dto.Dto;
	
	import spark.components.Group;
		
	/**
	 * Base form panel.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _LA5mkFc1EeG6S8FiFZ8nVA
	 */
	public class BaseForm extends Group {
				
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
            					
		/**
		 * @flowerModelElementId _mVQecFfLEeGs_rPsEt9lFQ
		 */
		protected function formOkHandler():void {
//			closeForm();
		}
		
		protected function exceptionCallback(exception:Object):void {
//			ModalSpinner.removeModalSpinner(this);
		}

	}
	
}