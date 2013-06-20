package org.flowerplatform.web.common.explorer {
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
	
	public class ExplorerTreeList extends GenericTreeList implements IPopupContent {
		
		protected var _popupHost:IPopupHost;
		
		protected var editorTreeActionProvider:EditorTreeActionProvider = new EditorTreeActionProvider();
		
		public function ExplorerTreeList() {
			super();
			addEventListener(IndexChangeEvent.CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:IndexChangeEvent):void {
			if (popupHost) {
				popupHost.refreshActions(this);
			}
		}
		
		/**
		 * This method is inspired from <code>get selectedItems()</code>. We convert
		 * the selection like this (and not by converting selectedItems), because
		 * <code>get selectedItems()</code> does an iteration as well; so that would
		 * mean 2 iterations. This way, we iterate only once.
		 */
		public function getSelection():IList {
			var result:Array = new Array();
			
			if (selectedIndices) {
				var count:int = selectedIndices.length;
				
				for (var i:int = 0; i < count; i++)
					result.push(HierarchicalModelWrapper(dataProvider.getItemAt(selectedIndices[i])).treeNode);  
			}
			
			return new ArrayList(result);
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

		
	}
}