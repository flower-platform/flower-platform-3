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
package org.flowerplatform.codesync.regex.ide {
	
	import com.crispico.flower.util.layout.event.ViewAddedEvent;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.ClassFactory;
	import mx.core.FlexGlobals;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.codesync.regex.ide.action.RunAction;
	import org.flowerplatform.codesync.regex.ide.remote.RegexMatchDto;
	import org.flowerplatform.codesync.regex.ide.remote.RegexSelectedItem;
	import org.flowerplatform.codesync.regex.ide.ui.RegexItemRenderer;
	import org.flowerplatform.editor.text.CodeMirrorEditorFrontend;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.layout.event.ViewRemovedEvent;
	import org.flowerplatform.flexutil.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	import spark.components.List;
	import spark.events.IndexChangeEvent;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RegexMatchesView extends List implements IViewContent, ISelectionProvider, ISelectionForServerProvider {
		
		protected var _viewHost:IViewHost;
		
		public var editorFrontend:CodeMirrorEditorFrontend;
		
		public function RegexMatchesView() {
			super();
			allowMultipleSelection = true;
			itemRenderer = new ClassFactory(RegexItemRenderer);
			labelFunction = function (object:Object):String {
				return RegexMatchDto(object).value + ") " + RegexMatchDto(object).parserRegex.name;
			}
			addEventListener(FlexEvent.VALUE_COMMIT, changeHandler);
			addEventListener(IndexChangeEvent.CHANGE, indexChangeHandler);					
		}
		
		public function getActions(selection:IList):Vector.<IAction> {			
			var result:Vector.<IAction> = new Vector.<IAction>();
			result.push(new RunAction(this));
			return result;
		}
				
		public function set viewHost(viewHost:IViewHost):void {
			if (_viewHost != null) {
				DisplayObject(_viewHost).removeEventListener(ViewAddedEvent.VIEW_ADDED, viewAddedHandler);
				DisplayObject(_viewHost).removeEventListener(ViewRemovedEvent.VIEW_REMOVED, viewRemovedHandler);	
			}
			
			_viewHost = viewHost;
						
			DisplayObject(_viewHost).addEventListener(ViewAddedEvent.VIEW_ADDED, viewAddedHandler);
			DisplayObject(_viewHost).addEventListener(ViewRemovedEvent.VIEW_REMOVED, viewRemovedHandler);	
		}
		
		public function getSelection():IList {			
			var array:ArrayList = new ArrayList();
			for (var i:int = 0; i < selectedItems.length; i++) {
				array.addItem(selectedItems[i]);
			}
			return array;
		}
				
		private function viewAddedHandler(event:ViewAddedEvent):void {
			FlexGlobals.topLevelApplication.addEventListener(RegexDataEvent.REGEX_MATCHES_CHANGED, regexMatchesChangedHandler);
			FlexGlobals.topLevelApplication.addEventListener(RegexDataEvent.REGEX_ACTIONS_SELECTED_CHANGED, regexActionsSelectedChangedHandler);
			
			// show actions in buttonBar
			_viewHost.selectionChanged();
		}
		
		private function viewRemovedHandler(event:ViewRemovedEvent):void {			
			FlexGlobals.topLevelApplication.removeEventListener(RegexDataEvent.REGEX_MATCHES_CHANGED, regexMatchesChangedHandler);
			FlexGlobals.topLevelApplication.removeEventListener(RegexDataEvent.REGEX_ACTIONS_SELECTED_CHANGED, regexActionsSelectedChangedHandler);
		}
		
		private function changeHandler(event:Event):void {			
			var array:Array = new Array(selectedItems.length);
			for (var i:int = 0; i < selectedItems.length; i++) {
				array[i] = selectedItems[i];
			}
			editorFrontend.colorText(array);
			
			event.stopImmediatePropagation();
		}
		
		private function indexChangeHandler(e:IndexChangeEvent):void {		
			var array:Array = new Array(selectedItems.length);
			for (var i:int = 0; i < selectedItems.length; i++) {
				array[i] = selectedItems[i];
			}
			var event:RegexDataEvent = new RegexDataEvent(RegexDataEvent.REGEX_MATCHES_SELECTED_CHANGED);
			event.newSelectedMatches = array;
			FlexGlobals.topLevelApplication.dispatchEvent(event);
			
			FlexUtilGlobals.getInstance().selectionManager.selectionChanged(_viewHost, this);
		}
		
		private function regexMatchesChangedHandler(event:RegexDataEvent):void {			
			dataProvider = event.newData;
		}
		
		private function regexActionsSelectedChangedHandler(event:RegexDataEvent):void {			
			var vector:Vector.<int> = new Vector.<int>();			
			for each (var match:RegexMatchDto in event.newSelectedMatches) {
				var index:int = dataProvider.getItemIndex(match);
				if (index != -1) {
					vector.push(index);
				}
			}
			selectedIndices = vector;
			if (vector.length > 0) {
				ensureIndexIsVisible(vector[0]);
			}			
			
			event.stopImmediatePropagation();
		}
		
		public function convertSelectionToSelectionForServer(selection:IList):IList {
			if (selection == null) {
				return selection;
			}			
			var selectedItems:ArrayCollection = new ArrayCollection();
			for each (var match:RegexMatchDto in selection.toArray()) {
				var selectedItem:RegexSelectedItem = new RegexSelectedItem();
				selectedItem.match = match;
				selectedItem.itemType = "regex_match";
				selectedItems.addItem(selectedItem);
			}
			
			return selectedItems;
		}
				
	}
}
