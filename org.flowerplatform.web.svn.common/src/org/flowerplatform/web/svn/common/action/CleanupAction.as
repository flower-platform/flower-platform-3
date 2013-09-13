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
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.BranchTagView;
	
	/**
	 * @author Gabriela Murgoci
	 */
	public class CleanupAction extends ActionBase {
		/**
		 * @flowerModelElementId _-0ZzQAMdEeOrJqcAep-lCg
		 */
		public function CleanupAction() {	
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.cleanup.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/cleanup.gif");
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
			var array:ArrayCollection = new ArrayCollection();
			for (var i:int=0; i < selection.length; i++) {
				array.addItem(TreeNode(selection.getItemAt(i)).getPathForNode(true));
			}
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("svnService", "cleanUp", [array], this));
		}
	}
	
}