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
	
	import mx.collections.*;
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.controls.Alert;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.remote.dto.GetModifiedFilesDto;
	import org.flowerplatform.web.svn.common.ui.RevertView;
	
	/**
	 * @author Victor Badila
	 */	
	public class RevertAction extends SvnProjectFileAction{
		
		public function RevertAction() {
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.revert.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/revert.gif");			
		}		
		
		public override function run():void {
			var selectionPaths:ArrayList = new ArrayList;
			for(var i:int=0; i<selection.length; i++) {
				var path:ArrayCollection = ArrayCollection(TreeNode(selection.getItemAt(i)).getPathForNode(true));				
				selectionPaths.addItem(path);
			}				
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("svnService", "getDifferences", [selectionPaths], this, runContinued));			
		}
		
		public function runContinued(result:GetModifiedFilesDto):void {
			if (result.files.length==0) {
				Alert.show(SvnCommonPlugin.getInstance().getMessage("svn.action.revert.alertNoItemsInSelectionSuitableForAction"));
				return;
			}
			var view:RevertView = new RevertView();
			view.modifiedResources = result;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(view)
				.show();
		}
		
		public override function get visible():Boolean {
			return getVisibleForAddToVersionOrAddToIgnore();
		}
	}
}