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
	
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.common.ui.CreateSvnRepositoryView;
	
	/**
	 * @author Victor Badila
	 *
	 * @flowerModelElementId _bq0SIAQUEeOrJqcAep-lCg
	 */
	public class CreateSvnRepositoryAction extends ActionBase  {
		/**
		 * @flowerModelElementId _F_Su8AQWEeOrJqcAep-lCg
		 */
		public function CreateSvnRepositoryAction() {
			
			label = "Add Svn Repository";
			icon = WebCommonPlugin.getInstance().getResourceUrl("images/project.gif");
			// TODO momentan foloseste project.gif
			
		}
		/**
		 * @flowerModelElementId _L-KhIAQWEeOrJqcAep-lCg
		 */
		public override function get visible():Boolean {	
			
			if (selection.length == 1 && selection.getItemAt(0) is TreeNode) {
				return TreeNode(selection.getItemAt(0)).pathFragment.type == SvnCommonPlugin.NODE_TYPE_SVN_REPOSITORIES;
			}
			return false;
		}
		/**
		 * @flowerModelElementId _OPLhgAQWEeOrJqcAep-lCg
		 */
		public override function run():void {
			
			var view:CreateSvnRepositoryView = new CreateSvnRepositoryView();
			view.node=TreeNode(selection.getItemAt(0));
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				//.setHeight(350)
				//.setWidth(350)
				.setPopupContent(view)
				.show();			
		}
	}
}