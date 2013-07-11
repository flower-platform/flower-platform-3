package org.flowerplatform.web.common.projects {
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.common.projects.properties.PropertiesView;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ProjectPropertiesAction extends ActionBase {
		
		public function ProjectPropertiesAction() {
			label = "Properties";
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/project.gif");
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return TreeNode(selection.getItemAt(0)).pathFragment.type == WebCommonPlugin.NODE_TYPE_PROJECT;
			}
			return false;
		}

		override public function run():void {
			var view:PropertiesView = new PropertiesView();
			view.node = TreeNode(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(500)
				.setHeight(450)
				.setPopupContent(view)
				.show();
		}
		
	}
}