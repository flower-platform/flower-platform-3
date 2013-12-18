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
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.wizard.remote.WizardDependency;
	import org.flowerplatform.codesync.wizard.ui.WizardDependenciesView;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class DragWizardDependencyTargetsOnDiagramAction extends ActionBase {
		
		private var wizardDependenciesView:WizardDependenciesView;
		
		public function DragWizardDependencyTargetsOnDiagramAction(wizardDependenciesView:WizardDependenciesView) {
			this.wizardDependenciesView = wizardDependenciesView;
			label = CodeSyncPlugin.getInstance().getMessage("wizard.drag");		
			preferShowOnActionBar = true;
		}
		
		override public function run():void {			
			var selectedDependencies:ArrayCollection = wizardDependenciesView.getSelectedWizardDependencies();
			if (selectedDependencies.length != 0) {
				var statefulClient:NotationDiagramEditorStatefulClient = CodeSyncPlugin.getInstance().wizardUtils.getSelectedEditorStatefulClient();
				if (CodeSyncPlugin.getInstance().wizardUtils.isSelectedEditorStatefulClientValid(statefulClient)) {
					statefulClient.service_dragOnDiagramWizardDependenciesTargets(
						selectedDependencies, 
						CodeSyncPlugin.getInstance().wizardUtils.selectedWizardElement.getPathForNode(true));
				}				
			}
		}
	}
}