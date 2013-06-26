package org.flowerplatform.web.git.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DeleteRemoteAction extends ActionBase {
		
		public function DeleteRemoteAction() {
			label = GitPlugin.getInstance().getMessage("git.action.deleteRemote.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/delete_obj.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.deleteRemote.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REMOTE;
			}
			return false;
		}
		
		override public function run():void {
			GitPlugin.getInstance().service.deleteRemote(TreeNode(selection.getItemAt(0)));
		}
	}
}