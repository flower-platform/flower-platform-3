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
package org.flowerplatform.web.common.explorer {
	import flash.events.FocusEvent;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.IndexChangedEvent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.communication.tree.remote.TreeNode;
	import org.flowerplatform.editor.EditorPlugin;
	import org.flowerplatform.editor.action.EditorTreeActionProvider;
	import org.flowerplatform.editor.action.OpenAction;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.properties.PropertiesPlugin;
	import org.flowerplatform.web.common.WebCommonPlugin;
	import org.flowerplatform.web.common.explorer.properties.FileSelectedItem;
	
	import spark.events.IndexChangeEvent;
	
	public class ExplorerTreeList extends GenericTreeList implements IViewContent, ISelectionProvider, ISelectionForServerProvider {
		
		protected var _viewHost:IViewHost;
		
		protected var editorTreeActionProvider:EditorTreeActionProvider = new EditorTreeActionProvider();
		
		public function ExplorerTreeList() {
			super();
			doubleClickEnabled = true;
			addEventListener(IndexChangeEvent.CHANGE, selectionChangedHandler);
			
			// open file at double click or ENTER
			addEventListener(MouseEvent.DOUBLE_CLICK, doubleClickHandler);
			addEventListener(KeyboardEvent.KEY_UP, keyUpHandler1);
		}
		
		protected function selectionChangedHandler(e:IndexChangeEvent):void {
			FlexUtilGlobals.getInstance().selectionManager.selectionChanged(viewHost, this);
		}
				
		public function getActions(selection:IList):Vector.<IAction> {
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			for each (var ap:IActionProvider in WebCommonPlugin.getInstance().explorerTreeActionProviders) {
				var actions:Vector.<IAction> = ap.getActions(selection);
				if (actions != null) {
					for each (var action:IAction in actions) {
						result.push(action);
					}
				}
			}
			return result;
		}
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}

		public function set viewHost(value:IViewHost):void {
			_viewHost = value;
		}

		
		/**
		 * @author Cristina Constantinescu
		 */ 
		private function doubleClickHandler(event:MouseEvent):void {
			var selection:IList = getSelection();	
			for (var i:int = 0; i < selection.length; i++) {
				var node:TreeNode = TreeNode(selection.getItemAt(i));
				if (node.customData == null || node.customData[EditorPlugin.TREE_NODE_KEY_CONTENT_TYPE] == null) {
					// found at least one node not openable
					return;
				}
			}
			// run action
			var action:OpenAction = new OpenAction(null, false);
			action.selection = selection;
			action.run();
		}
		
		/**
		 * @author Cristina Constantinescu
		 */ 
		private function keyUpHandler1(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.ENTER) {
				doubleClickHandler(null);
			}
		}
		
		public function convertSelectionToSelectionForServer(selection:IList):IList {
			if (selection == null) return selection;
			var itemsOfSelection:Array = selection.toArray();
			var selectedItems = new ArrayCollection();
			for each (var itemOfSelection:TreeNode in itemsOfSelection) {
				selectedItems.addItem(new FileSelectedItem(itemOfSelection.getPathForNode(true)));
			}
			var myObject:Object;
			CommunicationPlugin.getInstance().bridge.sendObject(
				new InvokeServiceMethodServerCommand("propertiesProviderService",
					"getProperties",[selectedItems],
					myObject,
					function(object:Object):void {
						PropertiesPlugin.getInstance().propertyList.dataProvider = object as IList;
						PropertiesPlugin.getInstance().propertyList.selectedItemsForProperties = selectedItems;
					}
				));				
			return selection;
		}
	}
}