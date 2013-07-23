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
package org.flowerplatform.web.git.common.action {
	
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.common.remote.dto.RemoteConfig;
	import org.flowerplatform.web.git.common.ui.ConfigRemoteView;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class ConfigRemoteAction extends ActionBase {
		
		private var node:TreeNode;
		
		public function ConfigRemoteAction() {
			label = GitCommonPlugin.getInstance().getMessage("git.action.configRemote.label");
			icon = GitCommonPlugin.getInstance().getResourceUrl('images/full/obj16/remote_entry_tbl.gif');
			orderIndex = int(GitCommonPlugin.getInstance().getMessage("git.action.configRemote.sortIndex"));
		}
		
		override public function get visible():Boolean {
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {			
				return TreeNode(selection.getItemAt(0)).pathFragment.type == GitCommonPlugin.NODE_TYPE_REMOTE;
			}
			return false;
		}
		
		private function getRemoteConfigDataCallbackHandler(result:RemoteConfig):void {
			if (result != null) {
				var configWindow:ConfigRemoteView = new ConfigRemoteView();
				configWindow.node = node.parent;
				configWindow.remoteConfig = result;
				FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
					.setPopupContent(configWindow)
					.setWidth(400)
					.setHeight(350)					
					.show();		
			}
		}
		
		override public function run():void {
			node = TreeNode(selection.getItemAt(0));
			GitCommonPlugin.getInstance().service.getRemoteConfigData(node, this, getRemoteConfigDataCallbackHandler);			
		}	
	}
}