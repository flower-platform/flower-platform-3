package  org.flowerplatform.web.git.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.ui.ConfigRemoteWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class CreateRemoteAction extends ActionBase  {
		
		public function CreateRemoteAction() {
			label = GitPlugin.getInstance().getMessage("git.action.createRemote.label");
			icon = GitPlugin.getInstance().getResourceUrl('images/full/obj16/remote_entry_tbl.gif');
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.createRemote.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REMOTES;
			}
			return false;
		}
		
		override public function run():void {
			var configWindow:ConfigRemoteWindow = new ConfigRemoteWindow();
			configWindow.node = selection.getItemAt(0) as TreeNode;
			configWindow.showPopup();
		}	
		
	}
}