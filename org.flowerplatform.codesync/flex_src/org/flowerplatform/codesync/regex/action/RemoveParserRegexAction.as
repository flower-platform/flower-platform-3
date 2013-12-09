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
package org.flowerplatform.codesync.regex.action {
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.remote.ParserRegexDto;
	import org.flowerplatform.codesync.regex.ui.ParserRegexView;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class RemoveParserRegexAction extends ActionBase {
		
		private var parserView:ParserRegexView;
		
		public function RemoveParserRegexAction(view:ParserRegexView) {
			this.parserView = view;
			label = CodeSyncPlugin.getInstance().getMessage("regex.remove");
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/common/delete.png");
			preferShowOnActionBar = true;
		}
		
		override public function run():void {
			if (CodeSyncPlugin.getInstance().regexUtils.isSelectedConfigValid()) {
				if (getSelectedParserRegexes().length == 0) {
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setText(CodeSyncPlugin.getInstance().getMessage("regex.parser.noParserRegexSelected.info"))
						.setTitle(label)
						.setWidth(300)
						.setHeight(150)						
						.showMessageBox();
				} else {
					FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
						.setText(CodeSyncPlugin.getInstance().getMessage("regex.parser.remove.message"))
						.setTitle(label)
						.setWidth(300)
						.setHeight(150)
						.addButton(CommonPlugin.getInstance().getMessage("yes"), confirmDeleteHandler)
						.addButton(CommonPlugin.getInstance().getMessage("no"))
						.showMessageBox();
				}
			}
		}
		
		private function confirmDeleteHandler(event:MouseEvent):void {			
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"regexService", 
					"removeParserRegex", 
					[CodeSyncPlugin.getInstance().regexUtils.selectedConfig, getSelectedParserRegexes()]));
		}
		
		private function getSelectedParserRegexes():ArrayCollection {
			var parsers:ArrayCollection = new ArrayCollection();
			for each (var selected:Object in parserView.tree.selectedItems) {
				var item:Object = HierarchicalModelWrapper(selected).treeNode;
				if (item is ParserRegexDto) {
					parsers.addItem(ParserRegexDto(item).name);
				}				
			}
			return parsers;
		}
		
	}
}