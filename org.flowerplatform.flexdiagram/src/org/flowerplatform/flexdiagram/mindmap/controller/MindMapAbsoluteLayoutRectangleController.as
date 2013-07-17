package org.flowerplatform.flexdiagram.mindmap.controller {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.mindmap.MindMapDiagramShell;
	import org.flowerplatform.flexdiagram.mindmap.controller.IMindMapModelController;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class MindMapAbsoluteLayoutRectangleController extends ControllerBase implements IAbsoluteLayoutRectangleController {
		
		public function MindMapAbsoluteLayoutRectangleController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getBounds(model:Object):Rectangle {
			var modelController:IMindMapModelController = MindMapDiagramShell(diagramShell).getModelController(model);
			return new Rectangle(
				modelController.getX(model), 
				modelController.getY(model), 
				modelController.getWidth(model), 
				modelController.getHeight(model));
		}
		
	}
}