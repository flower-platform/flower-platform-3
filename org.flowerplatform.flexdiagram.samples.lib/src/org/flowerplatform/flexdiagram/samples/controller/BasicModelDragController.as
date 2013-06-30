package org.flowerplatform.flexdiagram.samples.controller {
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	import org.flowerplatform.flexdiagram.tool.controller.drag.AbsoluteLayoutChildPlaceHolderDragController;
	import org.flowerplatform.flexdiagram.tool.controller.drag.IDragController;
	import org.flowerplatform.flexdiagram.ui.MoveResizePlaceHolder;
	
	public class BasicModelDragController extends AbsoluteLayoutChildPlaceHolderDragController {
		
		public function BasicModelDragController(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
		
		override public function drop(model:Object):void {
			var movePlaceHolder:MoveResizePlaceHolder = diagramShell.modelToExtraInfoMap[model].movePlaceHolder;
						
			BasicModel(model).x = movePlaceHolder.x;
			BasicModel(model).y = movePlaceHolder.y;
		}
		
	}
}