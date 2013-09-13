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
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.SwitchView;
	
	/**
	 * @author Gabriela Murgoci
	 */
	public class SwitchAction extends ActionBase  {
		/**
		 * @flowerModelElementId _-0ZzQAMdEeOrJqcAep-lCg
		 */
		public function SwitchAction() {	
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.switchTo.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/switch.gif");
		}
		
		/**
		 * @flowerModelElementId _GWBDMAMhEeOrJqcAep-lCg
		 */
		public function isTreeNode():Boolean {
			var i:int;
			for (i = 0; i < selection.length; i++)
				if (!(selection.getItemAt(i) is TreeNode))
					return false;
			return true;
		}
		
		public override function get visible():Boolean {				
			for (var i:int = 0; i < selection.length; i++) {
				var currentSelection:Object = selection.getItemAt(i);
				if (!(currentSelection is TreeNode)) {
					return false;
				}
				if (currentSelection.customData == null ||
					currentSelection.customData.svnFileType == null ||
					currentSelection.customData.svnFileType == false) {
					return false;
				}
			}			
			return true;
		}
		
		/**
		 * @flowerModelElementId _TnF_EAMeEeOrJqcAep-lCg
		 */
		public override function run():void {			
			var view:SwitchView = new SwitchView();
			view.node = TreeNode(selection.getItemAt(0));
			view.selection = selection;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(400)
				.setHeight(450)
				.setPopupContent(view)
				.show();			
		}
	}
	
}