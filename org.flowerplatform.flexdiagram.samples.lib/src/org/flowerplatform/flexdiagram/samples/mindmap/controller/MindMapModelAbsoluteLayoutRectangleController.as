package org.flowerplatform.flexdiagram.samples.mindmap.controller {
	import flash.geom.Rectangle;
	
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.samples.mindmap.model.MindMapModel;
	import org.flowerplatform.flexdiagram.samples.model.BasicModel;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class MindMapModelAbsoluteLayoutRectangleController extends ControllerBase implements IAbsoluteLayoutRectangleController {
		public function MindMapModelAbsoluteLayoutRectangleController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getBounds(model:Object):Rectangle {
			var basicModel:MindMapModel = MindMapModel(model);
			return new Rectangle(basicModel.x, basicModel.y, basicModel.width, basicModel.height);
		}
		
	}
}