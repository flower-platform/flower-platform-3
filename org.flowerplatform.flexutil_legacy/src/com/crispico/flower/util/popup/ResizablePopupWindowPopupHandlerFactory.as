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
package com.crispico.flower.util.popup {
	import com.crispico.flower.util.spinner.ModalSpinner;
	
	import flash.display.DisplayObject;
	
	import mx.core.FlexGlobals;
	import mx.core.IChildList;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.managers.PopUpManager;
	
	import org.flowerplatform.flexutil.popup.IPopupHandler;
	import org.flowerplatform.flexutil.popup.IPopupHandlerFactory;
	
	public class ResizablePopupWindowPopupHandlerFactory implements IPopupHandlerFactory {
		public function createPopupHandler():IPopupHandler {
			return new ResizablePopupWindowPopupHandlerAndHost();
		}
		
		public function removePopup(popupContent:IVisualElement):void {
			var popup:UIComponent;
			// from what I have seen, popups can be located in both locations
			popup = iteratePopups(FlexGlobals.topLevelApplication.systemManager, popupContent);
			if (popup == null) {
				popup = iteratePopups(FlexGlobals.topLevelApplication.systemManager.popUpChildren, popupContent);
			}
			if (popup != null) {
				PopUpManager.removePopUp(popup);
			}
		}
		
		private function iteratePopups(childList:IChildList, popupContent:IVisualElement):UIComponent {
			for (var i:int = 0; i < childList.numChildren; i++) {
				var child:DisplayObject = childList.getChildAt(i);
				if (!(child is ResizablePopupWindowPopupHandlerAndHost || child is ModalSpinner) || !UIComponent(child).isPopUp) {
					continue;
				}
				if (child is ResizablePopupWindowPopupHandlerAndHost) {
					var d:UIComponent = UIComponent(child);
					if (d.numChildren == 0 || !(d.getChildAt(0) == popupContent)) {
						continue;
					}
					return d;	
				} else {
					// ModalSpinner
					if (ModalSpinner(child).childrenUnderSpinner.length > 0 && ModalSpinner(child).childrenUnderSpinner[0] == popupContent) {
						return ModalSpinner(child);
					}
				}
			}
			return null;
		}
	}
}