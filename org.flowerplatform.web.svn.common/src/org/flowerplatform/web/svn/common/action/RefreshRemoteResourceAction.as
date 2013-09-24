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

package org.flowerplatform.web.svn.common.action {
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.GenericTreeStatefulClient;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.explorer.ExplorerTreeList;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	
	/**
	 * Action displayed in SVN Repositories View.
	 * Refreshes the selected object (belonging to SvnRepositories subtree).
	 * 
	 * @author Victor Badila
	 *	 
	 */
	public class RefreshRemoteResourceAction extends ActionBase {
						
		public function RefreshRemoteResourceAction() {
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.refreshAction.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/refresh.gif");			
		}
		
		public override function get visible():Boolean {			
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				var nodeType:String = String(TreeNode(selection.getItemAt(0)).pathFragment.type);
				return ((nodeType == SvnCommonPlugin.NODE_TYPE_REPOSITORY) ||
						(nodeType == SvnCommonPlugin.NODE_TYPE_FOLDER) ||
						(nodeType == SvnCommonPlugin.NODE_TYPE_FILE));
			}
			return false;
		}
		
		override public function run():void {			
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("svnService", "refresh", [selection.getItemAt(0).getPathForNode(true)], this));			
		}
	}
	
}
