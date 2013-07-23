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
package org.flowerplatform.web.git.action {
	
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.ui.ImportProjectWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ImportProjectsAction extends ActionBase {
		
		public function ImportProjectsAction() {
			label = GitPlugin.getInstance().getMessage("git.action.importProjects.label");
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/import_prj.gif");
			orderIndex = int(GitPlugin.getInstance().getMessage("git.action.importProjects.sortIndex"));
		}
		
		override public function get visible():Boolean {
			var node:TreeNode;
			if (selection.length == 1) {
				node = TreeNode(selection.getItemAt(0));
				if (node.pathFragment.type == GitNodeType.NODE_TYPE_WDIR) {
					return true;
				}
			}
			for (var i:int = 0; i < selection.length; i++) {				
				node = TreeNode(selection.getItemAt(0));
				if (node.pathFragment.type != GitNodeType.NODE_TYPE_FOLDER) {
					return false;
				}
			}
			return true;
		}
		
		override public function run():void {
			var wizard:ImportProjectWindow = new ImportProjectWindow();			
			wizard.selectedNodes = ArrayList(selection); 
			wizard.showPopup();		
		}	
	}
}