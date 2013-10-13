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
package org.flowerplatform.flexutil.mobile.popup {
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionUtil;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.components.Button;
	import spark.components.Label;
	import spark.components.Scroller;
	import spark.components.View;
	import spark.core.IViewport;
	import spark.primitives.BitmapImage;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class WrapperView extends WrapperViewBase {
		
		override protected function createChildren():void {
			super.createChildren();
			
			var scroller:Scroller = new Scroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			addElement(scroller);
			
			setActivePopupContent(IPopupContent(data.popupContent));
			activePopupContent.popupHost = this;
			activePopupContent.percentHeight = 100;
			activePopupContent.percentWidth = 100;
			scroller.viewport = IViewport(activePopupContent);
			
			if (data.modalOverAllApplication) {
				navigationContent = [];
			}
		}
		
	}
}