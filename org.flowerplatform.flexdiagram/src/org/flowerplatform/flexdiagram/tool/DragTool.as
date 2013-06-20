package org.flowerplatform.flexdiagram.tool {
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.collections.ArrayList;
	import mx.core.IDataRenderer;
	import mx.core.IVisualElement;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.controller.IDragController;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class DragTool extends Tool implements IWakeUpableTool {
		
		public static const ID:String = "DragTool";
		
		public function DragTool(diagramShell:DiagramShell)	{
			super(diagramShell);
			
			WakeUpTool.wakeMeUpIfEventOccurs(this, WakeUpTool.MOUSE_DRAG);
		}
		
		public function wakeUp(eventType:String, initialEvent:MouseEvent):Boolean {
			var renderer:IVisualElement = getRendererFromDisplayCoordinates();
			if (renderer is DiagramRenderer) {
				return false;
			}
			if (renderer is IDataRenderer) {
				var model:Object = IDataRenderer(renderer).data;
				return diagramShell.getControllerProvider(model).getDragController(model) != null &&
					   (diagramShell.selectedItems.getItemIndex(model) != -1);						
			}
			return false;
		}
				
		override public function activateAsMainTool():void {			
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
				
			context.initialMousePoint = globalToDiagram(Math.ceil(diagramRenderer.stage.mouseX), Math.ceil(diagramRenderer.stage.mouseY));
						
			var renderer:IDataRenderer = IDataRenderer(getRendererFromDisplayCoordinates());
			
			if (diagramShell.selectedItems.getItemIndex(renderer.data) != -1) {				
				for (var i:int = 0; i < diagramShell.selectedItems.length; i++) {
					var model:Object = diagramShell.selectedItems.getItemAt(i);
					var dragController:IDragController = diagramShell.getControllerProvider(model).getDragController(model);
					if (dragController != null) {
						if (context.draggableItems == null) {
							context.draggableItems = new ArrayList();
						}
						ArrayList(context.draggableItems).addItem(model);
						dragController.activate(model, context.initialMousePoint.x, context.initialMousePoint.y);						
					}
				}	
			}		
		}
		
		override public function deactivateAsMainTool():void {			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			
			for (var i:int = 0; i < ArrayList(context.draggableItems).length; i++) {
				var model:Object = ArrayList(context.draggableItems).getItemAt(i);
				diagramShell.getControllerProvider(model).getDragController(model).deactivate(model);												
			}		
			
			delete context.initialMousePoint;			
			delete context.draggableItems;
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {			
			if (event.buttonDown) {		
				var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
				var deltaX:int = mousePoint.x - context.initialMousePoint.x;
				var deltaY:int = mousePoint.y - context.initialMousePoint.y;
				
				for (var i:int = 0; i < ArrayList(context.draggableItems).length; i++) {
					var model:Object = ArrayList(context.draggableItems).getItemAt(i);
					diagramShell.getControllerProvider(model).getDragController(model).drag(model, deltaX, deltaY);										
				}
			} else {				
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function mouseUpHandler(event:MouseEvent):void {
			for (var i:int = 0; i < ArrayList(context.draggableItems).length; i++) {
				var model:Object = ArrayList(context.draggableItems).getItemAt(i);
				diagramShell.getControllerProvider(model).getDragController(model).drop(model);				
			}
			diagramShell.mainToolFinishedItsJob();
		}
		
	}
}