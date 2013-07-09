package  org.flowerplatform.web.git.common.action {
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class DeleteBranchAction extends ActionBase  {
		
		private var branch:TreeNode;
		
		public function DeleteBranchAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.deleteBranch.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/delete_obj.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.deleteBranch.sortIndex"));		
		}
		
		override public function get visible():Boolean {	
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				branch = selection.getItemAt(0) as TreeNode; 
				return branch.pathFragment.type == GitCommonPlugin.NODE_TYPE_LOCAL_BRANCH ||
					branch.pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE_BRANCH;
			}
			return false;
		}
		
		override public function run():void {
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setTitle(CommonPlugin.getInstance().getMessage("confirmation"))
				.setText(GitCommonPlugin.getInstance().getMessage("git.deleteBranch.message"))
				.setWidth(300)
				.setHeight(300)
				.addButton("Yes", confirmDeleteHandler)
				.addButton("No")
				.showMessageBox();			
		}
		
		private function confirmDeleteHandler(event:MouseEvent):void {
			GitCommonPlugin.getInstance().service.deleteBranch(branch, null, null);
		}
		
	}
}