package org.flowerplatform.flexdiagram.tool {
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayList;
	import mx.utils.ArrayUtil;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class WakeUpTool extends Tool {
				
		public static const ID:String = "WakeUpTool";
		
		public static const NO_EVENT:String = "none";
		public static const MOUSE_CLICK:String = "mouseClick";		
		public static const MOUSE_DRAG:String = "mouseDrag";
		public static const MOUSE_DOWN:String = "mouseDown";
		public static const MOUSE_UP:String = "mouseUp";
		
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
			diagramRenderer.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
		}
		
		override public function deactivateAsMainTool():void {			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);			
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
		}
				
		private function mouseDownHandler(event:MouseEvent):void {
			myEventType = MOUSE_DOWN;
			dispatchMyEvent(myEventType, event);			
		}
		
		private function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {
				myEventType = MOUSE_DRAG;
				dispatchMyEvent(myEventType, event);
			}			
		}
		
		private function mouseUpHandler(event:MouseEvent):void {
			if (myEventType != MOUSE_DRAG) {
				myEventType = MOUSE_UP;
				dispatchMyEvent(myEventType, event);
			}	
		}
			
		private function dispatchMyEvent(eventType:String, initialEvent:MouseEvent):void {
			var array:Array = filterAndSortListeners(eventType);
			
			while (array.length != 0) {				
				var tool:IWakeUpableTool = array.pop().tool;
				if (tool.wakeUp(eventType, initialEvent.ctrlKey, initialEvent.shiftKey)) {
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
			return array.sortOn("priority", Array.DESCENDING & Array.NUMERIC);
		}
	}
}