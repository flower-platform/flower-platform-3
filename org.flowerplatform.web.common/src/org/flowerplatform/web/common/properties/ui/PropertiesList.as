package org.flowerplatform.web.common.properties.ui {
	import flash.events.FocusEvent;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.ClassFactory;
	
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IPopupContent;
	import org.flowerplatform.flexutil.popup.IPopupHost;
	
	import spark.components.Button;
	import spark.components.List;
	import spark.components.VGroup;
	import spark.events.IndexChangeEvent;
	
	/**
	 * @author Tache Razvan Mihai
	 */
	public class PropertiesList extends List implements IPopupContent {
		
		protected var _popupHost:IPopupHost;
				
		public function PropertiesList() {
			super();
			itemRenderer = new ClassFactory(PropertyItemRenderer);
			dataProvider = new ArrayList();
			var property:Property = new Property();
			
			dataProvider.addItem(property);
			dataProvider.addItem(property);
			dataProvider.addItem(property);
			dataProvider.addItem(property);
			
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