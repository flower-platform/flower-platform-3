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
	
	import mx.collections.*;
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.PathFragment;
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
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/checkout.gif");
		}	
		
		public override function get visible():Boolean {				
			if (selection.length == 0)
				return false;
			if (!selection.getItemAt(0) is TreeNode)
				return false;
			var node:TreeNode  = TreeNode(selection.getItemAt(0));
			var node_type:String = node.pathFragment.type;
			if(node_type != SvnCommonPlugin.NODE_TYPE_REPOSITORY && node_type != SvnCommonPlugin.NODE_TYPE_FOLDER)
				return false;
			for (var i:int = 0; i<selection.length; i++) {
				var currentNode:TreeNode = TreeNode(selection.getItemAt(i)); 
				var pathFragmentsOfSelection:ArrayCollection = currentNode.getPathForNode(true);
				if (PathFragment(pathFragmentsOfSelection.getItemAt(pathFragmentsOfSelection.length - 1)).type != SvnCommonPlugin.NODE_TYPE_FOLDER)
					return false;
			}
			var organizationName:String = PathFragment(TreeNode(selection.getItemAt(0)).getPathForNode(false).getItemAt(0)).name;
			for (var j:int=0; j<selection.length; j++) {
				var treeNode:TreeNode = TreeNode(selection.getItemAt(j));
				if (organizationName != PathFragment(treeNode.getPathForNode(false).getItemAt(0)).name) {
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
