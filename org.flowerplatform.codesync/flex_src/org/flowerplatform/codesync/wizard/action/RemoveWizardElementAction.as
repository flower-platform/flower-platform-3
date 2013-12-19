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
	import org.flowerplatform.editor.model.remote.NotationDiagramEditorStatefulClient;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RemoveWizardElementAction extends ActionBase {
		
		private var wizardElementsView:WizardElementsView;
		
		public function RemoveWizardElementAction(wizardElementsView:WizardElementsView) {
			this.wizardElementsView = wizardElementsView;
			label = CodeSyncPlugin.getInstance().getMessage("wizard.remove");
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/common/delete.png");
			preferShowOnActionBar = true;
			orderIndex = 20;
		}
		
		override public function run():void {
			if (CodeSyncPlugin.getInstance().wizardUtils.selectedWizardElement == null) {
				return;
			}
			var statefulClient:NotationDiagramEditorStatefulClient = CodeSyncPlugin.getInstance().wizardUtils.getSelectedEditorStatefulClient();
			if (CodeSyncPlugin.getInstance().wizardUtils.isSelectedEditorStatefulClientValid(statefulClient)) {
				statefulClient.service_removeWizardElement(					
					CodeSyncPlugin.getInstance().wizardUtils.selectedWizardElement.getPathForNode(true),
					wizardElementsView, wizardElementsView.refreshHandler);		
			}		
		}	
		
	}
}