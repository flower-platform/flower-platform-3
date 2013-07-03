package  org.flowerplatform.web.git.common.action {
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.ConfigRemoteView;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class CreateRemoteAction extends ActionBase  {
		
		public function CreateRemoteAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.createRemote.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl('images/full/obj16/remote_entry_tbl.gif');
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.createRemote.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTES;
			}
			return false;
		}
		
		override public function run():void {
			var configWindow:ConfigRemoteView = new ConfigRemoteView();
			configWindow.node = selection.getItemAt(0) as TreeNode;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(configWindow)
				.setWidth(400)
				.setHeight(350)					
				.show();
		}	
		
	}
}