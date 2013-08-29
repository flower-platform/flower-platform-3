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

package org.flowerplatform.web.svn.common.action {
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.CheckoutView;
	
	/**
	 * @author Victor Badila
	 */
	public class CheckoutAction extends ActionBase {
		
		public function CheckoutAction() {
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.checkout.label");
			icon = null;
		}	
		
		public override function get visible():Boolean {				
			if (selection.length == 0)
				return false;
			if (!selection.getItemAt(0) is TreeNode)
				return false;									
			var node_type:String = TreeNode(selection.getItemAt(0)).pathFragment.type;
			if(node_type != SvnCommonPlugin.NODE_TYPE_REPOSITORY && node_type != SvnCommonPlugin.NODE_TYPE_FILE)
				return false;
			for (var i:int = 0; i<selection.length; i++) {
				if (TreeNode(selection.getItemAt(i)).pathFragment.type != node_type) {
					return false;
				}			
			}
			return true;
		}
		
		public override function run():void {			
			var view:CheckoutView = new CheckoutView();
			view.selection = ArrayList(selection);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(view)
				.show();
		}		
	}
	
}