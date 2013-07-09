package org.flowerplatform.web.common.projects {
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.common.projects.properties.PropertiesView;
	
	public class ProjectPropertiesAction extends ActionBase {
		
		public function ProjectPropertiesAction() {
			label = "Properties";
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
				.setWidth(450)
				.setHeight(400)
				.setPopupContent(view)
				.show();
		}
		
	}
}