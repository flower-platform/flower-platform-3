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

package org.flowerplatform.web.common.projects.remote
{	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	/**
	 * @author Tache Razvan
	 **/
	public class CreateDirectoryAction extends ActionBase implements IDialogResultHandler {
		
		public var selectedTreeNode:TreeNode;
		
		public function CreateDirectoryAction() {
			label = WebCommonPlugin.getInstance().getMessage("explorer.createDirectory.action");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/newfolder_wiz.gif");
		}

		public function isFilesActionVisible(nodeType:String,treeNode:TreeNode) :Boolean {
			if( !(treeNode.customData == null || treeNode.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE] == null) ||
				!WebCommonPlugin.getInstance().nodeTypeBelongsToNodeTypeCategory(nodeType, WebCommonPlugin.NODE_TYPE_CATEGORY_DECORATABLE_FILE)
				) {
				return false;
			} else {
				return true;	
			}
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);		
			return isFilesActionVisible(TreeNode(obj).pathFragment.type,TreeNode(obj));
		}
		
		override public function run():void	{
			selectedTreeNode = TreeNode(selection.getItemAt(0));
			var view:TextInputView = new TextInputView();
			view.label = WebCommonPlugin.getInstance().getMessage("explorer.createDirectory.input.label");
			view.title = WebCommonPlugin.getInstance().getMessage("explorer.createDirectory.input.title");
			view.setResultHandler(this);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(view)
				.show();		
		}
		
		public function handleDialogResult(result:Object):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
			new InvokeServiceMethodServerCommand("fileManagerService", "createDirectory", [selectedTreeNode.getPathForNode(true),result]));
		}
	}
	
}