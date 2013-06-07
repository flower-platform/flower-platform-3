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
		
		[Embed(source="../icons/move_cursor.png")]
		private var _moveCursor:Class;
		
		protected var initialPoint:Point = new Point();
		
		public function ScrollTool(diagramShell:DiagramShell) {
			super(diagramShell);		
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, MouseEvent.MOUSE_DOWN);
		}
	
		override public function activateAsMainTool():void {			
			initialPoint.x = diagramRenderer.stage.mouseX;
			initialPoint.y = diagramRenderer.stage.mouseY;
			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			diagramRenderer.cursorManager.setCursor(_moveCursor);
		}
				
		override public function deactivateAsMainTool():void {			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
		}
			
		public function wakeUp():Boolean {
			return getRendererFromDisplayCoordinates() is DiagramRenderer;
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {
				var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
				
				if (inDiagramVisibleArea(mousePoint.x, mousePoint.y)) {
					var deltaX:int = initialPoint.x - event.stageX;
					var deltaY:int = initialPoint.y - event.stageY;				
					initialPoint.x = event.stageX;
					initialPoint.y = event.stageY;
					
					scrollDiagram(deltaX, deltaY);
				}		
			} else {
				mouseUpHandler();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent = null):void {
			diagramRenderer.cursorManager.removeAllCursors();
			diagramShell.mainToolFinishedItsJob();
		}
		
		private function scrollDiagram(deltaX:int, deltaY:int):void {			
			var scrollPositionChanged:Boolean = false;
			
			var maxScrollPosition:Number = getMaxHorizontalScrollPosition();
			if (maxScrollPosition != 0) {
				var newPosX:Number = diagramRenderer.horizontalScrollPosition + deltaX;				
				// checking if new position between min and max scroll positions
				if (newPosX < 0) {
					newPosX = 0;
				} else if (newPosX > maxScrollPosition) {
					newPosX = maxScrollPosition;
				} 
				diagramRenderer.horizontalScrollPosition = newPosX;
				scrollPositionChanged = true;	
			}
			
			maxScrollPosition = getMaxHorizontalScrollPosition();
			if (maxScrollPosition != 0) {
				var newPosY:Number = diagramRenderer.verticalScrollPosition + deltaY;				
				// checking if new position between min and max scroll positions
				if (newPosY < 0) {
					newPosY = 0;
				} else if (newPosY > maxScrollPosition) {
					newPosY = maxScrollPosition;
				}
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