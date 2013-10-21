package org.flowerplatform.properties.ui {
	import flash.events.FocusEvent;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.ClassFactory;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.selection.SelectionChangedEvent;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	import spark.components.Button;
	import spark.components.List;
	import spark.components.VGroup;
	import spark.events.IndexChangeEvent;
	
	/**
	 * @author Razvan Tache
	 */
	public class PropertiesList extends List implements IViewContent {
		
		protected var _viewHost:IViewHost;
		
		public var propertyList:PropertiesList;
		
		public var selectionForServer:Object;
		
		public function PropertiesList() {
			super();
			itemRenderer = new ClassFactory(PropertiesItemRenderer);
			dataProvider = new ArrayList();
			FlexUtilGlobals.getInstance().selectionManager.addEventListener(SelectionChangedEvent.SELECTION_CHANGED, function (event:SelectionChangedEvent):void {
				// I did this, because the selection system, sends an empty selection when the diagram is opened
				// so in case the event.selectionForServer is an empty array we stop the logic.
				if (event.selectionForServer == null) 
					return;
				if (event.selectionForServer.length == 0)
					return;
				var myObject:Object;
				CommunicationPlugin.getInstance().bridge.sendObject(
					new InvokeServiceMethodServerCommand("propertiesProviderService",
						"getProperties",[event.selectionForServer],
						myObject,
						function(object:Object):void {
							dataProvider = IList(object);
							selectionForServer = event.selectionForServer;
						}
					));		
			});
		}
		
		public function getSelectionForServer():Object {
			return selectionForServer;	
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