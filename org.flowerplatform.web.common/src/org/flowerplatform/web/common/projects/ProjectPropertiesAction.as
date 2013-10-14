/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.web.common.projects {
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
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
				.setViewContent(view)
				.show();
		}
		
	}
}