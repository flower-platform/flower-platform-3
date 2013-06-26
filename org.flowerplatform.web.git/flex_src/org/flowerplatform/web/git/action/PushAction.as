package org.flowerplatform.web.git.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.ui.PushWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class PushAction extends ActionBase {
		
		private var configPush:Boolean;
		
		public function PushAction(configPush:Boolean = false) {
			this.configPush = configPush;
			
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/push.gif");
			if (configPush) {
				label = GitPlugin.getInstance().getMessage("git.action.push.label");				
				orderIndex = int(GitPlugin.getInstance().getMessage("git.action.push.sortIndex"));				
			} else {
				label = GitPlugin.getInstance().getMessage("git.action.pushFromRemote.label");
				orderIndex = int(GitPlugin.getInstance().getMessage("git.action.pushFromRemote.sortIndex"));			
			}			
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				if (configPush) {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REPOSITORY;
				} else {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REMOTE;
				}				
			}
			return false;
		}
		
		override public function run():void {
			if (configPush) {
				var popup:PushWindow = new PushWindow();
				popup.node = TreeNode(selection.getItemAt(0));
				popup.showPopup();
			} else {
				GitPlugin.getInstance().service.	push(TreeNode(selection.getItemAt(0)), null, false, null, null);
			}
		}
	}
}