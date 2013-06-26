package org.flowerplatform.web.git.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.ui.CloneWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class CloneAction extends ActionBase {
		
		public function CloneAction() {
			label = GitPlugin.getInstance().getMessage("git.action.cloneRepo.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/cloneGit.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.cloneRepo.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_GIT_REPOSITORIES;
			}
			return false;
		}
		
		override public function run():void {
			var cloneWindow:CloneWindow = new CloneWindow();
			cloneWindow.selectedNode = selection.getItemAt(0) as TreeNode;
			cloneWindow.showPopup();
		}
		
	}
}