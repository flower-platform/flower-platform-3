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
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.history.GitHistoryView;
	import org.flowerplatform.web.git.history.GitHistoryViewProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ShowHistoryAction extends ActionBase {
		
		public function ShowHistoryAction() {
			label = GitPlugin.getInstance().getMessage("git.action.showHistory.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/history.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.showHistory.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				var node:TreeNode = TreeNode(selection.getItemAt(0));
				// visible on local/remote bransh and git file type
				return node.pathFragment.type == GitCommonPlugin.NODE_TYPE_LOCAL_BRANCH ||
					node.pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE_BRANCH ||
					(node.customData != null && Boolean(node.customData[GitCommonPlugin.TREE_NODE_GIT_FILE_TYPE]));
			}
			return false;
		}
		
		override public function run():void {
			var workbench:Workbench = Workbench(FlexUtilGlobals.getInstance().workbench);
			var component:UIComponent = workbench.getComponent(GitHistoryViewProvider.ID);
			
			// verifies if the view is already on workbench
			if (component == null) {
				var parentStack:StackLayoutData = workbench.getStackBelowEditorSash();				
				component = workbench.addNormalView(GitHistoryViewProvider.ID, false, -1, false, parentStack);
			}
			workbench.callLater(workbench.activeViewList.setActiveView, [component]);
			
			if (component != null) {
				GitHistoryView(component).refreshClickHandler(true);
			}
		}
	}
}