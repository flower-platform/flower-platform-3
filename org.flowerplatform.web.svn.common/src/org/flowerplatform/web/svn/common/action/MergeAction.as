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
	import mx.collections.IList;
	
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
	public class MergeAction extends SvnProjectFileAction implements IDialogResultHandler{
		
		public var resourcePath:String;
		
		public var resourceUrl:String;
		
		public var remoteResourceUrl:String;
		
		public var node:TreeNode;
		
		public var selectionPaths:ArrayList = new ArrayList;
		
		public function MergeAction() {
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.mergeAction.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/merge.gif");	
		}
		
		public function resultClallbackFunction(result:ArrayCollection):void{
			
			remoteResourceUrl = result.getItemAt(0) as String;
			resourceUrl = result.getItemAt(1) as String;
			resourcePath = resourceUrl;
			
			var view:MergeView = new MergeView();
			view.setResultHandler(this);
			
			view.node = node;
			view.resourcePath = resourcePath;
			view.resourceUrl = resourceUrl;
			view.remoteResourceUrl = remoteResourceUrl;
			view.selection.addAll(selectionPaths);
			
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler().
				setPopupContent(view).
				setWidth(600).
				setHeight(600).
				show();
		}
		
		public function handleDialogResult(result:Object):void {
			
		}
		
		override public function run():void {
			for(var i:int=0; i<selection.length; i++) {
				var path:ArrayCollection = ArrayCollection(TreeNode(selection.getItemAt(i)).getPathForNode(true));				
				selectionPaths.addItem(path);
			}
			node = selection.getItemAt(0) as TreeNode;
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("svnService", 
					"getMergeSpecs", [selectionPaths, node.getPathForNode(true)] , this, resultClallbackFunction));
		}
	}
}