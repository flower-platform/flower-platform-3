package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.events.EventDispatcher;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayList;
	import mx.utils.ArrayUtil;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class WakeUpTool extends Tool {
				
		private static var listeners:ArrayList = new ArrayList();
		
		private var myEventType:String;
		
		public function WakeUpTool(diagramShell:DiagramShell) {
			super(diagramShell);
		}
		
		public static function wakeMeUpIfEventOccurs(tool:Tool, event:String, priority:int = 0):void {
			var listener:Object = new Object();
			listener.tool = tool;
			listener.event = event;
			listener.priority = priority;
			
			listeners.addItem(listener);
		}
		
		override public function activateAsMainTool():void {
			diagramRenderer.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			diagramRenderer.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.addEventListener(MouseEvent.CLICK, mouseClickHandler);
		}
		
		override public function deactivateAsMainTool():void {			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			diagramRenderer.removeEventListener(MouseEvent.CLICK, mouseClickHandler);
		}
			
		private function mouseDownHandler(event:MouseEvent):void {
			myEventType = "mouseDown";
			trace("mouseDown");
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {
				myEventType = "mouseDrag";
				dispatchMyEvent("mouseDrag");
			}
			trace("mouseMove");
		}
		
		private function mouseClickHandler(event:MouseEvent):void {
			if (myEventType != "mouseDrag") {
				dispatchMyEvent("mouseClick");
			}
			trace("mouseClick");
		}
		
		private function dispatchMyEvent(eventType:String):void {
			var array:Array = filterAndSortListeners(eventType);
			
			while (array.length != 0) {
				var tool:IWakeUpableTool = array.pop().tool;
				if (tool.wakeUp()) {
					diagramShell.mainTool = Tool(tool);
					break;
				}
			}
		}
		
		private function filterAndSortListeners(eventType:String):Array {
			var array:Array = listeners.source.filter(
				function callback(item:*, index:int, array:Array):Boolean {
					return (item.event == eventType);
				}			
			);			
			return array.sortOn("priority", Array.NUMERIC);
		}
	}
}