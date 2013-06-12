package org.flowerplatform.flexdiagram.tool {
	
	import flash.display.DisplayObject;
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.Mouse;
	
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.managers.CursorManager;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	
	import spark.components.Scroller;
	import spark.core.IViewport;

	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ScrollTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "ScrollTool";
		
		[Embed(source="../icons/move_cursor.png")]
		private var _moveCursor:Class;
				
		public function ScrollTool(diagramShell:DiagramShell) {
			super(diagramShell);		
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_DRAG);
		}
	
		public function wakeUp(eventType:String, ctrlPressed:Boolean, shiftPressed:Boolean):Boolean {
			return getRendererFromDisplayCoordinates() is DiagramRenderer;
		}
		
		override public function activateAsMainTool():void {			
			context.initialX = diagramRenderer.stage.mouseX;
			context.initialY = diagramRenderer.stage.mouseY;
			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			diagramRenderer.cursorManager.setCursor(_moveCursor, 2, -16, -16);
		}
				
		override public function deactivateAsMainTool():void {			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			diagramRenderer.cursorManager.removeAllCursors();
			delete context.initialX;
			delete context.initialY;
		}
			
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {
				var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
				
				//if (inDiagramVisibleArea(mousePoint.x, mousePoint.y)) {
					var deltaX:int = context.initialX - event.stageX;
					var deltaY:int = context.initialY - event.stageY;				
					context.initialX = event.stageX;
					context.initialY = event.stageY;
					
					scrollDiagram(deltaX, deltaY);
				//}		
			} else {
				mouseUpHandler();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent = null):void {			
			diagramShell.mainToolFinishedItsJob();
		}
		
		private function scrollDiagram(deltaX:int, deltaY:int):void {			
			var scrollPositionChanged:Boolean = false;
			
			var maxScrollPosition:Number = getMaxHorizontalScrollPosition();
			if (maxScrollPosition != 0) {
				var newPosX:Number = diagramRenderer.horizontalScrollPosition + deltaX;				
				diagramRenderer.horizontalScrollPosition = newPosX;
				scrollPositionChanged = true;	
			}
			
			maxScrollPosition = getMaxHorizontalScrollPosition();
			if (maxScrollPosition != 0) {
				var newPosY:Number = diagramRenderer.verticalScrollPosition + deltaY;				
				diagramRenderer.verticalScrollPosition = newPosY;
				scrollPositionChanged = true;
			}
			
			// track changes to scroll position because calling refreshVisualChildren can be expensive
			if (scrollPositionChanged) {
				diagramShell.getControllerProvider(diagramShell.rootModel).
					getVisualChildrenController(diagramShell.rootModel).refreshVisualChildren(diagramShell.rootModel);
			}
		}		
	}
	
}