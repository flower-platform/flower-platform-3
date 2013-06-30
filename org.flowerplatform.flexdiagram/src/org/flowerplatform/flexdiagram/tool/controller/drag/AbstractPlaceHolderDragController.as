package org.flowerplatform.flexdiagram.tool.controller.drag {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class AbstractPlaceHolderDragController extends ControllerBase implements IDragController {
		
		public function AbstractPlaceHolderDragController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		protected function getInitialBounds(model:Object):Rectangle {
			throw new Error("This method should be implemented");
		}
						
		public function activate(model:Object, initialX:Number, initialY:Number):void {
			var movePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			var rect:Rectangle = getInitialBounds(model);
			movePlaceHolder.x = rect.x;
			movePlaceHolder.y = rect.y;
			movePlaceHolder.width = rect.width;
			movePlaceHolder.height = rect.height;
			
			diagramShell.modelToExtraInfoMap[model].movePlaceHolder = movePlaceHolder;
			diagramShell.diagramRenderer.addElement(movePlaceHolder);
			
			diagramShell.modelToExtraInfoMap[model].initialX = movePlaceHolder.x;
			diagramShell.modelToExtraInfoMap[model].initialY = movePlaceHolder.y;
		}
		
		public function drag(model:Object, deltaX:Number, deltaY:Number):void {
			var movePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].movePlaceHolder;
			
			movePlaceHolder.x = diagramShell.modelToExtraInfoMap[model].initialX + deltaX;
			movePlaceHolder.y = diagramShell.modelToExtraInfoMap[model].initialY + deltaY;		
		}
		
		public function drop(model:Object):void {
			throw new Error("This method should be implemented");
		}
		
		public function deactivate(model:Object):void {
			var movePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].movePlaceHolder;			
			diagramShell.diagramRenderer.removeElement(movePlaceHolder);
			
			delete diagramShell.modelToExtraInfoMap[model].movePlaceHolder;			
			delete diagramShell.modelToExtraInfoMap[model].initialX;
			delete diagramShell.modelToExtraInfoMap[model].initialY;			
		}		
		
	}
}