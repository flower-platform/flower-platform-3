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

package org.flowerplatform.web.svn.actions {
	
	import com.crispico.flower.util.layout.Workbench;
	import com.crispico.flower.util.layout.persistence.StackLayoutData;
	
	import mx.core.UIComponent;
	
	import org.flowerplatform.communication.tree.remote.PathFragment;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.web.svn.SvnPlugin;
	import org.flowerplatform.web.svn.common.SvnCommonPlugin;
	import org.flowerplatform.web.svn.history.SvnHistoryView;
	import org.flowerplatform.web.svn.history.SvnHistoryViewProvider;
	
	
	/**
	 * @author Victor Badila
	 */
	public class ShowHistoryAction extends ActionBase{
		
		public function ShowHistoryAction() {
			label = SvnCommonPlugin.getInstance().getMessage("svn.action.showHistory.label");
			icon = SvnCommonPlugin.getInstance().getResourceUrl("images/history_view.gif");
			/*orderIndex = int(GitPlugin.getInstance().getMessage("git.action.showHistory.sortIndex"));*/
			
		}	
		
		public override function get visible():Boolean {
			return SvnPlugin.getInstance().showHistoryIsPossible(selection);			
		}
		
		public override function run():void {
			var workbench:Workbench = Workbench(FlexUtilGlobals.getInstance().workbench);
			var component:UIComponent = workbench.getComponent(SvnHistoryViewProvider.ID);
			
			// verifies if the view is already on workbench
			if (component == null) {
				var parentStack:StackLayoutData = workbench.getStackBelowEditorSash();				
				component = workbench.addNormalView(SvnHistoryViewProvider.ID, false, -1, false, parentStack);
			}
			workbench.callLater(workbench.activeViewList.setActiveView, [component]);
			SvnHistoryView(component).selectedObject = TreeNode(selection.getItemAt(0));			
			if (component != null) {				
				SvnHistoryView(component).refreshBtnHandler();
			}
		}
	}
}

