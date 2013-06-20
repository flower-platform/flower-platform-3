package org.flowerplatform.flexdiagram.tool {
	
	import flash.display.DisplayObject;
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.Keyboard;
	import flash.ui.Mouse;
	import flash.ui.Multitouch;
	
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
	
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			// TODO: s-ar putea aici sa nu fie ok localX/localY
			context.initialX = initialEvent.localX;
			context.initialY = initialEvent.localY;
			
			return getRendererFromDisplayCoordinates() is DiagramRenderer;
		}
		
		override public function activateAsMainTool():void {			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			if(!Multitouch.supportsGestureEvents) { // don't show cursor on touch screens
				diagramRenderer.cursorManager.setCursor(_moveCursor, 2, -16, -16);
			}
		}
				
		override public function deactivateAsMainTool():void {			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			
			if(!Multitouch.supportsGestureEvents) {
				diagramRenderer.cursorManager.removeAllCursors();
			}
			delete context.initialX;
			delete context.initialY;
		}
			
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {		
				scrollUnscaledPointToDiagramScreenPoint(context.initialX, context.initialY, event.stageX, event.stageY);
			} else {
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function scrollUnscaledPointToDiagramScreenPoint(unscaledPointX:Number, unscaledPointY:Number, diagramScreenPointX:Number, diagramScreenPointY:Number):void {
			var localPoint:Point = new Point();		
			localPoint.x = diagramScreenPointX + diagramRenderer.scrollRect.x;
			localPoint.y = diagramScreenPointY + diagramRenderer.scrollRect.y;
			
			var deltaX:int = unscaledPointX - localPoint.x;
			var deltaY:int = unscaledPointY -  localPoint.y;				
			
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
		
		private function mouseUpHandler(event:MouseEvent):void {			
			diagramShell.mainToolFinishedItsJob();
		}
		
	}	
}