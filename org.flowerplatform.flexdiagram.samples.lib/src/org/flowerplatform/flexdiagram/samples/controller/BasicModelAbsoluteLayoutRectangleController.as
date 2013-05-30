package org.flowerplatform.flexdiagram.samples.controller {
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class BasicModelAbsoluteLayoutRectangleController extends ControllerBase implements IAbsoluteLayoutRectangleController {
		public function BasicModelAbsoluteLayoutRectangleController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getBounds(model:Object):Rectangle {
			var basicModel:BasicModel = BasicModel(model);
			return new Rectangle(basicModel.x, basicModel.y, basicModel.width, basicModel.height);
		}
		
	}
}