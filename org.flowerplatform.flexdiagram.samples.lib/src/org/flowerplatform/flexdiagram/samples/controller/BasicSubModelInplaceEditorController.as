package org.flowerplatform.flexdiagram.samples.controller
{
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.IInplaceEditorController;
	
	public class BasicSubModelInplaceEditorController extends ControllerBase implements IInplaceEditorController {
		
		public function BasicSubModelInplaceEditorController(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			trace("startEditing");
		}
		
		public function commit(model:Object):void {
			trace("commit");
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function abort(model:Object):void {
			trace("abort");
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			trace("endEditing");
		}		
	}
	
}