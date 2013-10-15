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
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.CommitView;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class CommitAction extends ActionBase {
		public function CommitAction()	{
			label = GitCommonPlugin.getInstance().getMessage("git.action.commit.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/commit.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.commit.team.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {		
				var node:TreeNode = TreeNode(selection.getItemAt(0));
				return (node.pathFragment.type == WebCommonPlugin.NODE_TYPE_PROJ_FILE || node.pathFragment.type == WebCommonPlugin.NODE_TYPE_PROJECT)
					&& node.customData != null 
					&& Boolean(node.customData[GitCommonPlugin.TREE_NODE_GIT_FILE_TYPE]);
			}
			return false;
		}
		
		override public function run():void {
			var popup:CommitView = new CommitView();
			popup.selectedNodes = ArrayList(selection);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(popup)	
				.setWidth(400)
				.setHeight(450)
				.show();	
		}
		
	}
}