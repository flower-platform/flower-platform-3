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
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	import spark.components.View;
	import spark.components.ViewMenuItem;
	
	public class OpenMenuAction extends ActionBase {
		
		protected var view:WrapperViewBase;
		public var viewMenuItems:Vector.<ViewMenuItem>;
		
		public function OpenMenuAction(view:WrapperViewBase) {
			this.view = view;
			icon = CommonPlugin.getInstance().getResourceUrl("images/menu.png");
		}
		
		override public function get enabled():Boolean {
			return viewMenuItems != null && viewMenuItems.length > 0;
		}
		
		
		override public function run():void {
			view.viewMenuItems = viewMenuItems; 
			FlexGlobals.topLevelApplication.viewMenuOpen = true;
		}
		
	}
}