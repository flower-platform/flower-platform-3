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
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.MergeView;
	
	[RemoteClass]
	public class MergeAction extends ActionBase implements IDialogResultHandler{
		
		public function MergeAction() {
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.mergeAction.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/merge.gif");
		}
		
		public function handleDialogResult(result:Object):void {
			
		}
		
		override public function get visible():Boolean {
			if (selection.length >= 1 && selection.getItemAt(0) is TreeNode){
				for(var i:int = 0; i < selection.length; i++){
					var selectedNode:TreeNode = selection.getItemAt(i) as TreeNode;
					if (!((selectedNode.pathFragment.type == SvnCommonPlugin.NODE_TYPE_REPOSITORY) ||
						(selectedNode.pathFragment.type == SvnCommonPlugin.NODE_TYPE_FILE)))
						return false;
				}
				return true;
			}
			return true;
			
		}
		
		override public function run():void {
			
			var view:MergeView = new MergeView();
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler().
				setPopupContent(view).
				setWidth(600).
				setHeight(600).
				show();
		}
	}
}