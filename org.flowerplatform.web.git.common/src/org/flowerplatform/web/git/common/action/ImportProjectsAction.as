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