package org.flowerplatform.flexdiagram.tool {
	
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class SelectOrDragToCreateElementTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "SelectOrDragToCreateElementTool";
				
		public static const SELECT_MODE_ADD:String = "add";
		public static const SELECT_MODE_SUBSTRACT:String = "substract";
		
		public function SelectOrDragToCreateElementTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_DRAG, 1);
		}
		
		public function wakeUp(eventType:String, ctrlPressed:Boolean, shiftPressed:Boolean):Boolean {		
			context.wakedUp = (getRendererFromDisplayCoordinates() is DiagramRenderer) && (ctrlPressed || shiftPressed);
			context.ctrlPressed = ctrlPressed;
			context.shiftPressed = shiftPressed;
			return context.wakedUp;
		}	
		
		override public function activateAsMainTool():void {			
			if (context.wakedUp) {
				mouseDownHandler();
			} else {
				diagramRenderer.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			}
		}
		
		override public function deactivateAsMainTool():void {		
			if (!context.wakedUp) {
				diagramRenderer.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			}
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);	
			
			diagramShell.getControllerProvider(diagramShell.rootModel).
				getSelectOrDragToCreateElementController(diagramShell.rootModel).deactivate(diagramShell.rootModel);
						
			delete context.wakedUp;
			delete context.initialMousePoint;			
			delete context.ctrlPressed;
			delete context.shiftPressed;
		}		
		
		private function mouseDownHandler(event:MouseEvent = null):void {
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
				
			context.initialMousePoint = globalToDiagram(Math.ceil(diagramRenderer.stage.mouseX), Math.ceil(diagramRenderer.stage.mouseY));			
											
			diagramShell.getControllerProvider(diagramShell.rootModel).
				getSelectOrDragToCreateElementController(diagramShell.rootModel).activate(diagramShell.rootModel, context.initialMousePoint.x, context.initialMousePoint.y, getMode());
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {			
			if (event == null || event.buttonDown) {
				var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));				
				var deltaX:int = mousePoint.x - context.initialMousePoint.x;
				var deltaY:int = mousePoint.y - context.initialMousePoint.y;
				
				diagramShell.getControllerProvider(diagramShell.rootModel).
					getSelectOrDragToCreateElementController(diagramShell.rootModel).drag(diagramShell.rootModel, deltaX, deltaY);				
			} else {				
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent):void {
			diagramShell.getControllerProvider(diagramShell.rootModel).
				getSelectOrDragToCreateElementController(diagramShell.rootModel).drop(diagramShell.rootModel);
		}
		
		private function getMode():String {
			if (context.ctrlPressed) {
				return SELECT_MODE_SUBSTRACT;
			}
			if (context.shiftPressed) {
				return SELECT_MODE_ADD;
			}
			return null;
		}

	}
	
}