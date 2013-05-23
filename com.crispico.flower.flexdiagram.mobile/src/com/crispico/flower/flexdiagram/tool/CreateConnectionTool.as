package com.crispico.flower.flexdiagram.tool {
	import com.crispico.flower.flexdiagram.AbsolutePositionEditPartUtils;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.connection.ConnectionEditPart;
	import com.crispico.flower.flexdiagram.ui.ConnectionFigure;
	
	import flash.display.DisplayObject;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.Keyboard;
	
	import mx.core.Container;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	
	
	/**
	 * Generic tool for creation of connection elements.
	 * 
	 * Subclasses are responsible to implement
	 * createEditPart
	 * 
	 * @author mircea
	 * @flowerModelElementId _bzRusL8REd6XgrpwHbbsYQ
	 */
	public class CreateConnectionTool extends CreateElementTool {
		
		/**
		 * When interval between mouse down and mouse up is very short in getConnectionAcceptedEditPart 
		 * localEditPart.isTargetConnectionEditPartAccepted is not called, so this flag is used to make sure that 
		 * target has been verified before create the connection
		 * @flowerModelElementId _F6un8ABVEd-COsJ6v0d3bA
		 */
		protected var targetAccepted:Boolean = false;
		
		/**
		 * Internal state variable that tells us what is the next
		 * head of the conection to be checked for acceptance
		 * and update cursor accordingly
		 * 		true = check head
		 * 		false = check tail
		 * @flowerModelElementId _bzRutb8REd6XgrpwHbbsYQ
		 */
		protected var checkSourceOrDestination:Boolean = true;
		
		/**
		 * Internal state variable that tells us the last EditPart
		 * that accepted our current head of connection
		 * @flowerModelElementId _bzRuur8REd6XgrpwHbbsYQ
		 */
		protected var lastTargetEditPartAccepted:EditPart = null;
		
		/**
		 * The actual figure obtained from editPart used as a placeHolder
		 * @flowerModelElementId _bzRuv78REd6XgrpwHbbsYQ
		 */
		protected var connectionFigure:IFigure;
		
		/**
		 * If the mouseDown happened on a acceptingSource.
		 * Usefull in mouseMove to know if the origination move was over
		 * an acceptingSource or not.
		 * @flowerModelElementId _bza4o78REd6XgrpwHbbsYQ
		 */
		protected var mouseWasDownOnAcceptingSource:Boolean = false;
		
		/**
		 * Remove handlers
		 * @flowerModelElementId _bza4rr8REd6XgrpwHbbsYQ
		 */
		override public function deactivate():void {
			// destroy leftovers - if any
			resetEditPartAndFigure();
			
			super.deactivate();
		}
		
		/** We send this function as a parameter to the utility function 
		 * <code>getEditPartFromDisplayCoordinates()</code> in order for this method 
		 * to exclude that figures that are ConnectionFigure, because on drag
		 * the connection will be under mouse (sometimes)
		 */ 
		public function IFigureMeetsAdditionalConditions(disp:IFigure):Boolean {
			return disp != connectionFigure;
		}
		
		/**
		 * Utility function that returns the first EditPart
		 * that accepts our connection editPart
		 * @flowerModelElementId _bza4ur8REd6XgrpwHbbsYQ
		 */
		protected function getConnectionAcceptedEditPart():EditPart {
			
			// local variable so we don't accidentally
			// overwrite targetEditPart
			var localEditPart:EditPart = targetEditPart;
			
			while (localEditPart != null) {
				if (checkSourceOrDestination && localEditPart.isSourceConnectionEditPartAccepted(newEditPart)) {
					return localEditPart;
				} else if (!checkSourceOrDestination && localEditPart.isTargetConnectionEditPartAccepted(newEditPart)) {
					targetAccepted = true;
					return localEditPart;
				}
				localEditPart = localEditPart.getParent();
			}
			
			return null;
		}
		
		/**
		 * Update the cursor icon based on the target figure 
		 * under mouse that can/cannot accept the selected figures.
		 * 
		 * It also sets the internal state variable lastTargetEditPartAccepted
		 * to the recursively found accepting EditPart
		 * @flowerModelElementId _bza4v78REd6XgrpwHbbsYQ
		 */
		override protected function checkTargetAndUpdateCursor():void {
			if (targetEditPart != null) {
				lastTargetEditPartAccepted = getConnectionAcceptedEditPart();
				if (lastTargetEditPartAccepted != null) {
					// operation allowed 
					updateCursor(copyCursor);
				} else {
					// operation denied
					updateCursor(rejectCursor);
				}
			} else { // we cannot obtain a editPart from the element under cursor
				// reset the cursor
				updateCursor(null);
			}
		}		
		
		/**
		 * Computes and sets the starting point(_sourcePoint) of the ConnectionFigure to be rendered.
		 * By default, this point is the center of the Figure on which the Connection starts.
		 * 
		 * @parameter upperLeftCorner - the upper left corner of the Figure currently being connected.
		 * @parameter figureBounds - the bounds of the Figure currently being connected.
		 * 
		 * Given also the position of the mouse at the Connection start, some specialized tools might 
		 * want to keep one of these coordinates fixed instead of computing the center of the figure.
		 */ 
		protected function updateConnectionSourcePoint(upperLeftCorner:Point, figureBounds:Rectangle, mousePositionX:Number, mousePositionY:Number):void {
			ConnectionFigure(connectionFigure)._sourcePoint.x = upperLeftCorner.x + figureBounds.width / 2;
			ConnectionFigure(connectionFigure)._sourcePoint.y = upperLeftCorner.y + figureBounds.height / 2;
		}
		
		protected function updateConnectionTargetPoint(mousePositionX:Number, mousePositionY:Number):void {
			var mousePoint:Point = globalToDiagram(mousePositionX, mousePositionY);
			ConnectionFigure(connectionFigure)._targetPoint.x = mousePoint.x;
			ConnectionFigure(connectionFigure)._targetPoint.y = mousePoint.y;
		}
				
		/**
		 * Handler for mouseMove
		 * 
		 * Updates the mouse (accept - green, deny - red) depending on 
		 * the EditPart under the mouse accepting the line or not
		 * @flowerModelElementId _bza4xL8REd6XgrpwHbbsYQ
		 */
		override protected function mouseMoveHandler(event:MouseEvent):void {
			if (targetObject != event.target) {
				targetObject = event.target;
				targetEditPart = getEditPartFromDisplayCoordinates(event.stageX, event.stageY, IFigureMeetsAdditionalConditions);
				checkTargetAndUpdateCursor();
			}
			if (event.buttonDown) {
				if (mouseWasDownOnAcceptingSource) {
					if (ConnectionEditPart(editPart).getSource() == null) {
						// first time move -> check to see if it is accepted 
						if (lastTargetEditPartAccepted != null) {
							connectionFigure = editPart.createOrGetRecycledFigure();
							connectionFigure.setEditPart(editPart);
							ConnectionEditPart(editPart).setConnectionVisualProperties(connectionFigure);
							AbsolutePositionEditPartUtils.addChildFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), IVisualElement(connectionFigure));
							var p:Point;
							var rect:Rectangle;
							if (lastTargetEditPartAccepted.getFigure() is ConnectionFigure) {
								var array:Array = ConnectionEditPart(lastTargetEditPartAccepted).getConnectionAnchorRect();
								p = new Point(array[0], array[1]);
								rect = new Rectangle();
							} else {
								var actualContainer:Container = Container(AbsolutePositionEditPartUtils.getActualFigure(Container(diagramViewer.getRootFigure())));
        						rect = Rectangle(DisplayObject(lastTargetEditPartAccepted.getFigure()).getBounds(actualContainer));
        						// convert coordinates to take scroll into account
        						p = actualContainer.localToContent(rect.topLeft);
							}
							
							updateConnectionSourcePoint(p, rect, event.stageX, event.stageY);
							
							// start from the same point (applies especially to recycled/reused connectionFigure
							// that have a dimension already) 
							ConnectionFigure(connectionFigure)._targetPoint.x = ConnectionFigure(connectionFigure)._sourcePoint.x; 
							ConnectionFigure(connectionFigure)._targetPoint.y = ConnectionFigure(connectionFigure)._sourcePoint.y; 
							
							ConnectionEditPart(editPart).setSource(lastTargetEditPartAccepted);
							
							// now check for tail end
							checkSourceOrDestination = false;
						} 
					} else {
						// second time update connection end coordinates
						updateConnectionTargetPoint(event.stageX, event.stageY);
					}
				}
			}
			else {
				// if the mouse reenters
				if (ConnectionEditPart(editPart).getSource() != null) {
					// reset editPart and figure
					resetEditPartAndFigure();
				}
			}
		}
		
		/**
		 * Handler for mouseUp
		 * 
		 * Updates de mouse cursor based on the EventPart under
		 * the mouse.
		 * Adds the connection to the model and reset the internal
		 * state.
		 * 
		 * It delegate to <code>EditPart</code> the creation of the new edit part
		 * 
		 * @author Cristi
		 * @flowerModelElementId _bza4yr8REd6XgrpwHbbsYQ
		 */
		override protected function mouseUpHandler(event:MouseEvent):void {
			if (targetObject != event.target) {
				targetObject = event.target;
				targetEditPart = getEditPartFromDisplayCoordinates(event.stageX, event.stageY);
			}
			checkTargetAndUpdateCursor();
			
			// because we have overridden mouseDownHandler we need to
			// do this here
			dragInProgress = false; 
			
			if (ConnectionEditPart(editPart).getSource() != null) {
				var x:Number = -1;
				var y:Number = -1;
				// remove figure from diagram
				if (connectionFigure != null) {
					AbsolutePositionEditPartUtils.removeChildFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), IVisualElement(connectionFigure));
					x = ConnectionFigure(connectionFigure)._sourcePoint.x;
					y = ConnectionFigure(connectionFigure)._sourcePoint.y;
					connectionFigure = null;
				}
				if (lastTargetEditPartAccepted != null && targetAccepted) {
					ConnectionEditPart(editPart).setTarget(lastTargetEditPartAccepted);
					// create the new connection defined by editPart
					var currentTargetEditPart:EditPart = targetEditPart;
					targetEditPart = diagramViewer.getRootEditPart();
					callAcceptNewEditPart(editPart, x, y, -1, -1);
					targetEditPart = currentTargetEditPart;
				}
			}
			
			mouseWasDownOnAcceptingSource = false;
			lastTargetEditPartAccepted = null;
			// check source head again
			checkSourceOrDestination = true;
			
		}
		
		/**
		 * If the mouse was down on a connection accepting EditPart, 
		 * notify that, so that MouseMove knows that a drag has started.
		 * @flowerModelElementId _bza40L8REd6XgrpwHbbsYQ
		 */
		override protected function mouseDownHandler(event:MouseEvent):void {
			// because we have overridden mouseDownHandler we need to
			// do this here
			dragInProgress = true;
			 
			targetAccepted = false;
			if (targetObject != event.target) {
				targetObject = event.target;
				targetEditPart = getEditPartFromDisplayCoordinates(event.stageX, event.stageY, IFigureMeetsAdditionalConditions);
				checkTargetAndUpdateCursor();
			}
			if (lastTargetEditPartAccepted != null) {
				mouseWasDownOnAcceptingSource = true;
			}
		}
		
		/**
		 * On Esc cancel the whole thing
		 * @flowerModelElementId _bza41r8REd6XgrpwHbbsYQ
		 */
		override protected function keyDownHandler(event:KeyboardEvent):void {
			super.keyDownHandler(event);
			// check if Esc is pressed and if we have what to cancel
			if (event.keyCode == Keyboard.ESCAPE && ConnectionEditPart(editPart).getSource() != null) {
				resetEditPartAndFigure();
			}
		}
		
		/**
		 * Utility function that resets the editPart source/target
		 * and removes connectionFigure from diagram
		 * @flowerModelElementId _bzkpo78REd6XgrpwHbbsYQ
		 */
		protected function resetEditPartAndFigure():void {
			// remove figure from diagram
			if (connectionFigure != null) {
				AbsolutePositionEditPartUtils.removeChildFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), IVisualElement(connectionFigure));
				connectionFigure = null;
			}
			
			ConnectionEditPart(editPart).setSource(null);
			ConnectionEditPart(editPart).setTarget(null);
			
			mouseWasDownOnAcceptingSource = false;
			lastTargetEditPartAccepted = null;
			// check source head again
			checkSourceOrDestination = true;			
		}		
	}
}