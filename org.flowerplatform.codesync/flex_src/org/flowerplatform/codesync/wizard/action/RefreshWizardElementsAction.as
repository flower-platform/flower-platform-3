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
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RefreshWizardElementsAction extends ActionBase {
		
		private var wizardElementsView:WizardElementsView;
		
		public function RefreshWizardElementsAction(wizardElementsView:WizardElementsView) {
			this.wizardElementsView = wizardElementsView;
			label = CodeSyncPlugin.getInstance().getMessage("wizard.refresh");
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/common/refresh.png");
			preferShowOnActionBar = true;
		}
		
		override public function run():void {
			wizardElementsView.refreshHandler();
		}
		
	}
}