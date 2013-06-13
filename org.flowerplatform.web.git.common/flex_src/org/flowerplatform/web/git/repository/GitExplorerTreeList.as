package org.flowerplatform.web.git.repository {
	import mx.events.IndexChangedEvent;
	
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.flexutil.popup.ActionBase;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.events.IndexChangeEvent;
	
	public class GitExplorerTreeList extends GenericTreeList implements IPopupContent {
		
		protected var _popupHost:IPopupHost;
		
		public function GitExplorerTreeList() {
			super();			
		}
				
		public function get popupHost():IPopupHost {
			return _popupHost;
		}

		public function set popupHost(value:IPopupHost):void {
			_popupHost = value;
		}

		public function getActions():Vector.<IAction> {
			return null;
		}
	}
}