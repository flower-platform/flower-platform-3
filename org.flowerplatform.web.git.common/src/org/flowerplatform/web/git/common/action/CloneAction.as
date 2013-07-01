package org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.CloneView;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class CloneAction extends ActionBase {
		
		public function CloneAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.cloneRepo.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/cloneGit.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.cloneRepo.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_GIT_REPOSITORIES;
			}
			return false;
		}
		
		override public function run():void {
			var cloneWindow:CloneView = new CloneView();
			cloneWindow.selectedNode = selection.getItemAt(0) as TreeNode;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler().setPopupContent(cloneWindow).show();
//			cloneWindow.showPopup();
		}
		
	}
}