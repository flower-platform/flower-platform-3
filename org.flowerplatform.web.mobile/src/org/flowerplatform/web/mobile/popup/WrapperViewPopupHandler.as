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
package org.flowerplatform.web.mobile.popup {
	import mx.core.FlexGlobals;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	
	public class WrapperViewPopupHandler implements IPopupHandler {
		
		protected var _popupContent:IPopupContent;
		
		public function setTitle(value:String):IPopupHandler {
			return this;
		}
		
		public function setWidth(value:int):IPopupHandler {
			return this;
		}
		
		public function setHeight(value:int):IPopupHandler {
			return this;
		}
		
		public function setPopupContent(value:IPopupContent):IPopupHandler {
			_popupContent = value;
			return this;
		}
		
		public function show(modal:Boolean=true):void {
			showInternal(false);
		}
		
		public function showModalOverAllApplication():void {
			showInternal(true);		
		}
		
		private function showInternal(modalOverAllApplication:Boolean):void {
			FlexGlobals.topLevelApplication.navigator.pushView(WrapperView, { 
				popupContent: _popupContent,
				popupHandler: this, 
				modalOverAllApplication: modalOverAllApplication
			});
		}
		
	}
}