package org.flowerplatform.editor.model.controller {
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.emf_model.notation.Diagram;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexutil.ComposedList;
	
	public class DiagramModelChildrenController extends ViewModelChildrenController {
		public function DiagramModelChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		override public function getChildren(model:Object):IList {
			return new ComposedList([
				new ReferenceHolderList(View(model).persistentChildren_RH),
				new ReferenceHolderList(Diagram(model).persistentEdges_RH)
			]);
		}
		
		
	}
}