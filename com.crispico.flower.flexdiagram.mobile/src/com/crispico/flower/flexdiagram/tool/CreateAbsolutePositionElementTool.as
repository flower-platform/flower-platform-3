package com.crispico.flower.flexdiagram.tool {
	import com.crispico.flower.flexdiagram.AbsolutePositionEditPartUtils;
	import com.crispico.flower.flexdiagram.ui.MoveResizePlaceHolder;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.ui.Keyboard;
	
	import mx.core.Container;
	
	
	/**
	 * Generic tool for creation absolute position elements.
	 * 
	 * Subclasses are responsable to implement the 
	 * <code>createEditPart()</code> 
	 * @flowerModelElementId _by-Mw78REd6XgrpwHbbsYQ
	 */
	public class CreateAbsolutePositionElementTool extends CreateElementTool {
		
		/**
		 * @flowerModelElementId _by-Myr8REd6XgrpwHbbsYQ
		 */
		protected var creationPlaceHolder:MoveResizePlaceHolder = new MoveResizePlaceHolder();

		/**
		 * MouseMove will add the placeholder when button is pressed
		 * @flowerModelElementId _bzH9t78REd6XgrpwHbbsYQ
		 */
		override protected function mouseMoveHandler(event:MouseEvent):void {
			if (event.buttonDown) {
				if (targetEditPart != null && targetEditPart.isEditPartAccepted(newEditPart)) {
					var mousePoint:Point = globalToDiagram(event.stageX, event.stageY);
					if (creationPlaceHolder.parent == null) {
						// place holder is not added to the diagram
						creationPlaceHolder.x = mousePoint.x;
						creationPlaceHolder.y = mousePoint.y;
						// reset placeHolder's dimensions
						// because otherwise is posible to display a huge one and then redimensionate it
						creationPlaceHolder.width = 1;
						creationPlaceHolder.height = 1; 
						// add it now
						AbsolutePositionEditPartUtils.addChildFigure(Container(diagramViewer.getRootEditPart().getFigure()), creationPlaceHolder);
					} else {
						// place holder is added to the diagram
						// modify it's dimensions
						if (mousePoint.x - creationPlaceHolder.x > 0) {
							creationPlaceHolder.width = mousePoint.x - creationPlaceHolder.x;
						}
						if (mousePoint.y - creationPlaceHolder.y > 0) {
							creationPlaceHolder.height = mousePoint.y - creationPlaceHolder.y;
						}
						creationPlaceHolder.invalidateSize();						
					}
				}
			} else {
				super.mouseMoveHandler(event);
			}
			
		}
		
		/**
		 * On mouse down do nothing because we don't want to add anything 
		 * at mouse down
		 * @flowerModelElementId _bzH9vb8REd6XgrpwHbbsYQ
		 */
		override protected function mouseDownHandler(event:MouseEvent):void {
			// because we have overridden mouseDownHandler we need to
			// do this here 
			dragInProgress = true;
		}
		
		/**
		 * On mouse up we need to add a new figure at the selected position
		 * with the selected dimensions.
		 * 
		 * <p>
		 * It delegate to <code>EditPart</code> the creation of the new edit part.
		 * 
		 * @author Cristi
		 * @flowerModelElementId _bzH9w78REd6XgrpwHbbsYQ
		 */
		override protected function mouseUpHandler(event:MouseEvent):void {
			// now update the cursor based on what is under mouse
			super.mouseUpHandler(event);
			// remove place holder from diagram
			if (creationPlaceHolder.parent != null) {
				AbsolutePositionEditPartUtils.removeChildFigure(Container(diagramViewer.getRootEditPart().getFigure()), creationPlaceHolder);
				// on mouseUp create a AbsolutePositionFigure at the position and with dimensions required
				// create the AbsolutePositionFigure only if the placeHolder was on the diagram
				if (targetEditPart != null && targetEditPart.isEditPartAccepted(newEditPart)) {
					callAcceptNewEditPart(editPart, creationPlaceHolder.x, creationPlaceHolder.y, 
						creationPlaceHolder.width, creationPlaceHolder.height);
				}
			}
			
		}
		
		/**
		 * On ESC key, abort a creation (if in progress).
		 * @flowerModelElementId _bzH9yb8REd6XgrpwHbbsYQ
		 */
		override protected function keyDownHandler(event:KeyboardEvent):void {
			super.keyDownHandler(event);
			if (event.keyCode == Keyboard.ESCAPE && creationPlaceHolder.parent != null) {
				// remove placeholder from diagram
				AbsolutePositionEditPartUtils.removeChildFigure(Container(diagramViewer.getRootEditPart().getFigure()), creationPlaceHolder);
				// signal no more recreating the placeholder
				targetEditPart = null;
				targetObject = null;
			}
		}
	}
}