package org.flowerplatform.flexdiagram.tool
{
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TransformGestureEvent;
	import flash.geom.Matrix;
	import flash.geom.Point;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	
	/**
	 * @author Cristina Constantinescu
	 */ 
	public class ZoomTool extends Tool {
		
		public static const ID:String = "ZoomTool";
		
		public function ZoomTool(diagramShell:DiagramShell)	{
			super(diagramShell);
		}
		
		override public function activateDozingMode():void {
			diagramRenderer.addEventListener(MouseEvent.MOUSE_WHEEL, mouseWheelHandler, false, int.MAX_VALUE);
			diagramRenderer.addEventListener(TransformGestureEvent.GESTURE_ZOOM, gestureZoomHandler);
		}
		
		override public function deactivateDozingMode():void {
			diagramRenderer.removeEventListener(MouseEvent.MOUSE_WHEEL, mouseWheelHandler);
			diagramRenderer.removeEventListener(TransformGestureEvent.GESTURE_ZOOM, gestureZoomHandler);
		}
		
		private function mouseWheelHandler(event:MouseEvent):void {			
			if (event.ctrlKey && event.type == MouseEvent.MOUSE_WHEEL) {
				diagramShell.mainTool = this;
						
				var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
								
				var scale:Number = 0.1;
				if (event.delta < 0) {
					scale = 1 - scale;
				} else {
					scale = 1 + scale;
				}
	
				// get the transformation matrix of this object
				var affineTransform:Matrix = diagramRenderer.transform.matrix;				
				// scale
				affineTransform.scale(scale, scale);				
				// apply the new transformation to the object
				diagramRenderer.transform.matrix = affineTransform;
					
				// scrolling the mouse with ctrl pressed
				// The delta parameter represents the number of lines to be scrolled, when using the mouse wheel. This 
				// parameter is given by the operating system. We have enforced that the delta should be different from 0
				// because on Mac, when scrolling the wheel, with the most small step, the delta returned is 0. 
				// On Mac this means that the mouse wheel must do bigger steps (scroll faster) in order for the zoom to work.
				// TODO sorin : on Mac OS , native listeners should be added to fix the problem.
				if (event.delta != 0) {
				
				}
				// We stop the propagation of the event even if the delta is 0 because on Mac, sometimes when scrolling
				// with CMD pressed at a speed not big enougth to be caught with delta !=0, it seams that it scrolls the diagram,
				// instead to perform a zooming.
				event.preventDefault();
				
				diagramShell.mainToolFinishedItsJob();
			}
		}
		
		private function gestureZoomHandler(event:TransformGestureEvent):void {
			
		}
	
		public function scaleAt(scale:Number) : void {
			// get the transformation matrix of this object
			var affineTransform:Matrix = diagramRenderer.transform.matrix;
			
			// scale
			affineTransform.scale(scale, scale);
			
			// apply the new transformation to the object
			diagramRenderer.transform.matrix = affineTransform;
		}
	}
}