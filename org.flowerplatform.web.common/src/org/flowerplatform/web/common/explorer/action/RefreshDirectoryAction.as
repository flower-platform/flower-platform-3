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
package org.flowerplatform.web.common.explorer.action {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;

	/**
	 * @author Tache Razvan
	 **/
	public class RefreshDirectoryAction extends ActionBase {
		
		public var selectedTreeNode:TreeNode;
		
		public function RefreshDirectoryAction() {
			label = WebCommonPlugin.getInstance().getMessage("explorer.refresh.action");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/refresh.gif");
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);		
			return !(TreeNode(obj).customData == null) && (TreeNode(obj).customData[WebCommonPlugin.TREE_NODE_FILE_SYSTEM_IS_DIRECTORY]);
		}
		
		override public function run():void	{
			selectedTreeNode = TreeNode(selection.getItemAt(0));
			
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("fileManagerService", "refreshDirectory", [selectedTreeNode.getPathForNode(true)]));
		}
	}
}