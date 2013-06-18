package org.flowerplatform.flexdiagram.samples.controller {
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.IDragToCreateRelationController;
	
	public class BasicModelDragToCreateRelationController extends ControllerBase implements IDragToCreateRelationController	{
		
		public function BasicModelDragToCreateRelationController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function activate(model:Object):void {
			trace("startDragging");
		}
		
		public function drag(model:Object):void {
			trace("update");
		}
		
		public function drop(model:Object):void {
			trace("endDragging");		
			
			diagramShell.mainToolFinishedItsJob();
		}
		
		public function deactivate(model:Object):void {
			trace("deactivate");
			
		}
	}
}