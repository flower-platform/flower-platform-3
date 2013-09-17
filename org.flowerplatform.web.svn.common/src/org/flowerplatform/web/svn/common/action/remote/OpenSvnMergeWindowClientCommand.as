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

package org.flowerplatform.web.svn.common.action.remote {
	
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
	import org.flowerplatform.communication.command.AbstractClientCommand;
	
	[RemoteClass(alias="org.flowerplatform.web.svn.common.action.remote.OpenMergeWindowClientCommand")]
	public class OpenSvnMergeWindowClientCommand extends AbstractClientCommand implements IDialogResultHandler{
		
		public var resourcePath:String;
		
		public var resourceUrl:String;
		
		public var remoteResourceUrl:String;
		
		public function handleDialogResult(result:Object):void {
			
		}
		
		override public function execute():void {
			
			var view:MergeView = new MergeView();
			view.resourcePath = resourcePath;
			view.resourceUrl = resourceUrl;
			view.remoteResourceUrl = remoteResourceUrl;
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler().
				setPopupContent(view).
				setWidth(600).
				setHeight(600).
				show();
		}
	}
}