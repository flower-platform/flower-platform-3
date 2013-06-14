package org.flowerplatform.web.common.explorer {
	import mx.collections.ArrayList;
	import mx.events.IndexChangedEvent;
	
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.editor.action.EditorTreeActionProvider;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.events.IndexChangeEvent;
	
	public class ExplorerTreeList extends GenericTreeList implements IPopupContent {
		
		protected var _popupHost:IPopupHost;
		
		protected var editorTreeActionProvider:EditorTreeActionProvider = new EditorTreeActionProvider();
		
		public function ExplorerTreeList() {
			super();
			addEventListener(IndexChangeEvent.CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:IndexChangeEvent):void {
			popupHost.refreshActions(this);
		}
		
		public function getActions():Vector.<IAction>
		{
			var result:Vector.<IAction> = new Vector.<IAction>();
			var action:ActionBase;
			
			action = new ActionBase();
			action.label = "Test 1";
			result.push(action);
			
			return result.concat(editorTreeActionProvider.getActions(new ArrayList([].concat(selectedItems))));
		}
		
		public function get popupHost():IPopupHost {
			return _popupHost;
		}

		public function set popupHost(value:IPopupHost):void {
			_popupHost = value;
		}

		
	}
}