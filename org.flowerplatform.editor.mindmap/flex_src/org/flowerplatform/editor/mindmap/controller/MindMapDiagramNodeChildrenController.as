package org.flowerplatform.editor.mindmap.controller {
	import mx.collections.IList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	import mx.events.PropertyChangeEventKind;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.communication.transferable_object.TransferableObjectUpdatedEvent;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	
	public class MindMapDiagramNodeChildrenController extends ControllerBase implements IModelChildrenController {
		
		public function MindMapDiagramNodeChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getChildren(model:Object):IList {	
			if (MindMapDiagramShell(diagramShell).diagramChildren.length == 0) {
				MindMapDiagramShell(diagramShell).diagramChildren.addItem(View(model).persistentChildren_RH.getItemAt(0).referencedObject);
				diagramShell.shouldRefreshVisualChildren(model);
			}
			return MindMapDiagramShell(diagramShell).diagramChildren;
		}
		
		public function beginListeningForChanges(model:Object):void {
		}
		
		public function endListeningForChanges(model:Object):void {	
		}
		
	}
}