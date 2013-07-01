package org.flowerplatform.web.git.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class PullAction extends ActionBase {
		
		public function PullAction() {
			label = GitPlugin.getInstance().getMessage("git.action.pull.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/pull.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.pull.team.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_LOCAL_BRANCH;							
			}
			return false;
		}
		
		override public function run():void {			
			GitPlugin.getInstance().service.	pull(TreeNode(selection.getItemAt(0)));
		}
	}
}