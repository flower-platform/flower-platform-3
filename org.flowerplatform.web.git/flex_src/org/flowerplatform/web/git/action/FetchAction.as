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
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.web.git.GitNodeType;
	import org.flowerplatform.web.git.GitPlugin;
	import org.flowerplatform.web.git.GitService;
	import org.flowerplatform.web.git.ui.FetchWindow;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class FetchAction extends ActionBase {
		
		private var configFetch:Boolean;
		
		public function FetchAction(configFetch:Boolean = false) {
			this.configFetch = configFetch;
			
			icon = GitPlugin.getInstance().getResourceUrl("images/full/obj16/fetch.gif");
			if (configFetch) {
				label = GitPlugin.getInstance().getMessage("git.action.fetch.label");				
				orderIndex = int(GitPlugin.getInstance().getMessage("git.action.fetch.sortIndex"));				
			} else {
				label = GitPlugin.getInstance().getMessage("git.action.fetchFromRemote.label");
				orderIndex = int(GitPlugin.getInstance().getMessage("git.action.fetchFromRemote.sortIndex"));			
			}			
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				if (configFetch) {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REPOSITORY;
				} else {
					return TreeNode(selection.getItemAt(0)).pathFragment.type == GitNodeType.NODE_TYPE_REMOTE;
				}				
			}
			return false;
		}
		
		override public function run():void {
			if (configFetch) {
				var popup:FetchWindow = new FetchWindow();
				popup.node = TreeNode(selection.getItemAt(0));
				popup.showPopup();
			} else {
				GitPlugin.getInstance().service.	fetch(TreeNode(selection.getItemAt(0)), null, -1, false, null, null);
			}
		}
	}
}