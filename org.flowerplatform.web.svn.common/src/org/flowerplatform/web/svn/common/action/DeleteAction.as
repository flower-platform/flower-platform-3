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
	
	import flash.events.MouseEvent;
	import flash.utils.flash_proxy;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.core.mx_internal;
	import mx.utils.object_proxy;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.CommentView;
	
	/**
	 * @author Cristina Necula
	 */
	
	public class DeleteAction extends ActionBase implements IDialogResultHandler {
		
		private var selectedNodes:ArrayCollection = new ArrayCollection();
		
		public function DeleteAction() {
			
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.deleteAction.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/cancel_delete.png");
		
		}
		
		override public function get visible():Boolean {
			if (selection.length >= 1 && selection.getItemAt(0) is TreeNode){
				for(var i:int = 0; i < selection.length; i++){
					var selectedNode:TreeNode = selection.getItemAt(i) as TreeNode;
					if (!((selectedNode.pathFragment.type == SvnCommonPlugin.NODE_TYPE_REPOSITORY) ||
						(selectedNode.pathFragment.type == SvnCommonPlugin.NODE_TYPE_FILE)))
						return false;
				}
				return true;
			}
			return true;
			
		}
		
		override public function run():void {
			
			for(var i:int = 0; i < selection.length; i++){
				var node:TreeNode = selection.getItemAt(i) as TreeNode;
				selectedNodes.addItem(node.getPathForNode(true));
				
			}
			
			var message:String = SvnCommonPlugin.getInstance().getMessage("svn.action.deleteAction.message");
			var messageName:String = message;
			if (selection.length == 1) {
				var nodeName:String = selection.getItemAt(0).pathFragment.name;
				messageName += " \"" + nodeName + "\"" + "?";
			} 
			else {
				for(var i:int = 0; i < selection.length - 1; i++){
					var nodeName:String = selection.getItemAt(i).pathFragment.name.toString();
					messageName += " \"" + nodeName + "\"" + ",";
				}
				messageName += " \"" + selection.getItemAt(selection.length - 1).pathFragment.name.toString() + "\"?";
			}
			
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(CommonPlugin.getInstance().getMessage("confirmation"))
				.setText(messageName)
				.addButton("Yes", confirmDeleteHandler)
				.addButton("No")
				.setWidth(300)
				.setHeight(300)
				.showMessageBox();			
		}
		
		
		private function confirmDeleteHandler(event:MouseEvent):void {
			var view:CommentView = new CommentView();
			view.setResultHandler(this);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(view)
				.setWidth(400)
				.setHeight(350)	
				.show();
			
		}
		
		public function handleDialogResult(result:Object):void {
			var comment:String = result as String;
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("svnService", 
					"deleteSvnAction", [new ArrayCollection(selectedNodes.toArray()), comment], this, null));
			
		}
		
	}
}