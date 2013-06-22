package org.flowerplatform.web.git.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ChangeCredentialsAction extends ActionBase {
		
		public function ChangeCredentialsAction() {
			label = GitPlugin.getInstance().getMessage("git.action.changeCredentials.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/permission.png");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.changeCredentials.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REMOTE;
			}
			return false;
		}
		
		override public function run():void {
			GitPlugin.getInstance().service.openCredentials(TreeNode(selection.getItemAt(0)));
		}
		
	}
}