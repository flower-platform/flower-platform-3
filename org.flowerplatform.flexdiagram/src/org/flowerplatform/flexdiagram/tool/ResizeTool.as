package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.tool.controller.IResizeController;
	import org.flowerplatform.flexdiagram.ui.ResizeAnchor;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ResizeTool extends Tool implements IWakeUpableTool {
			
		public static const ID:String = "ResizeTool";
		
		public function ResizeTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_DOWN);
		}
		
		public function wakeUp(eventType:String, ctrlPressed:Boolean, shiftPressed:Boolean):Boolean {
			return getResizeAnchorFromDisplayCoordinates() != null;
		}
		
		override public function activateAsMainTool():void {
			context.initialMousePoint = globalToDiagram(Math.ceil(diagramRenderer.stage.mouseX), Math.ceil(diagramRenderer.stage.mouseY));
			
			var resizeAnchor:ResizeAnchor = getResizeAnchorFromDisplayCoordinates();		
			context.resizeType = resizeAnchor.type;
			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
				var model:Object = diagramShell.selectedItems.getItemAt(i);
				var resizeController:IResizeController = diagramShell.getControllerProvider(model).getResizeController(model);
				if (resizeController != null) {
					resizeController.activate(model);
				}				
			}			
		}
		
		override public function deactivateAsMainTool():void {
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
				var model:Object = diagramShell.selectedItems.getItemAt(i);
				var resizeController:IResizeController = diagramShell.getControllerProvider(model).getResizeController(model);
				if (resizeController != null) {
					resizeController.deactivate(model);
				}				
			}	
			
			delete context.initialMousePoint;
			delete context.resizeType;
		}
				
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {		
				var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
				var deltaX:int = mousePoint.x - context.initialMousePoint.x;
				var deltaY:int = mousePoint.y - context.initialMousePoint.y;		

				for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
					var model:Object = diagramShell.selectedItems.getItemAt(i);
					var resizeController:IResizeController = diagramShell.getControllerProvider(model).getResizeController(model);
					if (resizeController != null) {
						resizeController.drag(model, deltaX, deltaY, context.resizeType);
					}				
				}				
			} else {
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent = null):void {
			for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
				var model:Object = diagramShell.selectedItems.getItemAt(i);
				var resizeController:IResizeController = diagramShell.getControllerProvider(model).getResizeController(model);
				if (resizeController != null) {
					resizeController.drop(model);
				}				
			}		
			diagramShell.mainToolFinishedItsJob();
		}
				
		private function getResizeAnchorFromDisplayCoordinates():ResizeAnchor  {
			var stage:Stage = DisplayObject(diagramShell.diagramRenderer).stage;
			var array:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
			
			var anchor:ResizeAnchor;
			var i:int;
			for (i = array.length - 1; i >= 0;  i--) {
				anchor = getResizeAnchorFromDisplay(array[i]);
				if (anchor != null) {					
					return anchor;
				}
			}
			return null;
		}
		
		private function getResizeAnchorFromDisplay(obj:Object):ResizeAnchor {			
			// in order for us to traverse its hierrarchy it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}			
			// traverse all the obj's hierarchy	
			while (obj != null) {				
				if (obj is ResizeAnchor) { // found it					
					return ResizeAnchor(obj);					
				}
				obj = DisplayObject(obj).parent;
			}			
			return null;
		}
	}
}