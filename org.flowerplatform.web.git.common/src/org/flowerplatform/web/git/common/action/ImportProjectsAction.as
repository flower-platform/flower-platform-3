package org.flowerplatform.web.git.common.action {
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.ui.ImportProjectView;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ImportProjectsAction extends ActionBase {
		
		public function ImportProjectsAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.importProjects.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/full/obj16/import_prj.gif");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.importProjects.sortIndex"));
		}
		
		override public function get visible():Boolean {
			var node:TreeNode;
			if (selection.length == 1) {
				node = TreeNode(selection.getItemAt(0));
				if (node.pathFragment.type == GitCommonPlugin.NODE_TYPE_WDIR) {
					return true;
				}
			}			
			return false;
		}
		
		override public function run():void {
			var wizard:ImportProjectView = new ImportProjectView();			
			wizard.node = TreeNode(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(wizard)
				.setWidth(450)
				.setHeight(400)
				.show();
		}	
	}
}