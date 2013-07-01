package  org.flowerplatform.web.git.common.action {
	import flash.events.MouseEvent;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class DeleteRepositoryAction extends ActionBase  {
		
		private var selectedNode:TreeNode;
		
		public function DeleteRepositoryAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.deleteRepo.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/delete_obj.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.deleteRepo.sortIndex"));
		}
				
		override public function get visible():Boolean {	
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				selectedNode = selection.getItemAt(0) as TreeNode; 
				return selectedNode.pathFragment.type == GitCommonPlugin.NODE_TYPE_REPOSITORY;
			}
			return false;
		}
		
		override public function run():void {
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(CommonPlugin.getInstance().getMessage("confirmation"))
				.setText(GitCommonPlugin.getInstance().getMessage("git.deleteRepo.message"))
				.addButton("Yes", confirmDeleteHandler)
				.addButton("No")
				.showMessageBox();			
		}
		
		private function confirmDeleteHandler(event:MouseEvent):void {
			GitCommonPlugin.getInstance().service.deleteRepository(selectedNode);
		}
	}
}