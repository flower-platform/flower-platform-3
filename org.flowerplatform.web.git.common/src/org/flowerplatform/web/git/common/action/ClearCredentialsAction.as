package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.GitNodeType;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ClearCredentialsAction extends ActionBase {
		
		public function ClearCredentialsAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.clearCredentials.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/remove_outline.png");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.clearCredentials.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REMOTE;
			}
			return false;
		}
		
		override public function run():void {
			GitCommonPlugin.getInstance().service.clearCredentials(TreeNode(selection.getItemAt(0)));
		}
	}
}