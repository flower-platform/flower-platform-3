package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class PullAction extends ActionBase {
		
		public function PullAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.pull.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/pull.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.pull.team.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_LOCAL_BRANCH;							
			}
			return false;
		}
		
		override public function run():void {			
			GitCommonPlugin.getInstance().service.pull(TreeNode(selection.getItemAt(0)));
		}
	}
}