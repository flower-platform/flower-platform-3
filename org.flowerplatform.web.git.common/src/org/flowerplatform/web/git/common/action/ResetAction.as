package  org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.remote.dto.GitActionDto;
	import org.flowerplatform.web.git.common.ui.ResetView;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ResetAction extends ActionBase {
		
		private var node:TreeNode;
		
		public function ResetAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.reset.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/reset.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.reset.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_LOCAL_BRANCH;
			}
			return false;
		}
		
		private function getNodeAdditionalDataCallbackHandler(result:GitActionDto):void {
			if (result != null) {
				var popup:ResetView = new ResetView();
				popup.dto = result;
				popup.dto.repositoryNode = node.parent.parent;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setPopupContent(popup)
					.setWidth(400)
					.setHeight(400)					
					.show();	
			}
		}
		
		override public function run():void {
			node = TreeNode(selection.getItemAt(0));
			GitCommonPlugin.getInstance().service.getNodeAdditionalData(node, this, getNodeAdditionalDataCallbackHandler);
		}				
	
	}
}