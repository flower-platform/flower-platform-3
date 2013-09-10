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
	import mx.controls.Alert;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;	

	/**
	 * @author Victor Badila
	 */
	public class UpdateToHeadAction extends ActionBase {
		
		public function UpdateToHeadAction() {
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.updateToHead.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/update.gif");
		}
		
		public override function get visible():Boolean {
			for (var i:int=0; i<selection.length; i++) {
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
		
		public override function run():void {			
			var selectionPaths:ArrayList = new ArrayList;
			for(var i:int=0; i<selection.length; i++) {
				var path:ArrayCollection = ArrayCollection(TreeNode(selection.getItemAt(i)).getPathForNode(true));				
				selectionPaths.addItem(path);
			}			
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("svnService", "updateToHEAD", 
					[selectionPaths], this, getProjectsCallbackHandler));
		}	
		
		public function getProjectsCallbackHandler(response:Boolean):void {
			if(!response) {
				Alert.show(SvnCommonPlugin.getInstance().getMessage("svn.action.updateToHead.operationNotSuccessful"));
			}
		}
	}
}