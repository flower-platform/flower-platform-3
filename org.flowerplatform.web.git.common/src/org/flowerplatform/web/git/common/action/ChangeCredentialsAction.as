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
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ChangeCredentialsAction extends ActionBase {
		
		public function ChangeCredentialsAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.changeCredentials.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl("images/permission.png");
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.changeCredentials.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {				
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE;
			}
			return false;
		}
		
		override public function run():void {
			GitCommonPlugin.getInstance().service.openCredentials(TreeNode(selection.getItemAt(0)));
		}
		
	}
}