package org.flowerplatform.flexdiagram.samples.controller {
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IResizeController;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	
	public class BasicModelResizeController extends ControllerBase implements IResizeController {
		
		public function BasicModelResizeController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function resize(model:Object, deltaX:Number, deltaY:Number):void {			
			BasicModel(model).width += deltaX;
			BasicModel(model).height += deltaY;
		}
	}
}