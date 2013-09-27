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
	import mx.controls.Tree;
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.BranchTagView;
	import org.flowerplatform.web.svn.common.ui.ShareProjectView;
	
	/**
	 * @author Gabriela Murgoci
	 */
	public class ShareProjectAction extends ActionBase {
		/**
		 * @flowerModelElementId _-0ZzQAMdEeOrJqcAep-lCg
		 */
		public function ShareProjectAction() {	
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.shareProject.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/share.png");
		}
		
		/**
		 * @flowerModelElementId _GWBDMAMhEeOrJqcAep-lCg
		 */
		public override function get visible():Boolean {				
			for (var i:int = 0; i < selection.length; i++) {
				var currentSelection:Object = selection.getItemAt(i);
				if (!(currentSelection is TreeNode)) {
					return false;
				}
				var node:TreeNode = TreeNode(currentSelection);
				if (node.pathFragment.type != WebCommonPlugin.NODE_TYPE_PROJECT)
					return false;
				if (node.pathFragment.type == SvnCommonPlugin.NODE_TYPE_FOLDER || node.pathFragment.type == SvnCommonPlugin.NODE_TYPE_FILE)
					return false;
				if (currentSelection.customData.svnFileType == true)
					return false;
			}
			return true;
		}
		
		/**
		 * @flowerModelElementId _TnF_EAMeEeOrJqcAep-lCg
		 */
		public override function run():void {			
			var view:ShareProjectView = new ShareProjectView();
			view.node = TreeNode(selection.getItemAt(0));
			view.selection = selection;
			view.viewLabel = "svn.ui.shareProject.label";
			view.viewIcon = "images/svn_persp.gif";
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(400)
				.setHeight(450)
				.setPopupContent(view)
				.show();			
		}
	}
	
}