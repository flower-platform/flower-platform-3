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
package org.flowerplatform.codesync.wizard.action {
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.wizard.ui.WizardElementsView;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class AddWizardElementAction extends ActionBase {
				
		private var wizardElementsView:WizardElementsView;
		
		public function AddWizardElementAction(wizardElementsView:WizardElementsView, addWizardAttribute:Boolean = false) {	
			this.wizardElementsView = wizardElementsView;
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/common/add.png");
			if (addWizardAttribute) {
				label = CodeSyncPlugin.getInstance().getMessage("wizard.add.attribute");				
			} else {
				label = CodeSyncPlugin.getInstance().getMessage("wizard.add");
				preferShowOnActionBar = true;
			}
			orderIndex = 10;
		}
		
		override public function get visible():Boolean {
			if (!preferShowOnActionBar) {
				if (selection.length == 0) {
					return false;
				}
				return TreeNode(HierarchicalModelWrapper(selection.getItemAt(0)).treeNode).parent == null;
			}
			return true;
		}
			
		override public function run():void {			
			var statefulClient:NotationDiagramEditorStatefulClient = CodeSyncPlugin.getInstance().wizardUtils.getSelectedEditorStatefulClient();
			if (CodeSyncPlugin.getInstance().wizardUtils.isSelectedEditorStatefulClientValid(statefulClient)) {
				statefulClient.service_addWizardElement(
						!preferShowOnActionBar,
						CodeSyncPlugin.getInstance().wizardUtils.selectedWizardElement != null 
						? CodeSyncPlugin.getInstance().wizardUtils.selectedWizardElement.getPathForNode(true) 
						: null,
						wizardElementsView, wizardElementsView.refreshHandler);		
			}		
		}		
		
	}
}