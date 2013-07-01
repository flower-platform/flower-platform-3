package org.flowerplatform.web.git.action {
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.history.GitHistoryView;
	import org.flowerplatform.web.git.history.GitHistoryViewProvider;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ShowHistoryAction extends ActionBase {
		
		public function ShowHistoryAction() {
			label = GitPlugin.getInstance().getMessage("git.action.showHistory.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/history.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.showHistory.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				var node:TreeNode = TreeNode(selection.getItemAt(0));
				return node.pathFragment.type == GitNodeType.NODE_TYPE_REPOSITORY ||
					node.pathFragment.type == GitNodeType.NODE_TYPE_LOCAL_BRANCH;
			}
			return false;
		}
		
		override public function run():void {
			var workbench:Workbench = Workbench(FlexUtilGlobals.getInstance().workbench);
			var component:UIComponent = workbench.getComponent(GitHistoryViewProvider.ID);
			
			// verifies if the view is already on workbench
			if (component == null) {
				var parentStack:StackLayoutData = workbench.getStackBelowEditorSash();				
				component = workbench.addNormalView(GitHistoryViewProvider.ID, false, -1, false, parentStack);
			}
			workbench.callLater(workbench.activeViewList.setActiveView, [component]);
			
			if (component != null) {
				GitHistoryView(component).refreshClickHandler(true);
			}
		}
	}
}