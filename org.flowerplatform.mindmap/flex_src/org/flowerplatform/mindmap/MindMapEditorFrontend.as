package org.flowerplatform.mindmap {
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayCollection;
	import mx.collections.IList;
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.FlexEvent;
	import mx.managers.IFocusManagerComponent;
	
	import org.flowerplatform.communication.CommunicationPlugin;
	import org.flowerplatform.communication.service.InvokeServiceMethodServerCommand;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.util.infinitegroup.InfiniteScroller;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.action.IAction;
	import org.flowerplatform.flexutil.selection.ISelectionForServerProvider;
	import org.flowerplatform.flexutil.selection.ISelectionProvider;
	import org.flowerplatform.flexutil.view_content_host.IViewContent;
	import org.flowerplatform.flexutil.view_content_host.IViewHost;
	import org.flowerplatform.flexutil.view_content_host.IViewHostAware;
	import org.flowerplatform.mindmap.action.AddNodeAction;
	import org.flowerplatform.mindmap.action.RefreshAction;
	import org.flowerplatform.mindmap.action.ReloadAction;
	import org.flowerplatform.mindmap.action.RemoveNodeAction;
	import org.flowerplatform.mindmap.action.RenameAction;
	import org.flowerplatform.mindmap.action.SaveAction;
	import org.flowerplatform.mindmap.remote.Node;

	public class MindMapEditorFrontend extends VBox implements IViewContent, IFocusManagerComponent, ISelectionProvider, IViewHostAware {
		
		public var diagramShell:DiagramShell;
		
		protected var _viewHost:IViewHost;
		
		public function MindMapEditorFrontend() {
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}
		
		override protected function createChildren():void {			
			var scroller:InfiniteScroller = new InfiniteScroller();
			scroller.percentWidth = 100;
			scroller.percentHeight = 100;
			addChild(scroller);
			
			var diagramRenderer:DiagramRenderer = new DiagramRenderer();
			diagramRenderer.useGrid = false;
			scroller.viewport = diagramRenderer;
			diagramRenderer.horizontalScrollPosition = diagramRenderer.verticalScrollPosition = 0;
							
			diagramShell = new MindMapEditorDiagramShell();
			diagramShell.diagramRenderer = diagramRenderer;
			
			super.createChildren();					
		}
		
		private function creationCompleteHandler(event:FlexEvent):void {
			requestRootModel();
			diagramShell.selectedItems.addEventListener(CollectionEvent.COLLECTION_CHANGE, selectionChangedHandler);
		}
		
		protected function selectionChangedHandler(e:CollectionEvent):void {
			// CollectionEvent.COLLECTION_CHANGE will be triggered even when an item is updated (CollectionEventKind.UPDATE), so ignore it
			if (!diagramShell.selectedItems.eventsCanBeIgnored && e.kind != CollectionEventKind.UPDATE) { // catch events only if necessary
				FlexUtilGlobals.getInstance().selectionManager.selectionChanged(viewHost, this);
			}
		}
		public function getActions(selection:IList):Vector.<IAction> {	
			var result:Vector.<IAction> = new Vector.<IAction>();
			
			result.push(new AddNodeAction());
			result.push(new RemoveNodeAction());
			
			result.push(new RefreshAction(this));
			result.push(new RefreshAction(this, true));
			
			result.push(new RenameAction());
			
			result.push(new ReloadAction(this));
			result.push(new SaveAction());
			
			return result;
		}
		
		public function getSelection():IList {			
			return diagramShell.selectedItems;
		}
		
		public function set viewHost(value:IViewHost):void {
			_viewHost = value;
			
		}
		
		public function get viewHost():IViewHost {
			return _viewHost;
		}
		
		public function requestRootModel():void {
			MindMapPlugin.getInstance().service.getChildrenForNodeId(null, this, getChildrenForNodeIdCallbackHandler);
		}
		
		private function getChildrenForNodeIdCallbackHandler(result:ArrayCollection):void {
			var diagram:Diagram = new Diagram();
			
			var node:Node = Node(result[1].getItemAt(0));
			node.side = MindMapDiagramShell.NONE;
			diagram.rootNode = node;
					
			diagramShell.rootModel = diagram;			
			IMindMapControllerProvider(diagramShell.getControllerProvider(node)).getMindMapModelController(node).setExpanded(node, true);
		}
	}
}