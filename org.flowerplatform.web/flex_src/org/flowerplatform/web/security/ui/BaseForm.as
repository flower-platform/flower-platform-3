/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.security.ui {
	
	import com.crispico.flower.util.spinner.ModalSpinner;
	import com.crispico.flower.util.spinner.ModalSpinnerSupport;
	
	import flash.events.Event;
	
	import mx.collections.IList;
	import mx.containers.VBox;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.web.common.entity.dto.Dto;
		
	/**
	 * Base form panel.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @author Mariana
	 * 
	 */
	public class BaseForm extends VBox implements IViewContent, ModalSpinnerSupport {
				
		/**
		 * 
		 */
		private var _dto:Dto;
		
		/**
		 * 
		 */
		private var _entityId:Number;
		
		/**
		 * 
		 */
		private var _parentListPanel:BaseListPanel;
		
		private var _viewHost:IViewHost;
				
		/**
		 * 
		 */
		public function get dto():Dto {
			return _dto;
		}   	
		
		/**
		 * 
		 */
		public function set dto(_dto:Dto):void {
			this._dto = _dto;
			dispatchEvent(new Event("dtoChangedEvent"));
		}
		
		/**
		 * 
		 */
		[Bindable]
		public function get entityId():Number {
			return _entityId;
		}
		
		/**
		 * 
		 */
		public function set entityId(_entityId:Number):void {
			this._entityId = _entityId;
		}
            	
		/**
		 * 
		 */
		public function get parentListPanel():BaseListPanel {
			return _parentListPanel;
		}
		
		/**
		 * 
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
		
		public function set viewHost(value:IViewHost):void {
			_viewHost = value;
		}
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
		
		/**
		 * 
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