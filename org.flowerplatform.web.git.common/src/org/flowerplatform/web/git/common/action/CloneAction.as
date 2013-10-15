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
package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.CloneView;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class CloneAction extends ActionBase {
		
		public function CloneAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.cloneRepo.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/cloneGit.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.cloneRepo.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_GIT_REPOSITORIES;
			}
			return false;
		}
		
		override public function run():void {
			var cloneWindow:CloneView = new CloneView();
			cloneWindow.selectedNode = selection.getItemAt(0) as TreeNode;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(cloneWindow)
				.setWidth(400)
				.setHeight(350)					
				.show();	
		}
		
	}
}