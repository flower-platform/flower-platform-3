package org.flowerplatform.editor.model.controller {
	import mx.collections.IList;
	
	import org.flowerplatform.communication.transferable_object.ReferenceHolderList;
	import org.flowerplatform.emf_model.notation.View;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.model_children.IModelChildrenController;
	
	public class ViewModelChildrenController extends ControllerBase implements IModelChildrenController {
		
		public function ViewModelChildrenController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getChildren(model:Object):IList	{
			return new ReferenceHolderList(View(model).persistentChildren_RH);
		}
		
		public function beginListeningForChanges(model:Object):void	{
		}
		
		public function endListeningForChanges(model:Object):void {
		}
	}
}