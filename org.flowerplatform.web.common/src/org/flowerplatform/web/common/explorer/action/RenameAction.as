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
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.common.explorer.ui.TextInputView;

	/**
	 * @author Tache Razvan
	 **/
	public class RenameAction extends ActionBase implements IDialogResultHandler {
		
		public var selectedTreeNode:TreeNode;
		
		public function RenameAction() {
			label = WebCommonPlugin.getInstance().getMessage("explorer.rename.action");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/edit.png");
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);		
			return !(TreeNode(obj).customData == null) && (TreeNode(obj).customData[WebCommonPlugin.TREE_NODE_FILE_SYSTEM_IS_DIRECTORY] != null)
				|| TreeNode(obj).pathFragment.type == WebCommonPlugin.NODE_TYPE_WORKING_DIRECTORY;
		}
		
		override public function run():void	{
			selectedTreeNode = TreeNode(selection.getItemAt(0));
			var view:TextInputView = new TextInputView();
			view.node=TreeNode(selection.getItemAt(0));
			view.label = WebCommonPlugin.getInstance().getMessage("explorer.rename.input.label");
			view.title = WebCommonPlugin.getInstance().getMessage("explorer.rename.input.title");
			view.popupIcon = WebCommonPlugin.getInstance().getResourceUrl("images/icon_flower.gif");
			view.setResultHandler(this);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(view)
				.show();
		}
		
		public function handleDialogResult(result:Object):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("fileManagerService", "rename", [selectedTreeNode.getPathForNode(true),result]));
		}
	}
}