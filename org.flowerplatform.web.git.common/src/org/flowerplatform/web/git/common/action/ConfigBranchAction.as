package org.flowerplatform.web.git.common.action {
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.ConfigBranchView;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ConfigBranchAction extends ActionBase {
		
		public function ConfigBranchAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.configBranch.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/editconfig.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.configBranch.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				var node:TreeNode = TreeNode(selection.getItemAt(0));
				return node.pathFragment.type == GitCommonPlugin.NODE_TYPE_LOCAL_BRANCH;
			}
			return false;
		}
				
		override public function run():void {		
			var popup:ConfigBranchView = new ConfigBranchView();
			popup.node = TreeNode(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(popup)
				.setWidth(400)
				.setHeight(350)
				.show();			
		}
	}
}