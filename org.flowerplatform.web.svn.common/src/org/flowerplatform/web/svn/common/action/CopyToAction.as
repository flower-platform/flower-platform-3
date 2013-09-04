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

package org.flowerplatform.web.svn.common.action
{
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.BranchTagView;

	/**
	 * @author Gabriela Murgoci
	 */
	public class CopyToAction extends BranchTagAction
	{
		public function CopyToAction() {
			super();
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.copyTo.label");
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/project.gif");
		}
		
		public override function run():void {			
			var view:BranchTagView = new BranchTagView();
			view.node=TreeNode(selection.getItemAt(0));
			view.selection = selection;
			view.viewLabel = "svn.action.copyTo.view.label";
			view.viewIcon = "images/svn_persp.gif";
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setWidth(400)
				.setHeight(450)
				.setPopupContent(view)
				.show();			
		}
	}
}