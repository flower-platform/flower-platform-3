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
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	import org.flowerplatform.flexutil.tree.HierarchicalModelWrapper;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	import spark.events.IndexChangeEvent;
	
	/**
	 * @author Cristian Spiescu
	 * 
	 */	
	public class ExplorerTreeList extends GenericTreeList implements IPopupContent {
		
		protected var _popupHost:IPopupHost;
		
		protected var editorTreeActionProvider:EditorTreeActionProvider = new EditorTreeActionProvider();
		
		public function ExplorerTreeList() {
			super();
			allowMultipleSelection = true;
			addEventListener(IndexChangeEvent.CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:IndexChangeEvent):void {
			if (popupHost) {
				popupHost.refreshActions(this);
			}
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
		
		public function get popupHost():IPopupHost {
			return _popupHost;
		}

		public function set popupHost(value:IPopupHost):void {
			_popupHost = value;
		}

		override protected function focusInHandler(event:FocusEvent):void {
			super.focusInHandler(event);
			popupHost.activePopupContent = this;
		}
	}
}