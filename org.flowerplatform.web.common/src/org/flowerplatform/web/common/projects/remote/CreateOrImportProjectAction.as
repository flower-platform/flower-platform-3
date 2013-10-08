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
package org.flowerplatform.web.common.projects.remote {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.command.CompoundServerCommand;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	public class CreateOrImportProjectAction extends ActionBase {
		public function CreateOrImportProjectAction() {
			label = WebCommonPlugin.getInstance().getMessage("explorer.createOrImportProject.action");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/project.gif");
			parentId = "new";
			orderIndex = 30;
		}
		
		override public function get visible():Boolean {
			if (selection == null) {
				return false;
			}
			
			for (var i:int = 0; i < selection.length; i++) {
				var obj:Object = selection.getItemAt(i);
				if (!MarkAsWorkingDirectoryAction.isProjectsActionVisible(TreeNode(obj).pathFragment.type)) {
					return false;
				}
			}
			return true;
		}
		
		override public function run():void	{
			var command:CompoundServerCommand = new CompoundServerCommand();
			for (var i:int = 0; i < selection.length; i++) {
				var selectedTreeNode:TreeNode = TreeNode(selection.getItemAt(i));
				command.append(new InvokeServiceMethodServerCommand("projectsService", "createOrImportProject", [selectedTreeNode.getPathForNode(true)]));
			}
			CommunicationPlugin.getInstance().bridge.sendObject(command);
		}
		
	}
}