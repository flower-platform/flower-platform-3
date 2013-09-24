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

package  org.flowerplatform.web.svn.common.action {
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.BranchTagView;
	
	/**
	 * @author Gabriela Murgoci
	 */
	public class BranchTagAction extends ActionBase  {
		/**
		 * @flowerModelElementId _-0ZzQAMdEeOrJqcAep-lCg
		 */
		public function BranchTagAction() {	
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.branchTag.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/branch.gif");
		}
		
		/**
		 * @flowerModelElementId _GWBDMAMhEeOrJqcAep-lCg
		 */
		public override function get visible():Boolean {
			// check if there are selected resources
			if (selection.length == 0)
				return false;
			var organizationName:String = PathFragment(TreeNode(selection.getItemAt(0)).getPathForNode(false).getItemAt(0)).name;
			for (var i:int = 0; i < selection.length; i++) {
				var currentSelection:Object = selection.getItemAt(i);
				if (!(currentSelection is TreeNode))
					return false;
				var selectedNode:TreeNode = TreeNode(currentSelection); 
				if (organizationName != PathFragment(selectedNode.getPathForNode(false).getItemAt(0)).name) {
					return false;
				}
				if (selectedNode.pathFragment.type != SvnCommonPlugin.NODE_TYPE_FOLDER && selectedNode.pathFragment.type != SvnCommonPlugin.NODE_TYPE_FILE) {
					return false;
				}
				/*if ((selectedNode.customData.isFolder == false)) {
					return false;
				}	*/
			}
			return true;
		}
		
		/**
		 * @flowerModelElementId _TnF_EAMeEeOrJqcAep-lCg
		 */
		public override function run():void {			
			var view:BranchTagView = new BranchTagView();
			view.node = TreeNode(selection.getItemAt(0));
			view.selection = selection;
			view.viewLabel = "svn.action.branchTag.view.label";
			view.viewIcon = "images/svn_persp.gif";
			view.actionType = false;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(400)
				.setHeight(450)
				.setPopupContent(view)
				.show();			
		}
	}
}
