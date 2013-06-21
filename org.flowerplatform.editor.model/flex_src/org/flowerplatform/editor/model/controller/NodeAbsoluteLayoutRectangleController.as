package org.flowerplatform.editor.model.controller {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.emf_model.notation.Bounds;
	import org.flowerplatform.emf_model.notation.Node;
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.ControllerBase;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class NodeAbsoluteLayoutRectangleController extends ControllerBase implements IAbsoluteLayoutRectangleController {
		public function NodeAbsoluteLayoutRectangleController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public function getBounds(model:Object):Rectangle {
			var bounds:Bounds = Bounds(Node(model).layoutConstraint);
			return new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		
	}
}