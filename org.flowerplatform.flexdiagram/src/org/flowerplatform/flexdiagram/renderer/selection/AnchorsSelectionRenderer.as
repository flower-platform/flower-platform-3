package org.flowerplatform.flexdiagram.renderer.selection {
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.core.IVisualElement;
	import mx.events.MoveEvent;
	import mx.events.ResizeEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;

	/**	
	 * @author Cristina Constantinescu
	 */
	public class AnchorsSelectionRenderer extends AbstractSelectionRenderer {
		
		override public function activate(diagramShell:DiagramShell, target:IVisualElement):void {
			super.activate(diagramShell, target);			
			
			// set the handler that move/resize anchors with parent renderer.
			DisplayObject(target).addEventListener(MoveEvent.MOVE, handleTargetMoveResize); 
			DisplayObject(target).addEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);
			
			// TODO CC: modify when working at tools
//			DisplayObject(target.getEditPart().getViewer().getRootEditPart().getFigure()).addEventListener(MouseEvent.MOUSE_OVER, changeCursor);
			
			// update position
			handleTargetMoveResize(null);
		}
		
		/**
		 * 
		 * Called when we don't need the anchors, 
		 * and also when we don't want the anchors shown.
		 * @flowerModelElementId _b1gjZb8REd6XgrpwHbbsYQ
		 */
		override public function deactivate():void {
			// remove move/resize listeners
			DisplayObject(target).removeEventListener(MoveEvent.MOVE, handleTargetMoveResize);
			DisplayObject(target).removeEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);
			
			// TODO CC: modify when working at tools
//			DisplayObject(target.getEditPart().getViewer().getRootEditPart().getFigure()).removeEventListener(MouseEvent.MOUSE_OVER, changeCursor);
			
			super.deactivate();
		}
		
		protected function handleTargetMoveResize(event:Event):void {
			setLayoutBoundsPosition(target.x, target.y);
			setLayoutBoundsSize(target.width, target.height);
		}
	}
	
}