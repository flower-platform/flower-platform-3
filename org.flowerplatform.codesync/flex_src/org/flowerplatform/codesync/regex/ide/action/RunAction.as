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
	
	import com.crispico.flower.util.layout.Workbench;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.ide.RegexDataEvent;
	import org.flowerplatform.codesync.regex.ide.RegexMatchesView;
	import org.flowerplatform.codesync.regex.ide.remote.RegexMatchDto;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.editor.text.CodeMirrorEditorFrontend;
	import org.flowerplatform.emf_model.regex.ParserRegex;
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
			label = "Run";
			icon = CodeSyncPlugin.getInstance().getResourceUrl("images/common/refresh.png");
			preferShowOnActionBar = true;
		}
				
		override public function run():void	{
			var editors:ArrayCollection = Workbench(FlexUtilGlobals.getInstance().workbench).getAllVisibleViewLayoutData(true);
			if (editors.length == 0) {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setTitle(CommonPlugin.getInstance().getMessage("error"))
					.setText("A CodeMirror editor must be open and selected!")
					.setHeight(200)
					.setWidth(400)
					.showMessageBox();
				return;
			}
			
			var editorFrontend:Object;
			for each (var editor:ViewLayoutData in editors) {
				editorFrontend = Workbench(FlexUtilGlobals.getInstance().workbench).layoutDataToComponent[editor];
				if (editorFrontend is CodeMirrorEditorFrontend) {
					break;
				}
			}
			
			regexMatchesView.editorFrontend = CodeMirrorEditorFrontend(editorFrontend);
			var editorPath:String = regexMatchesView.editorFrontend.editorStatefulClient.editableResourcePath;
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("regexService", "run", [editorPath], this, runCallbackHandler));
		}	
		
		private function runCallbackHandler(result:Object):void {
			if (result != null) {		
				var matches:ArrayCollection = ArrayCollection(result[1]);
				
				var regexes:ArrayCollection = ArrayCollection(result[0]);
				for each (var action:ParserRegex in regexes) {
					for each (var match:RegexMatchDto in matches) {
						if (match.parserRegex.name == action.name) {
							if (action.matches == null) {
								action.matches = new ArrayCollection();
							}
							action.matches.addItem(match);
						}
					}
				}
				
				var event:RegexDataEvent = new RegexDataEvent(RegexDataEvent.REGEX_MATCHES_CHANGED);
				event.newData = matches;
				FlexGlobals.topLevelApplication.dispatchEvent(event);
				
				event = new RegexDataEvent(RegexDataEvent.REGEX_ACTIONS_CHANGED);
				event.newData = regexes;
				FlexGlobals.topLevelApplication.dispatchEvent(event);
			}
		}
	}
}