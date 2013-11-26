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
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.ide.ui.AddMacroRegexView;
	import org.flowerplatform.codesync.regex.ide.ui.AddParserRegexView;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.dialog.IDialogResultHandler;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class AddParserRegexAction extends ActionBase	implements IDialogResultHandler {
		
		public function AddParserRegexAction() {
			label = CodeSyncPlugin.getInstance().getMessage("regex.add");
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/common/add.png");
			preferShowOnActionBar = true;
		}
				
		override public function run():void {
			if (CodeSyncPlugin.getInstance().regexUtils.selectedConfig == null) {
				return;
			}
			var view:AddParserRegexView = new AddParserRegexView();
			view.setResultHandler(this);
			FlexUtilGlobals.getInstance().popupHandlerFactory.createPopupHandler()
				.setViewContent(view)
				.setWidth(450)
				.setHeight(200)
				.show();
		}	
		
		public function handleDialogResult(result:Object):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("regexService", "addParserRegex", 
					[CodeSyncPlugin.getInstance().regexUtils.selectedConfig, result[0], result[1], result[2]]));
		}
		
	}
}