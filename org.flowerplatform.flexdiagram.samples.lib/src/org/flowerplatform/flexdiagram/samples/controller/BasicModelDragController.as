package org.flowerplatform.flexdiagram.samples.controller
{
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.tool.controller.IDragController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	public class BasicModelDragController extends ControllerBase implements IDragController	{
		
		public function BasicModelDragController(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
						
		public function activate(model:Object, initialX:Number, initialY:Number):void {
			var movePlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();
			movePlaceHolder.x = BasicModel(model).x;
			movePlaceHolder.y = BasicModel(model).y;
			movePlaceHolder.width = BasicModel(model).width;
			movePlaceHolder.height = BasicModel(model).height;
			
			diagramShell.modelToExtraInfoMap[model].movePlaceHolder = movePlaceHolder;
			diagramShell.diagramRenderer.addElement(movePlaceHolder);
			
			diagramShell.modelToExtraInfoMap[model].initialX = movePlaceHolder.x;
			diagramShell.modelToExtraInfoMap[model].initialY = movePlaceHolder.y;
		}
		
		public function drag(model:Object, deltaX:Number, deltaY:Number):void {
			var movePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].movePlaceHolder;
			
			movePlaceHolder.x = Math.max(0, diagramShell.modelToExtraInfoMap[model].initialX + deltaX);
			movePlaceHolder.y = Math.max(0, diagramShell.modelToExtraInfoMap[model].initialY + deltaY);		
		}
		
		public function drop(model:Object):void {
			var movePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].movePlaceHolder;
						
			BasicModel(model).x = movePlaceHolder.x;
			BasicModel(model).y = movePlaceHolder.y;
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