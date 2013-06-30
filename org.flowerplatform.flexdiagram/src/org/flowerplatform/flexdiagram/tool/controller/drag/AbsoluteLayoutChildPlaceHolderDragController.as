package org.flowerplatform.flexdiagram.tool.controller.drag {
	import flash.geom.Rectangle;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class AbsoluteLayoutChildPlaceHolderDragController extends AbstractPlaceHolderDragController {
		public function AbsoluteLayoutChildPlaceHolderDragController(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		override protected function getInitialBounds(model:Object):Rectangle {
			return diagramShell.getControllerProvider(model).getAbsoluteLayoutRectangleController(model).getBounds(model);
		}
		
	}
}