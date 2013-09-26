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
package org.flowerplatform.web.common.explorer.action
{
	import flash.events.MouseEvent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	/**
	 * @author Tache Razvan Mihai
	 */
	public class DeleteAction extends ActionBase {
		
		public var selectedTreeNode:TreeNode;
		
		public function DeleteAction() {
			label = WebCommonPlugin.getInstance().getMessage("explorer.delete.action");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/delete.gif");
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);		
			return !(TreeNode(obj).customData == null) && (TreeNode(obj).customData[WebCommonPlugin.TREE_NODE_FILE_SYSTEM_IS_DIRECTORY] != null);
		}
		
		override public function run():void	{
			selectedTreeNode = TreeNode(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(WebCommonPlugin.getInstance().getMessage("explorer.delete.confirmDelete.title"))
				.setText(WebCommonPlugin.getInstance().getMessage("explorer.delete.confirmDelete.message"))
				.addButton("Yes", confirmDeleteHandler)
				.addButton("No")
				.setHeight(100)
				.setWidth(300)
				.showMessageBox();
		}
		
		private function confirmDeleteHandler(event:MouseEvent):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("fileManagerService", "deleteFile", [selectedTreeNode.getPathForNode(true)]));
		}

	}
}