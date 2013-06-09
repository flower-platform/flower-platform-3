package org.flowerplatform.flexdiagram.samples.controller {
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IDragToCreateRelationController;
	
	public class BasicModelDragToCreateRelationController extends ControllerBase implements IDragToCreateRelationController	{
		
		public function BasicModelDragToCreateRelationController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function startDragging(model:Object):void {
			trace("startDragging");
		}
		
		public function update(model:Object):void {
			trace("update");
		}
		
		public function endDragging(model:Object):void {
			trace("endDragging");
			diagramShell.mainToolFinishedItsJob();
		}
	}
}