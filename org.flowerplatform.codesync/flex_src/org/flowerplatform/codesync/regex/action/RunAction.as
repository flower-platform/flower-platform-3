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
package org.flowerplatform.codesync.regex.action  {
	
	import com.crispico.flower.util.layout.Workbench;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.RegexEvent;
	import org.flowerplatform.codesync.regex.ui.RegexMatchesView;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.text.CodeMirrorEditorFrontend;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.layout.ViewLayoutData;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class RunAction extends ActionBase {
		
		private var regexMatchesView:RegexMatchesView;	
		
		public function RunAction(regexMatchesView:RegexMatchesView) {
			this.regexMatchesView = regexMatchesView;
			label = CodeSyncPlugin.getInstance().getMessage("regex.run");
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/common/refresh.png");
			preferShowOnActionBar = true;
		}
				
		override public function run():void	{
			if (!CodeSyncPlugin.getInstance().regexUtils.isSelectedConfigValid()) {
				return;
			}
			// search for a open & selected codeMirrorEditor
			var editorFrontend:Object = null;
			var editors:ArrayCollection = Workbench(FlexUtilGlobals.getInstance().workbench).getAllVisibleViewLayoutData(true);			
			for each (var editor:ViewLayoutData in editors) {
				editorFrontend = Workbench(FlexUtilGlobals.getInstance().workbench).layoutDataToComponent[editor];
				if (editorFrontend is CodeMirrorEditorFrontend) {
					break;
				}
			}			
			
			// keep editor in regex matches view -> will be used to color text when selecting matches from list
			regexMatchesView.editorFrontend = CodeMirrorEditorFrontend(editorFrontend);
			
			if (editorFrontend == null) { // no CodeMirror editor open or selected -> don't run this action
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setTitle(CommonPlugin.getInstance().getMessage("error"))
					.setText(CodeSyncPlugin.getInstance().getMessage("regex.noCodeMirrorEditorOpen.info"))
					.setHeight(200)
					.setWidth(400)
					.showMessageBox();
				// clear regex matches list
				if (regexMatchesView.list.dataProvider != null) {
					regexMatchesView.list.dataProvider.removeAll();	
				}
				return;
			}		
			
			// run regex configuration for selected editor
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand(
					"regexService", 
					"run", 
					[editorFrontend.editorStatefulClient.editableResourcePath, CodeSyncPlugin.getInstance().regexUtils.selectedConfig], 
					this, runCallbackHandler));
		}	
		
		private function runCallbackHandler(result:Object):void {
			if (result != null) {
				// fill regex matches list with new data
				regexMatchesView.list.dataProvider = ArrayCollection(result);
				if (regexMatchesView.list.dataProvider != null && regexMatchesView.list.dataProvider.length > 0) {
					regexMatchesView.list.selectedIndex = 0;
				}
				
				// notify parsers regex view -> will fill each parser regex with children
				var event:RegexEvent = new RegexEvent(RegexEvent.FILL_PARSERS);
				event.newData = ArrayCollection(result);
				FlexGlobals.topLevelApplication.dispatchEvent(event);				
			}
		}
	}
}