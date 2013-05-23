package com.crispico.flower.flexdiagram.tool.zoom {
	import com.crispico.flower.flexdiagram.AbsolutePositionEditPartUtils;
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.RootFigure;
	import com.crispico.flower.flexdiagram.tool.Tool;
	import com.crispico.flower.flexdiagram.ui.MoveResizePlaceHolder;
	
	import flash.display.DisplayObjectContainer;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.ui.Keyboard;
	
	import mx.core.Container;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;
	import mx.managers.CursorManager;
	
	/**
	 * The tool is activated when it is selected from the main toolbar 
	 * and deactivated when another tool is selected.
	 * 
	 * @author Georgi
	 * @flowerModelElementId _IRTp8MVeEd6x1dpkaVcaXg
	 */
	public class ZoomToRectangleTool extends Tool {
		
		/**
		 * If the current tool is active the mouse receives a zoom icon.
		 */
		[Embed(source='/icons/zoomCursor.png')]
		[Bindable]
		protected var zoomCursor:Class;
		
		/** 
		 * The rectangle figure used to select the area that will be
		 * maximised.
		 */
		private var zoomPlaceHolder:MoveResizePlaceHolder = null;
		
		/**
		 * <code>RootFigure</code> used to register mouse listeners.
		 */
		private var canvas:IVisualElementContainer;
				
		/**
		 * Listener for MouseEvent.MOUSE_DOWN. The placeHolder is created
		 * and added to the canvas.
		 */
		override protected function mouseDownHandler(event:MouseEvent):void {
			super.mouseDownHandler(event);
			var p:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
			
			//we disable the autoscroll outside the diagram
			if (inDiagramVisibleArea(p.x, p.y)) {
				zoomPlaceHolder = new MoveResizePlaceHolder();
				zoomPlaceHolder.x = p.x;
				zoomPlaceHolder.y = p.y;
				zoomPlaceHolder.width = 0;
				zoomPlaceHolder.height = 0;
				canvas.addElement(zoomPlaceHolder);
			}
		}
		
		/**
		 * Listener for MouseEvent.MOUSE_MOVE. It updates the width/height 
		 * of the placeHolder.
		 */
		override protected function mouseMoveHandler(event:MouseEvent):void {
			super.mouseMoveHandler(event);
			
			var p:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
			if (inDiagramVisibleArea(p.x, p.y))
				updateCursor(zoomCursor);
			else
				updateCursor(null);
			
			if (event.buttonDown && zoomPlaceHolder != null) {
				var p:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
				if (p.x > zoomPlaceHolder.x)
					zoomPlaceHolder.width = p.x - zoomPlaceHolder.x;
				if (p.y > zoomPlaceHolder.y)
					zoomPlaceHolder.height = p.y - zoomPlaceHolder.y;
			}
		}
		
		/**
		 * Listener for MouseEvent.MOUSE_UP events. If the event is generated,
		 * call <code>RootFigure.zoomToRectangle()</code> method and remove the
		 * zoom place holder.
		 * @flowerModelElementId _IRcz4sVeEd6x1dpkaVcaXg
		 */
		override protected function mouseUpHandler(event:MouseEvent):void {
			super.mouseUpHandler(event);
			if (zoomPlaceHolder != null) {
				if (zoomPlaceHolder.width != 0 && zoomPlaceHolder.height != 0)
					RootFigure(diagramViewer.getRootEditPart().getFigure()).zoomToRectangle(zoomPlaceHolder.x, zoomPlaceHolder.y, 
						zoomPlaceHolder.width, zoomPlaceHolder.height);
				canvas.removeElement(zoomPlaceHolder);
				zoomPlaceHolder = null;
			}
		}
		
		/**
		 * Listener for KeyboardEvent.KEY_DOWN events. If the ESC key was pressed
		 * while drawing the zoom rectangle, the zoom is canceled and the rectangle
		 * is removed from the diagram.
		 */
		override protected function keyDownHandler(event:KeyboardEvent):void {
			super.keyDownHandler(event);
			if (event.keyCode == Keyboard.ESCAPE && zoomPlaceHolder != null) {
				canvas.removeElement(zoomPlaceHolder);
				zoomPlaceHolder = null;
			}
		}
		
		/**
		 * Override the <code>Tool.activate()</code> method to register 
		 * <code>mouseUpHandler()</code> listener
		 * 
		 * <p>
		 * The method sets the zoom cursor by calling <code>updateCursor()</code>
		 * method from superclass.
		 */
		override public function activate(diagramViewer:DiagramViewer):void {
			super.activate(diagramViewer);
			canvas = AbsolutePositionEditPartUtils.getActualFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure())); 
		}
		
		/**
		 * Override the <code>Tool.deactivate()</code> method to unregister the
		 * listeners and removes the zoom cursor.
		 */
		override public function deactivate():void {
			UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors();
			super.deactivate();
		}
		
		/**
		 * Zoom tool label
		 */
		override public function getLabel():String {
			return "Zoom";
		}
		
		/**
		 * Zoom tool icon
		 */
		override public function getIcon():Class {
			return zoomCursor;
		}
		
		/**
		 * @flowerModelElementId _SEzPYETOEeCbnenRcrXIzw
		 */
		override public function canLock():Boolean {
			return false;
		}
	}
}