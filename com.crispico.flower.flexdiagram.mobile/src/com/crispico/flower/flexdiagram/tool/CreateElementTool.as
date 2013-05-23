package com.crispico.flower.flexdiagram.tool {
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	
	import flash.events.MouseEvent;
	import flash.geom.Point;
	
	import mx.collections.ArrayCollection;
	import mx.core.UIComponent;
	import mx.managers.CursorManager;
	

	/**
	 * Generic tool for element creation.
	 * 
	 * Responsible with changing the cursor based on the posibility of creating
	 * an element.
	 * 
	 * Subclasses are responsible to implement <code>createEditPart</code>
	 * 
	 * @author mircea
	 * @flowerModelElementId _by-MyL8REd6XgrpwHbbsYQ
	 */
	public class CreateElementTool extends Tool {
		
		/**
		 * The current editPart under the mouse
		 * which is interogated about the acceptance
		 * @flowerModelElementId _bzkprb8REd6XgrpwHbbsYQ
		 */
		protected var targetEditPart:EditPart;
		
		/**
		  * The cache element under cursor, so that we do not
		  * recursivelly get the edit part under cursor for every
		  * mouseMove
		  * @flowerModelElementId _bzkpsb8REd6XgrpwHbbsYQ
		  */
		 protected var targetObject:Object = null;
		
		// Usefull for isEditPartAccepted which requires an ArrayCollection
		// so we don't create an ArrayCollection every time
		/**
		 * @flowerModelElementId _bzkptr8REd6XgrpwHbbsYQ
		 */
		protected var newEditPart:ArrayCollection = new ArrayCollection();
		
		/**
		 * Constructor
		 * @flowerModelElementId _bzkpur8REd6XgrpwHbbsYQ
		 */
		public function CreateElementTool() {
			super();
		}
		
		/**
		 * Abstract method.
		 * 
		 * Creates the "dummy" <code>EditPart</code> which is checked by
		 * the following methods:
		 * <ul>
		 * 		<li>EditPart.isEditPartAccepted</li>
		 * 		<li>EditPart.isSourceConnectionEditPartAccepted</li>
		 * 		<li>EditPart.isTargetConnectionEditPartAccepted</li>
		 * 		<li>EditPart.acceptNewEditPart</li>
		 * </ul>
		 * It is also used to create the connection figure on the diagram
		 * (in <code>CreateConnectionTool</code>
		 * Those method bases their check on the type of the dummy 
		 * EditPart.
		 * 
		 * Subclasses are responsible to create the kind of EditPart
		 * they need.
		 * @flowerModelElementId _bzkpvr8REd6XgrpwHbbsYQ
		 */
		protected function createEditPart():void {
			throw new Error("createEditPart() should be implemented.");
		}
		
		/**
		 * Adds handlers for mouseDown, mouseMove, mouseUp
		 * 
		 * @author Cristi
		 * @flowerModelElementId _bzkpw78REd6XgrpwHbbsYQ
		 */
		override public function activate(diagramViewer:DiagramViewer):void {
			super.activate(diagramViewer);
			// create the "dummy" EditPart.
			createEditPart();
			newEditPart.addItem(editPart);
		}
		
		/**
		 * Remove handlers
		 * 
		 * @author Cristi
		 * @flowerModelElementId _bzkpyb8REd6XgrpwHbbsYQ
		 */
		override public function deactivate():void {
			// set the default cursor
			UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors();
			editPart = null;
			super.deactivate();
		}
		
		/**
		 * Display the accept cursor (green) or remove cursor (red)
		 * depending on where it's position (over an accepting EditPart
		 * or an rejecting EditPart)
		 * @flowerModelElementId _bztzl78REd6XgrpwHbbsYQ
		 */
		override protected function mouseMoveHandler(event:MouseEvent):void {
			super.mouseMoveHandler(event);
			if (!event.buttonDown) {
				// get the editPart under the cursor
				if (targetObject != event.target) {
					targetObject = event.target;			
					targetEditPart = getEditPartFromDisplay(event.target);
				}
				// update cursor
				checkTargetAndUpdateCursor();
			}
		}
		
		/**
		 * Tells the target's EditPart that it should create a 
		 * new figure. 
		 * @flowerModelElementId _bz3klL8REd6XgrpwHbbsYQ
		 */
		override protected function mouseDownHandler(event:MouseEvent):void {
			super.mouseDownHandler(event);
			// on click on an accepted EditPart
			// give it the control
			if (targetObject != event.target) {
				targetObject = event.target;
				targetEditPart = getEditPartFromDisplay(event.target);
			}
			if (targetEditPart != null && targetEditPart.isEditPartAccepted(newEditPart)) {
				var mousePoint:Point = globalToDiagram(event.stageX, event.stageY);
				
				// because is not absolute -> width/height are not set
				callAcceptNewEditPart(editPart, mousePoint.x, mousePoint.y, -1, -1);
			}
		}
		
		/**
		 * Responsible with setting the cursor after the mouse is released
		 * 
		 * @flowerModelElementId _b0BVkb8REd6XgrpwHbbsYQ
		 */
		override protected function mouseUpHandler(event:MouseEvent):void {
			super.mouseUpHandler(event);
			if (targetObject != event.target) {
				targetObject = event.target;
				targetEditPart = getEditPartFromDisplay(event.target);
			}
			// update cursor based on the targetEditPart
			checkTargetAndUpdateCursor();	
		}
		
		/**
		 * Update the cursor icon based on the target figure 
		 * under mouse that can/cannot accept the selected figures.
		 * @flowerModelElementId _b0BVl78REd6XgrpwHbbsYQ
		 */
		protected function checkTargetAndUpdateCursor():void { 
			if (targetEditPart != null) {
				if (targetEditPart.isEditPartAccepted(newEditPart))// operation allowed
					updateCursor(copyCursor);
				else // operation denied
					updateCursor(rejectCursor);
			} else { // we cannot obtain a editPart from the element under cursor
				// reset cursor
				updateCursor(null);
			}
		}
		
		/**
		 * Calls acceptEditPart on this targetEditPart.
		 * To be overriden by subclasses who add additionalInfo 
		 * on the EditPart.
		 * 
		 * @flowerModelElementId _l2VzQBvQEd-uZOXubcdgVw
		 */
		public function callAcceptNewEditPart(editPart:EditPart, x:int, y:int, width:int, height:int, additionalInfo:Object=null) : void {
			targetEditPart.acceptNewEditPart(editPart, x, y, width, height, additionalInfo);
			jobFinished();
		}
		
		/**
		 * @flowerModelElementId _SFWpAETOEeCbnenRcrXIzw
		 */
		override public function canLock():Boolean {
			return true;
		}

	}
}