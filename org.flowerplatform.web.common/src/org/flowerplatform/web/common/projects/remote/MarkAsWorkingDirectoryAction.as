package org.flowerplatform.web.common.projects.remote {
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	public class MarkAsWorkingDirectoryAction extends ActionBase {
		public function MarkAsWorkingDirectoryAction() {
			label = WebCommonPlugin.getInstance().getMessage("explorer.markAsWorkingDirectory.action");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/workset.gif");
		}
		
		public static function isProjectsActionVisible(nodeType:String):Boolean {
			if (nodeType == WebCommonPlugin.NODE_TYPE_PROJECT || nodeType == WebCommonPlugin.NODE_TYPE_PROJ_FILE || 
				!WebCommonPlugin.getInstance().nodeTypeBelongsToNodeTypeCategory(nodeType, WebCommonPlugin.NODE_TYPE_CATEGORY_DECORATABLE_FILE)) {
				return false;
			} else {
				return true;
			}
		}
		
		override public function get visible():Boolean {
			if (selection == null || selection.length != 1) {
				return false;
			}
			var obj:Object = selection.getItemAt(0);
			return isProjectsActionVisible(TreeNode(obj).pathFragment.type);
		}
		
		override public function run():void	{
			var selectedTreeNode:TreeNode = TreeNode(selection.getItemAt(0));
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("projectsService", "markAsWorkingDirectory", [selectedTreeNode.getPathForNode(true)]));
		}
		
	}
}