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
package org.flowerplatform.web.git.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.ui.CheckoutWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class CheckoutAction extends ActionBase	{
		
		public function CheckoutAction() {
			label = GitPlugin.getInstance().getMessage("git.action.checkout.label");	
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/checkout.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.checkout.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				var node:TreeNode = TreeNode(selection.getItemAt(0));
				return node.pathFragment.type == GitNodeType.NODE_TYPE_REMOTE_BRANCHES ||
					node.pathFragment.type == GitNodeType.NODE_TYPE_TAG;
			}
			return false;
		}
		
		private function getName(fullName:String):String {
			return fullName.substring(fullName.lastIndexOf("/") + 1);
		}
		
		override public function run():void {		
			var popup:CheckoutWindow = new CheckoutWindow();
			popup.node = TreeNode(selection.getItemAt(0));
			popup.showPopup();			
		}
	}
}