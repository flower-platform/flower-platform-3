package org.flowerplatform.editor.mindmap.controller
{
	import mx.collections.ArrayList;
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.communication.transferable_object.TransferableObjectDisposedEvent;
	import org.flowerplatform.communication.transferable_object.TransferableObjectUpdatedEvent;
	import org.flowerplatform.emf_model.notation.MindMapNode;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapControllerProvider;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelChildrenController;
	
	public class MindMapNodeChildrenController extends ControllerBase implements IMindMapModelChildrenController {
		
		public function MindMapNodeChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getChildrenBasedOnSide(model:Object, side:int = 0):IList /* of MindMapNode */ {
			if (side == 0) {
				side = model.side;
			}
			var list:ArrayList = new ArrayList();			
			for (var i:int = 0; i < getChildren(model).length; i++) {
				var child:MindMapNode = MindMapNode(getChildren(model).getItemAt(i));
				if (side == 0 || side == child.side) {
					list.addItem(child);
				}
			}
			return list;
		}
		
		public function getChildren(model:Object):IList	{
			return new ReferenceHolderList(View(model).persistentChildren_RH);
		}
		
		public function beginListeningForChanges(model:Object):void {
			View(model).addEventListener(TransferableObjectUpdatedEvent.OBJECT_UPDATED, objectUpdatedHandler);
			View(model).addEventListener(TransferableObjectDisposedEvent.OBJECT_DISPOSED, objectDisposedHandler);
		}
		
		public function endListeningForChanges(model:Object):void {
			View(model).removeEventListener(TransferableObjectUpdatedEvent.OBJECT_UPDATED, objectUpdatedHandler);
			View(model).removeEventListener(TransferableObjectDisposedEvent.OBJECT_DISPOSED, objectDisposedHandler);
		}
		
		protected function objectUpdatedHandler(event:TransferableObjectUpdatedEvent):void {
			var model:MindMapNode = MindMapNode(event.object);
		
			MindMapDiagramShell(diagramShell).removeModelFromRootChildren(model, true);			
			diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
			
			MindMapDiagramShell(diagramShell).addModelToRootChildren(model, true);			
			diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
		}
		
		protected function objectDisposedHandler(event:TransferableObjectDisposedEvent):void {
			var model:MindMapNode = MindMapNode(event.object);
			
			MindMapDiagramShell(diagramShell).removeModelFromRootChildren(model);	
			diagramShell.shouldRefreshVisualChildren(diagramShell.rootModel);
		}
		
		
	}
}