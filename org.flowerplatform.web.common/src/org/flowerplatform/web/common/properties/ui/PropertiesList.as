package org.flowerplatform.web.common.properties.ui {
	import flash.events.FocusEvent;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.events.IndexChangedEvent;
	
	import org.flowerplatform.communication.tree.GenericTreeList;
	import org.flowerplatform.editor.action.EditorTreeActionProvider;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IActionProvider;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	import org.flowerplatform.web.common.WebCommonPlugin;
	
	import spark.components.List;
	import spark.events.IndexChangeEvent;

	public class PropertiesList extends List implements IPopupContent {
		
		protected var _popupHost:IPopupHost;
				
		public function PropertiesList() {
			super();
			addEventListener(IndexChangeEvent.CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:IndexChangeEvent):void {
			if (popupHost) {
				popupHost.refreshActions(this);
			}
		}
		
		public function get popupHost():IPopupHost {
			return _popupHost;
		}
		
		public function set popupHost(value:IPopupHost):void {
			_popupHost = value;
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			return null;
		}
		
		public function getSelection():IList {
			return null;
		}
		
		override protected function focusInHandler(event:FocusEvent):void {
			super.focusInHandler(event);
			popupHost.activePopupContent = this;
		}
	}
}