package org.flowerplatform.web.git.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.GitService;
	import org.flowerplatform.web.git.ui.FetchWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class FetchAction extends ActionBase {
		
		private var configFetch:Boolean;
		
		public function FetchAction(configFetch:Boolean = false) {
			this.configFetch = configFetch;
			
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/fetch.gif");
			if (configFetch) {
				label = GitPlugin.getInstance().getMessage("git.action.fetch.label");				
				orderIndex = int(GitPlugin.getInstance().getMessage("git.action.fetch.sortIndex"));				
			} else {
				label = GitPlugin.getInstance().getMessage("git.action.fetchFromRemote.label");
				orderIndex = int(GitPlugin.getInstance().getMessage("git.action.fetchFromRemote.sortIndex"));			
			}			
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				if (configFetch) {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REPOSITORY;
				} else {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REMOTE;
				}				
			}
			return false;
		}
		
		override public function run():void {
			if (configFetch) {
				var popup:FetchWindow = new FetchWindow();
				popup.node = TreeNode(selection.getItemAt(0));
				popup.showPopup();
			} else {
				GitPlugin.getInstance().service.	fetch(TreeNode(selection.getItemAt(0)), null, -1, false, null, null);
			}
		}
	}
}