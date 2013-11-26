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
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.codesync.regex.ide.action.AddRegexConfigAction;
	import org.flowerplatform.codesync.regex.ide.action.RemoveRegexConfigAction;
	import org.flowerplatform.codesync.regex.ide.remote.RegexSelectedItem;
	import org.flowerplatform.codesync.regex.ide.ui.RegexItemRenderer;
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
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
	public class RegexConfigsView extends List implements IViewContent, ISelectionProvider, ISelectionForServerProvider {
		
		protected var _viewHost:IViewHost;
		
		public function RegexConfigsView() {
			super();
			itemRenderer = new ClassFactory(RegexItemRenderer);
			ClassFactory(itemRenderer).properties = {iconFunction: getIcon};
			addEventListener(FlexEvent.VALUE_COMMIT, changeHandler);
		}
		
		private function getIcon(item:Object):Object {
			return CodeSyncPlugin.getInstance().getResourceUrl("images/regex/bullet_wrench.png");
		}
		
		public function getActions(selection:IList):Vector.<IAction> {			
			var result:Vector.<IAction> = new Vector.<IAction>();	
			result.push(new AddRegexConfigAction());
			result.push(new RemoveRegexConfigAction());
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
			// show actions in buttonBar
			_viewHost.selectionChanged();
			
			FlexGlobals.topLevelApplication.addEventListener(RegexDataEvent.CONFIGS_REQUEST_REFRESH, refreshHandler);
			
			refreshHandler();
		}
		
		private function viewRemovedHandler(event:ViewRemovedEvent):void {
			FlexGlobals.topLevelApplication.removeEventListener(RegexDataEvent.CONFIGS_REQUEST_REFRESH, refreshHandler);
		}
					
		public function convertSelectionToSelectionForServer(selection:IList):IList {
			if (selection == null) {
				return selection;
			}			
			var selectedItems:ArrayCollection = new ArrayCollection();
			for each (var config:String in selection.toArray()) {
				var selectedItem:RegexSelectedItem = new RegexSelectedItem();
				selectedItem.config = config;
				selectedItem.itemType = "regex_config";
				selectedItems.addItem(selectedItem);
			}
			
			return selectedItems;
		}
		
		private function changeHandler(event:Event):void {	
			FlexUtilGlobals.getInstance().selectionManager.selectionChanged(_viewHost, this);
			
			CodeSyncPlugin.getInstance().regexUtils.selectedConfig = selectedItem;
			
			event.stopImmediatePropagation();
		}
		
		public function getRegexConfigsCallbackHandler(result:ArrayCollection):void {
			if (result != null) {
				var oldSelectedItem:String = selectedItem;
				
				dataProvider = ArrayCollection(result);
				
				if (dataProvider.getItemIndex(oldSelectedItem) != -1) {
					selectedItem = oldSelectedItem;
				}
			}
		}
		
		public function refreshHandler(event:RegexDataEvent = null):void {
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("regexService", "getConfigs", null, this, getRegexConfigsCallbackHandler));
		}
						
	}
}