package org.flowerplatform.properties.ui {
	import flash.events.FocusEvent;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.ClassFactory;
	
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	import spark.components.Button;
	import spark.components.List;
	import spark.components.VGroup;
	import spark.events.IndexChangeEvent;
	
	/**
	 * @author Tache Razvan Mihai
	 */
	public class PropertiesList extends List implements IViewContent {
		
		protected var _viewHost:IViewHost;
		
		public var propertyList:PropertiesList;
		
		public var selectedItemsForProperties:Object;
		
		public function PropertiesList() {
			super();
			itemRenderer = new ClassFactory(PropertyItemRenderer);
			dataProvider = new ArrayList();
		}
		
		public function getSelectedItemsForProperties():Object {
			return selectedItemsForProperties;	
		}
		
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
		
		public function set viewHost(value:IViewHost):void {
			_viewHost = value;
		}
		
		public function getActions(selection:IList):Vector.<IAction> {
			return null;
		}
		
		public function getSelection():IList {
			return null;
		}
		
	}
}