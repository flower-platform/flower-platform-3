package org.flowerplatform.web.common.explorer {
	import mx.events.IndexChangedEvent;
	
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.events.IndexChangeEvent;
	
	public class ExplorerTreeList extends GenericTreeList implements IPopupContent {
		
		protected var _popupHost:IPopupHost;
		
		public function ExplorerTreeList() {
			super();
			addEventListener(IndexChangeEvent.CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:IndexChangeEvent):void {
			popupHost.refreshActions(this);
		}
		
		private static var ctr:int;
		
		public function getActions():Vector.<IAction>
		{
			var result:Vector.<IAction> = new Vector.<IAction>();
			var action:ActionBase;
			
			action = new ActionBase();
			action.label = "Test 1";
			result.push(action);
			
			if (ctr % 2 == 0) {
				action = new ActionBase();
				action.label = "Test 2";
				result.push(action);
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