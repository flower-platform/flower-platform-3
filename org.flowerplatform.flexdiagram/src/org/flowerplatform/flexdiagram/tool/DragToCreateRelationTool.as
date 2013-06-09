package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.selection.AnchorsSelectionRenderer;
	import org.flowerplatform.flexdiagram.renderer.selection.RelationAnchor;
	
	public class DragToCreateRelationTool extends Tool implements IWakeUpableTool {
		
		public function DragToCreateRelationTool(diagramShell:DiagramShell)	{
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_DRAG, 1);
		}
		
		public function wakeUp(eventType:String):Boolean {
			return getRelationAnchorFromDisplayCoordinates() != null;
		}
		
		override public function activateAsMainTool():void {
			context = new Object();
			var relationAnchor:RelationAnchor = getRelationAnchorFromDisplayCoordinates();			
			context.model = AnchorsSelectionRenderer(relationAnchor.parent).getTargetModel();
			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			diagramShell.getControllerProvider(context.model).
				getDragToCreateRelationController(context.model).startDragging(context.model);
		}
		
		override public function deactivateAsMainTool():void {
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			context = null;
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {				
				diagramShell.getControllerProvider(context.model).
					getDragToCreateRelationController(context.model).update(context.model);
			} else {
				mouseUpHandler();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent = null):void {			
			diagramShell.getControllerProvider(context.model).
				getDragToCreateRelationController(context.model).endDragging(context.model);
		}
		
		private function getRelationAnchorFromDisplayCoordinates():RelationAnchor  {
			var stage:Stage = DisplayObject(diagramShell.diagramRenderer).stage;
			var array:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
			
			var anchor:RelationAnchor;
			var i:int;
			for (i = array.length - 1; i >= 0;  i--) {
				anchor = getRelationAnchorFromDisplay(array[i]);
				if (anchor != null) {					
					return anchor;
				}
			}
			return null;
		}
		
		private function getRelationAnchorFromDisplay(obj:Object):RelationAnchor {			
			// in order for us to traverse its hierrarchy it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}			
			// traverse all the obj's hierarchy	
			while (obj != null) {				
				if (obj is RelationAnchor) { // found it					
					return RelationAnchor(obj);					
				}
				obj = DisplayObject(obj).parent;
			}			
			return null;
		}
	}
}