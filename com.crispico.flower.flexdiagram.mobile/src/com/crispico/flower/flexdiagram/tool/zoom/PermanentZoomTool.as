package com.crispico.flower.flexdiagram.tool.zoom {
	import com.crispico.flower.flexdiagram.RootFigure;
	import com.crispico.flower.flexdiagram.tool.Tool;
	
	import flash.events.MouseEvent;
	import flash.geom.Point;

	/**
	 * <p> The tool is activated when the diagram is created. It will
	 * remain active until the diagram is closed. Its action doesn't 
	 * interfere with any other tool.
	 * 
	 * <p> The handler is called by the Application when it detects a 
	 * mouse wheel event and the CTRL key pressed. The application
	 * catches the event and prevent the default behavior (the vertical
	 * scrollbar changes position). 
	 * Otherwise, if the event is cought here, the default behavior cannot
	 * be prevented.
	 * 
	 * @author Georgi
	 * @flowerModelElementId _IQaSE8VeEd6x1dpkaVcaXg
	 */
	public class PermanentZoomTool extends Tool {
		
		/**
		 * The method is called from <code>BaseFlowerDesigner</code> when a
		 * <code>MouseEvent.MOUSE_WHEEL</code> event with CTRL key pressed was
		 * fired. The method computes the correct parameters and calls 
		 * <code>RootFigure.zoomToPercent()</code> method.
		 * 
		 * @flowerModelElementId _IQaSGMVeEd6x1dpkaVcaXg
		 */
		public function mouseWheelHandler(event:MouseEvent, dx:Number, dy:Number):void {
			event.preventDefault();
			var p:Point = new Point(dx, dy);
			var delta:Number = 0.1;
			if (event.delta < 0)
				delta *= -1;
			RootFigure(diagramViewer.getRootEditPart().getFigure()).zoomToPercent(p.x, p.y, delta, true);
		}
		
		/** This tool doesn't need to handle the autoscroll
		 */
		override protected function toolHandlesAutoScroll():Boolean {
			return 	false;
		}
	}
}