package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ChangeCredentialsAction extends ActionBase {
		
		public function ChangeCredentialsAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.changeCredentials.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/permission.png");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.changeCredentials.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE;
			}
			return false;
		}
		
		override public function run():void {
			GitCommonPlugin.getInstance().service.openCredentials(TreeNode(selection.getItemAt(0)));
		}
		
	}
}