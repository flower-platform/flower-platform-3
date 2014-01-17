package org.flowerplatform.mindmap.controller {
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.controller.visual_children.IVisualChildrenController;
	import org.flowerplatform.flexdiagram.event.UpdateConnectionEndsEvent;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.MindMapModelRendererController;
	import org.flowerplatform.mindmap.remote.Node;
	import org.flowerplatform.mindmap.renderer.NodeRenderer;
		
	public class NodeChildrenController extends ControllerBase implements IModelChildrenController {
		
		private static const EMPTY_LIST:ArrayList = new ArrayList();
		
		public function NodeChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getParent(model:Object):Object {
			return Node(model).parent;
		}
		
		public function getChildren(model:Object):IList	{
			return EMPTY_LIST;
		}
		
		public function beginListeningForChanges(model:Object):void	{	
			Node(model).addEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
		}
		
		public function endListeningForChanges(model:Object):void {		
			Node(model).removeEventListener(UpdateConnectionEndsEvent.UPDATE_CONNECTION_ENDS, updateConnectionEndsHandler);
		}
		
		protected function updateConnectionEndsHandler(event:UpdateConnectionEndsEvent):void {
			MindMapModelRendererController(MindMapDiagramShell(diagramShell).getControllerProvider(event.target).getRendererController(event.target)).updateConnectors(event.target);
		}
	}
}