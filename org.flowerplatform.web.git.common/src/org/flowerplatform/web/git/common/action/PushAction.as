package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.PushView;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class PushAction extends ActionBase {
		
		private var configPush:Boolean;
		
		public function PushAction(configPush:Boolean = false) {
			this.configPush = configPush;
			
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/push.gif");
			if (configPush) {
				label = GitCommonPlugin.getInstance().getMessage("git.action.push.label");				
				orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.push.sortIndex"));				
			} else {
				label = GitCommonPlugin.getInstance().getMessage("git.action.pushFromRemote.label");
				orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.pushFromRemote.sortIndex"));			
			}			
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				if (configPush) {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REPOSITORY;
				} else {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE;
				}				
			}
			return false;
		}
		
		override public function run():void {
			if (configPush) {
				var popup:PushView = new PushView();
				popup.node = TreeNode(selection.getItemAt(0));
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setPopupContent(popup)
					.setWidth(450)
					.setHeight(400)		
					.show();
			} else {
				GitCommonPlugin.getInstance().service.push(TreeNode(selection.getItemAt(0)), null, null, null);
			}
		}
	}
}