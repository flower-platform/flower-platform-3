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
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.wizard.remote.WizardDependency;
	import org.flowerplatform.codesync.wizard.ui.WizardDependenciesView;
	import org.flowerplatform.codesync.wizard.ui.WizardElementsView;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class DragWizardElementOnDiagramAction extends ActionBase {
		
		private var wizardElementsView:WizardElementsView;
		
		public function DragWizardElementOnDiagramAction(wizardElementsView:WizardElementsView) {
			this.wizardElementsView = wizardElementsView;
			label = CodeSyncPlugin.getInstance().getMessage("wizard.drag");			
			preferShowOnActionBar = true;
			orderIndex = 40;
		}
		
		private function getSelectedWizardElementsToDrag():ArrayCollection {
			var array:ArrayCollection = new ArrayCollection();
			var selectedItems:IList = wizardElementsView.getSelection();
			for (var i:int = 0; i < selectedItems.length; i++) {
				var node:TreeNode = TreeNode(HierarchicalModelWrapper(selectedItems.getItemAt(i)).treeNode);
				array.addItem(node.parent != null ? node.parent.getPathForNode(true) : node.getPathForNode(true));				
			}
			return array;
		}
		
		override public function run():void {	
			var selectedWizardElementsToGenerate:ArrayCollection = getSelectedWizardElementsToDrag();
			if (selectedWizardElementsToGenerate.length != 0) {
				var statefulClient:NotationDiagramEditorStatefulClient = CodeSyncPlugin.getInstance().wizardUtils.getSelectedEditorStatefulClient();
				if (CodeSyncPlugin.getInstance().wizardUtils.isSelectedEditorStatefulClientValid(statefulClient)) {
					statefulClient.service_dragOnDiagramWizardElements(selectedWizardElementsToGenerate);
				}				
			}
		}
	}
}