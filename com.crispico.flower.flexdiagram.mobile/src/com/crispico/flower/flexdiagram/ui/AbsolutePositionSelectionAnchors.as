package  com.crispico.flower.flexdiagram.ui {
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.tool.SelectMoveResizeTool;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.events.MoveEvent;
	import mx.events.ResizeEvent;
	/**
	 * 
	 * A component that displays 8 resize anchors and 2 relation anchors. It is intended to be used to display
	 * the selected state of a <code>IAbsolutePositionEditPart</code>.
	 *
	 * <p>
	 * A "target" figure is provided (in <code>activate()</code>). It "surrounds"
	 * the figure (by using the figure's coordinates) and it listens to move or resize 
	 * events (in order to update itself). When no longer needed, <code>
	 * deactivate()</code> should be called.
	 *
	 * <p>
	 * Note: this component should be a sibbling of the target figure (from the Flex 
	 * UIComponent perspective) and the 2 of them should be placed in
	 * a component having absolute layout.
	 *
	 * @author Crist
	 * @flowerModelElementId _b1Noar8REd6XgrpwHbbsYQ
	 */
	public class AbsolutePositionSelectionAnchors extends AbstractSelectionAnchors {
		
		/**
		 * The mouse cursor shapes when the mouse is over an anchor
		 */
		[Embed(source="/icons/diag1Cursor.gif")]
        [Bindable]
		protected var diag1Cursor:Class;
 
		/**
		 * @flowerModelElementId _b1WyV78REd6XgrpwHbbsYQ
		 */
		[Embed(source="/icons/diag2Cursor.gif")]
        [Bindable]
		protected var diag2Cursor:Class;

		[Embed(source="/icons/horizCursor.gif")]
        [Bindable]
		protected var horizCursor:Class;

		[Embed(source="/icons/vertCursor.gif")]
        [Bindable]	
		protected var vertCursor:Class;
		
		/**
		 * 
		 * The list of those 8 anchor
		 * @flowerModelElementId _b1WyYL8REd6XgrpwHbbsYQ
		 */
		protected var anchorLeftUp:ResizeAnchor;
		/**
		 * @flowerModelElementId _b1WyZb8REd6XgrpwHbbsYQ
		 */
		protected var anchorMiddleUp:ResizeAnchor;
		/**
		 * @flowerModelElementId _b1WyaL8REd6XgrpwHbbsYQ
		 */
		protected var anchorRightUp:ResizeAnchor;
		/**
		 * @flowerModelElementId _b1Wya78REd6XgrpwHbbsYQ
		 */
		protected var anchorRightMiddle:ResizeAnchor;
		/**
		 * @flowerModelElementId _b1Wybr8REd6XgrpwHbbsYQ
		 */
		protected var anchorRightDown:ResizeAnchor;
		/**
		 * @flowerModelElementId _b1Wycb8REd6XgrpwHbbsYQ
		 */
		protected var anchorMiddleDown:ResizeAnchor;
		/**
		 * @flowerModelElementId _b1WydL8REd6XgrpwHbbsYQ
		 */
		protected var anchorLeftDown:ResizeAnchor;
		/**
		 * @flowerModelElementId _b1Wyd78REd6XgrpwHbbsYQ
		 */
		protected var anchorLeftMiddle:ResizeAnchor;
		
		/**
		 * @flowerModelElementId _tt0QwDnAEeCd8NTSFM7DsQ
		 */
		protected var connectionAnchorUp:RelationAnchor;
		
		/**
		 * @flowerModelElementId _tti0gEDSEeCpzKtyrWusGA
		 */
		protected var connectionAnchorDown:RelationAnchor;
		
		protected var anchorsToUse:int;
		
		public static const ALL_RESIZE_ANCHORS:int = 0x00FF; // binary value: 0000 1111 1111 (we have 8 anchors)
		
		public static const ALL_RELATION_ANCHORS:int = 0x0300; // binary value: 0011 0000 0000
		
		/**
		 * Constants used for the <code>ResizeAnchors</code> type
		 * @flowerModelElementId _b1Wyer8REd6XgrpwHbbsYQ
		 */
		public static const LEFT_UP:String = "LEFT_UP";
		/**
		 * @flowerModelElementId _b1Wyf78REd6XgrpwHbbsYQ
		 */
		public static const MIDDLE_UP:String = "MIDDLE_UP";
		/**
		 * @flowerModelElementId _b1Wyg78REd6XgrpwHbbsYQ
		 */
		public static const RIGHT_UP:String = "RIGHT_UP";
		/**
		 * @flowerModelElementId _b1Wyh78REd6XgrpwHbbsYQ
		 */
		public static const RIGHT_MIDDLE:String = "RIGHT_MIDDLE";
		/**
		 * @flowerModelElementId _b1Wyi78REd6XgrpwHbbsYQ
		 */
		public static const RIGHT_DOWN:String = "RIGHT_DOWN";
		/**
		 * @flowerModelElementId _b1Wyj78REd6XgrpwHbbsYQ
		 */
		public static const MIDDLE_DOWN:String = "MIDDLE_DOWN";
		/**
		 * @flowerModelElementId _b1gjU78REd6XgrpwHbbsYQ
		 */
		public static const LEFT_DOWN:String = "LEFT_DOWN";
		/**
		 * @flowerModelElementId _b1gjV78REd6XgrpwHbbsYQ
		 */
		public static const LEFT_MIDDLE:String = "LEFT_MIDDLE";
		
		/**
		 * 
		 * Constructor.
		 * 
		 * It also creates the 8 anchors, and adds them as child. Parameter anchorsToUse
		 * can be specified using ALL_RESIZE_ANCHORS and ALL_RELATION_ANCHORS constants.
		 * Implementation does not interpret the anchorsToUse as a binary value, but in future
		 * it might be modified to support other combinations of anchors.   
		 * 
		 * @flowerModelElementId _b1gjW78REd6XgrpwHbbsYQ
		 */
		public function AbsolutePositionSelectionAnchors(anchorsToUse:int = 0x03FF) {
			this.anchorsToUse = anchorsToUse;
			createAndAddActiveAnchors();						
		}
		
		/**
		 * @flowerModelElementId __TXtoJ-uEd-Acq1dtkSbHA
		 */
		protected function createAndAddActiveAnchors():void {
			if ((anchorsToUse & ALL_RESIZE_ANCHORS) == ALL_RESIZE_ANCHORS) {
				anchorLeftUp = new ResizeAnchor(LEFT_UP);
				this.addChild(anchorLeftUp);
				
				anchorMiddleUp = new ResizeAnchor(MIDDLE_UP);
				this.addChild(anchorMiddleUp);
				
				anchorRightUp = new ResizeAnchor(RIGHT_UP);
				this.addChild(anchorRightUp);
				
				anchorRightMiddle = new ResizeAnchor(RIGHT_MIDDLE);
				this.addChild(anchorRightMiddle);
				
				anchorRightDown = new ResizeAnchor(RIGHT_DOWN);
				this.addChild(anchorRightDown);
				
				anchorMiddleDown = new ResizeAnchor(MIDDLE_DOWN);
				this.addChild(anchorMiddleDown);
				
				anchorLeftDown = new ResizeAnchor(LEFT_DOWN);
				this.addChild(anchorLeftDown);
				
				anchorLeftMiddle = new ResizeAnchor(LEFT_MIDDLE);
				this.addChild(anchorLeftMiddle);
			}
			
			if ((anchorsToUse & ALL_RELATION_ANCHORS) == ALL_RELATION_ANCHORS) {
				connectionAnchorUp = new RelationAnchor();
				this.addChild(connectionAnchorUp);
				
				connectionAnchorDown = new RelationAnchor();
				this.addChild(connectionAnchorDown);
			}
		}
		
		/**
		 * 
		 * Called first after creation.
		 * Responsable for creating internal state of this component.
		 * <ul>
		 * 		<li>Creates <code>ResizeAnchors</code> list<li>
		 * 		<li>Sets the <code>EditPart</code> for every <code>ResizeAnchors</code></li>
		 * </ul
		 * @flowerModelElementId _b1gjX78REd6XgrpwHbbsYQ
		 */
		override public function activate(target:IFigure):void {
			super.activate(target);			
			
			// set the handler that move/resize anchors with parent figure.
			DisplayObject(target).addEventListener(MoveEvent.MOVE, handleTargetMoveResize); 
			DisplayObject(target).addEventListener(ResizeEvent.RESIZE, handleTargetMoveResize);
			DisplayObject(target.getEditPart().getViewer().getRootEditPart().getFigure()).addEventListener(MouseEvent.MOUSE_OVER, changeCursor);

			// update anchors position
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
			DisplayObject(target.getEditPart().getViewer().getRootEditPart().getFigure()).removeEventListener(MouseEvent.MOUSE_OVER, changeCursor);
			
			super.deactivate();
		}
		
		/**
		 * 
		 * Setter for main selection.
		 * 
		 * Also updates all the <code>ResizeAnchor</code>
		 * @flowerModelElementId _b1gjar8REd6XgrpwHbbsYQ
		 */
		override public function setMainSelection(value:Boolean):void {
			
			var oldValue:Boolean = isMainSelection;
			
			isMainSelection = value;
			
			if (oldValue != value) {
				// announce ResizeAnchors that the value has been modified
				invalidateActiveAnchors();
			}
		}
		
		/**
		 * Announce active ResizeAnchors that they changed and need to refresh.
		 * @flowerModelElementId __TYUsJ-uEd-Acq1dtkSbHA
		 */ 
		protected function invalidateActiveAnchors():void {
			if ((anchorsToUse & ALL_RESIZE_ANCHORS) == ALL_RESIZE_ANCHORS) {
				anchorLeftUp.invalidateDisplayList();
				anchorMiddleUp.invalidateDisplayList(); 
				anchorRightUp.invalidateDisplayList();
				anchorRightMiddle.invalidateDisplayList();
				anchorRightDown.invalidateDisplayList();
				anchorMiddleDown.invalidateDisplayList();
				anchorLeftDown.invalidateDisplayList();
				anchorLeftMiddle.invalidateDisplayList();
			}
			if ((anchorsToUse & ALL_RELATION_ANCHORS) == ALL_RELATION_ANCHORS) {
				connectionAnchorUp.invalidateDisplayList();
				connectionAnchorDown.invalidateDisplayList();
			}
		}
		
		/**
		 * Returns an array with the bounds of the figure on which the anchors are placed.
		 * By default this figure returns the bounds of the entire UIComponent target.
		 * 
		 * Subclasses can override this method in order to place the anchors on some other 
		 * rectangle. For example it can be used for complex figures that have more subcomponents
		 * for putting the anchors on a specific one. 
		 */
		protected function getTargetBounds():Array {
			return [DisplayObject(target).x, DisplayObject(target).y, DisplayObject(target).width, DisplayObject(target).height];
		}
		
		/**
		 * Event handler called when the figure, where the anchors are displayed,
		 * has moved.
		 * 
		 * Recomputes the position for every anchor, based on the figure
		 * coordinates.
		 * @flowerModelElementId _b1gjcL8REd6XgrpwHbbsYQ
		 */
		protected function handleTargetMoveResize(event:Event):void {
			// get figure coordinates
			var boundsArray:Array = getTargetBounds();
			
			if ((anchorsToUse & ALL_RESIZE_ANCHORS) == ALL_RESIZE_ANCHORS) { 
				// update position for every anchor			
				anchorLeftUp.x = boundsArray[0];
				anchorLeftUp.y = boundsArray[1];
							
				anchorMiddleUp.x = boundsArray[0] + int(boundsArray[2] / 2);
				anchorMiddleUp.y = boundsArray[1];
				
				anchorRightUp.x = boundsArray[0] + boundsArray[2];
				anchorRightUp.y = boundsArray[1];
										
				anchorRightMiddle.x = boundsArray[0] + boundsArray[2];
				anchorRightMiddle.y = boundsArray[1] + int(boundsArray[3] / 2);
							
				anchorRightDown.x = boundsArray[0] + boundsArray[2];
				anchorRightDown.y = boundsArray[1] + boundsArray[3];
							
				anchorMiddleDown.x = boundsArray[0] + int(boundsArray[2] / 2);
				anchorMiddleDown.y = boundsArray[1] + boundsArray[3];
							
				anchorLeftDown.x = boundsArray[0];
				anchorLeftDown.y = boundsArray[1] + boundsArray[3];
							
				anchorLeftMiddle.x = boundsArray[0];
				anchorLeftMiddle.y = boundsArray[1] + int(boundsArray[3] / 2);
			}
			if ((anchorsToUse & ALL_RELATION_ANCHORS) == ALL_RELATION_ANCHORS) {
				var quarterX:Number = boundsArray[0] + int(4 * boundsArray[2] / 5);
				
				connectionAnchorUp.x = quarterX - int(connectionAnchorUp.width / 2);
				connectionAnchorUp.y = boundsArray[1] - 16; // consider the height of the anchor
				
				connectionAnchorDown.x = quarterX - int(connectionAnchorDown.width / 2);
				connectionAnchorDown.y = boundsArray[1] + boundsArray[3] + 2; // add an offset so the anchor would not cover the figure edge
			}
			invalidateActiveAnchors();			
		}
		 
		/**
		 * 
		 * The function updates the cursor when the mouse is over a resize 
		 * anchor.
		 * @flowerModelElementId _b1gjdr8REd6XgrpwHbbsYQ
		 */
		protected function changeCursor(event:MouseEvent):void {
			var currentCursor:Class;
			if (target.getEditPart().getViewer().getActiveTool() is SelectMoveResizeTool) {
				// modify cursor only if the current tool is SeletMoveResizeTool
				if (event.target is ResizeAnchor) {// the object under mouse is a ResizeAnchor
					if (DisplayObject(event.target).parent is AbsolutePositionSelectionAnchors) { // if the anchor belongs to AbsolutePositionSelectionAnchors
						switch (ResizeAnchor(event.target).getType()) {
							case LEFT_DOWN:
								currentCursor = diag2Cursor;
								break;
							case LEFT_MIDDLE:
								currentCursor = horizCursor;
								break;
							case LEFT_UP:
								currentCursor = diag1Cursor;
								break;
							case MIDDLE_DOWN:
								currentCursor = vertCursor;
								break;
							case MIDDLE_UP:
								currentCursor = vertCursor;
								break;
							case RIGHT_DOWN:
								currentCursor = diag1Cursor;
								break;
							case RIGHT_MIDDLE:
								currentCursor = horizCursor;
								break;
							case RIGHT_UP:
								currentCursor = diag2Cursor;
								break;
						}
						cursorManager.removeAllCursors();
						cursorManager.setCursor(currentCursor, 2, -16, -16);// center the icon's image (32x32) over the anchor
					}
				} else { // the object under mouse is not a ResizeAnchor
					if (target.getEditPart() != null)
						// there is no drag&drop operation in progress otherwise removing cursor will cause flicker
						var smrTool:SelectMoveResizeTool = SelectMoveResizeTool(target.getEditPart().getViewer().getActiveTool());
						if (!smrTool.isDragInProgress() && !smrTool.moveCursorVisible) {
							cursorManager.removeAllCursors();
						}
				}
			}
		}
	}
}