package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.FetchView;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class FetchAction extends ActionBase {
		
		private var configFetch:Boolean;
		
		public function FetchAction(configFetch:Boolean = false) {
			this.configFetch = configFetch;
			
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/fetch.gif");
			if (configFetch) {
				label = GitCommonPlugin.getInstance().getMessage("git.action.fetch.label");				
				orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.fetch.sortIndex"));				
			} else {
				label = GitCommonPlugin.getInstance().getMessage("git.action.fetchFromRemote.label");
				orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.fetchFromRemote.sortIndex"));			
			}			
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				if (configFetch) {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REPOSITORY;
				} else {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE;
				}				
			}
			return false;
		}
		
		override public function run():void {
			if (configFetch) {
				var popup:FetchView = new FetchView();
				popup.node = TreeNode(selection.getItemAt(0));
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setPopupContent(popup)
					.setWidth(450)
					.setHeight(400)		
					.show();
			} else {
				GitCommonPlugin.getInstance().service.fetch(TreeNode(selection.getItemAt(0)), null, null, null);
			}
		}
	}
}