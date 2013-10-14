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
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.IndexChangedEvent;
	
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.editor.action.EditorTreeActionProvider;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.ActionBase;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.action.IActionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	import spark.events.IndexChangeEvent;
	
	public class ExplorerTreeList extends GenericTreeList implements IViewContent, ISelectionProvider {
		
		protected var _viewHost:IViewHost;
		
		protected var editorTreeActionProvider:EditorTreeActionProvider = new EditorTreeActionProvider();
		
		public function ExplorerTreeList() {
			super();
			addEventListener(IndexChangeEvent.CHANGE, selectionChangedHandler);
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

	}
}