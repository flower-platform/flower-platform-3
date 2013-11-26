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
package org.flowerplatform.codesync.regex.ide.action {
	
	import flash.events.MouseEvent;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.ide.MacrosRegexView;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.emf_model.regex.MacroRegex;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class RemoveMacroRegexAction extends ActionBase {
		
		private var macroView:MacrosRegexView;
		
		public function RemoveMacroRegexAction(view:MacrosRegexView) {
			this.macroView = view;
			label = CodeSyncPlugin.getInstance().getMessage("regex.remove");
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/common/delete.png");
			preferShowOnActionBar = true;
		}
		
		override public function run():void {
			if (CodeSyncPlugin.getInstance().regexUtils.selectedConfig == null) {
				return;
			}
			FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
				.setText(CodeSyncPlugin.getInstance().getMessage("regex.macro.remove.message"))
				.setTitle(label)
				.setWidth(300)
				.setHeight(150)
				.addButton(CommonPlugin.getInstance().getMessage("yes"), confirmDeleteHandler)
				.addButton(CommonPlugin.getInstance().getMessage("no"))
				.showMessageBox();
		}
		
		private function confirmDeleteHandler(event:MouseEvent):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("regexService", "removeMacro", 
					[CodeSyncPlugin.getInstance().regexUtils.selectedConfig, MacroRegex(macroView.list.selectedItem).name]));
		}
	}
}