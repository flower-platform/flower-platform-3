
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
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.RenameMoveView;
	
	/**
	 * @author Gabriela Murgoci
	 */
	public class RenameMoveAction extends ActionBase  {
		/**
		 * @flowerModelElementId _-0ZzQAMdEeOrJqcAep-lCg
		 */
		public function RenameMoveAction() {	
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.renameMove.label");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/project.gif");
		}
		
		/**
		 * @flowerModelElementId _GWBDMAMhEeOrJqcAep-lCg
		 */
		public override function get visible():Boolean {				
			var node_type:String;	
			if (selection.length == 0)
				return false;
			node_type = TreeNode(selection.getItemAt(0)).pathFragment.type;	
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return (node_type == SvnCommonPlugin.NODE_TYPE_REPOSITORY || node_type == SvnCommonPlugin.NODE_TYPE_FILE);
			}
			return false;
		}
		
		/**
		 * @flowerModelElementId _TnF_EAMeEeOrJqcAep-lCg
		 */
		public override function run():void {			
			var view:RenameMoveView = new RenameMoveView();
			view.node=TreeNode(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(400)
				.setHeight(450)
				.setPopupContent(view)
				.show();			
		}
	}
	
}