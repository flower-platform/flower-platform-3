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
package  org.flowerplatform.web.git.action {
	import flash.events.MouseEvent;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class DeleteRepositoryAction extends ActionBase  {
		
		private var selectedNode:TreeNode;
		
		public function DeleteRepositoryAction() {
			label = GitPlugin.getInstance().getMessage("git.action.deleteRepo.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/delete_obj.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.deleteRepo.sortIndex"));
		}
				
		override public function get visible():Boolean {	
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				selectedNode = selection.getItemAt(0) as TreeNode; 
				return selectedNode.pathFragment.type == GitNodeType.NODE_TYPE_REPOSITORY;
			}
			return false;
		}
		
		override public function run():void {
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(CommonPlugin.getInstance().getMessage("confirmation"))
				.setText(GitPlugin.getInstance().getMessage("git.deleteRepo.message"))
				.addButton("Yes", confirmDeleteHandler)
				.addButton("No")
				.showMessageBox();			
		}
		
		private function confirmDeleteHandler(event:MouseEvent):void {
			GitPlugin.getInstance().service.deleteRepository(selectedNode);
		}
	}
}