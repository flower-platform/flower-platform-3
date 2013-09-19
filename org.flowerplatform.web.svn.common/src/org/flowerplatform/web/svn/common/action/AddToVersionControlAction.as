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
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	
	public class AddToVersionControlAction extends SvnProjectFileAction{
		
		public function AddToVersionControlAction() {
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.addToVersionControl.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/added_ov.gif");
		}
	
		public override function run():void {
			var pathFragmentsForFiles:ArrayCollection = new ArrayCollection;
			for (var i:int=0; i<selection.length; i++) {
				var treeNode:TreeNode = TreeNode(selection.getItemAt(i));
				pathFragmentsForFiles.addItem(treeNode.getPathForNode(true));
			}
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("svnService", "addToVersion", 
					[pathFragmentsForFiles], 
					this));
		}		
	}
}