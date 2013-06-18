package  org.flowerplatform.web.git.action {
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.GitService;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class DeleteRepositoryAction extends ActionBase  {
		
		private var selectedNode:TreeNode;
		
		public function DeleteRepositoryAction() {
			label = GitPlugin.getInstance().getMessage("git.action.deleteRepo.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/delete_obj.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.deleteRepo.sortIndex"));
		}
				
		override public function get visible():Boolean {	
			if (selection.length == 1 && selection.getItemAt(0) is HierarchicalModelWrapper) {
				selectedNode = HierarchicalModelWrapper(selection.getItemAt(0)).treeNode as TreeNode; 
				return selectedNode.pathFragment.type == GitNodeType.NODE_TYPE_REPOSITORY;
			}
			return false;
		}
		
		override public function run():void {
			Alert.show(
				GitPlugin.getInstance().getMessage("git.deleteRepo.message"),
				CommonPlugin.getInstance().getMessage("confirmation"), 
				Alert.YES | Alert.NO, null, confirmDeleteHandler, null, Alert.YES); 
		}
		
		private function confirmDeleteHandler(event:CloseEvent):void {
			if (event.detail == Alert.YES) {
				GitPlugin.getInstance().service.deleteRepository(selectedNode);
			}
		}
	}
}