package org.flowerplatform.web.git.action {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.ui.CommitWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class CommitAction extends ActionBase {
		public function CommitAction()	{
			label = GitPlugin.getInstance().getMessage("git.action.commit.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/commit.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.commit.team.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == "f";
			}
			return false;
		}
		
		override public function run():void {
			var popup:CommitWindow = new CommitWindow();
			popup.selectedNodes = ArrayList(selection);
			popup.showPopup();
		}
		
	}
}