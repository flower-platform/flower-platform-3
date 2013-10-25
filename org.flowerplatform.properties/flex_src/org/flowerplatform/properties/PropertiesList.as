package org.flowerplatform.properties {
	import flash.events.FocusEvent;
	
	import flashx.textLayout.edit.ISelectionManager;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.ClassFactory;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.selection.SelectionChangedEvent;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	
	import spark.components.Button;
	import spark.components.List;
	import spark.components.VGroup;
	import spark.events.IndexChangeEvent;
	
	/**
	 * @author Razvan Tache
	 * @author Cristina Constantinescu
	 */
	public class PropertiesList extends List implements IViewContent {
		
		protected var _viewHost:IViewHost;
		
		public var propertyList:PropertiesList;
		
		public var selectionForServer:IList;
		
		public function PropertiesList() {
			super();
			itemRenderer = new ClassFactory(PropertiesItemRenderer);
			dataProvider = new ArrayList();
			
			// get selection from active provider to populate properties list with data
			var activeSelectionProvider:ISelectionProvider = FlexUtilGlobals.getInstance().selectionManager.activeSelectionProvider;
			if (activeSelectionProvider != null && activeSelectionProvider is ISelectionForServerProvider) {
				// create dummy event
				var event:SelectionChangedEvent = new SelectionChangedEvent();
				event.selection = activeSelectionProvider.getSelection();
				event.selectionForServer = ISelectionForServerProvider(activeSelectionProvider).convertSelectionToSelectionForServer(event.selection);
				
				selectionChangedHandler(event);
			}
			// listen for selection changes
			FlexUtilGlobals.getInstance().selectionManager.addEventListener(SelectionChangedEvent.SELECTION_CHANGED, selectionChangedHandler);
		}
		
		public function getSelectionForServer():IList {
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
		
		private function selectionChangedHandler(event:SelectionChangedEvent):void {
			// I did this, because the selection system, sends an empty selection when the diagram is opened
			// so in case the event.selectionForServer is an empty array we stop the logic.
			if (event.selectionForServer == null || event.selectionForServer.length == 0) 
				return;
						
			CommunicationPlugin.getInstance().bridge.sendObject(new InvokeServiceMethodServerCommand(
					"propertiesService", "getProperties", 
					[event.selectionForServer], 
					this, function(object:Object):void {
						dataProvider = IList(object);
						selectionForServer = event.selectionForServer;
					}));	
		}
		
	}
}