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
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
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
			var view:RevertView = new RevertView();
			view.selection = ArrayList(selection);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setPopupContent(view)
				.show();
		}
	}
}