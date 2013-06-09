package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	
	import mx.core.IDataRenderer;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.controller.IAbsoluteLayoutRectangleController;
	import org.flowerplatform.flexdiagram.controller.IControllerProvider;
	import org.flowerplatform.flexdiagram.controller.selection.AnchorsSelectionController;
	import org.flowerplatform.flexdiagram.renderer.selection.AnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.renderer.selection.ResizeAnchor;
	
	import spark.primitives.Rect;
	
	public class ResizeTool extends Tool implements IWakeUpableTool {
			
		public function ResizeTool(diagramShell:DiagramShell) {
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_DRAG, 1);
		}
		
		public function wakeUp(eventType:String):Boolean {
			return getResizeAnchorFromDisplayCoordinates() != null;
		}
		
		override public function activateAsMainTool():void {
			context = new Object();
			context.initialX = diagramRenderer.stage.mouseX;
			context.initialY = diagramRenderer.stage.mouseY;
			
			var resizeAnchor:ResizeAnchor = getResizeAnchorFromDisplayCoordinates();			
			context.model = AnchorsSelectionRenderer(resizeAnchor.parent).getTargetModel();
			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
		}
		
		override public function deactivateAsMainTool():void {
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
		}
				
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {				
				var deltaX:int = event.stageX - context.initialX;
				var deltaY:int = event.stageY - context.initialY;		
				context.initialX = event.stageX;
				context.initialY = event.stageY;
				
				diagramShell.getControllerProvider(context.model).
					getResizeController(context.model).resize(context.model, deltaX, deltaY);
					
			} else {
				mouseUpHandler();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent = null):void {			
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