package com.crispico.flower.flexdiagram.tool {
	
	import com.crispico.flower.flexdiagram.AbsoluteLayoutEditPart;
	import com.crispico.flower.flexdiagram.AbsolutePositionEditPartUtils;
	import com.crispico.flower.flexdiagram.DiagramContent;
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IAbsolutePositionEditPart;
	import com.crispico.flower.flexdiagram.IConnectableEditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.IScrollableEditPart;
	import com.crispico.flower.flexdiagram.RectangularGrid;
	import com.crispico.flower.flexdiagram.RootFigure;
	import com.crispico.flower.flexdiagram.action.ActionContext;
	import com.crispico.flower.flexdiagram.action.IAction;
	import com.crispico.flower.flexdiagram.connection.ConnectionEditPart;
	import com.crispico.flower.flexdiagram.connection.ConnectionLabelEditPart;
	import com.crispico.flower.flexdiagram.contextmenu.ActionEntry;
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuManager;
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuUtils;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	import com.crispico.flower.flexdiagram.ui.ConnectionFigure;
	import com.crispico.flower.flexdiagram.ui.ConnectionSegment;
	import com.crispico.flower.flexdiagram.ui.IDraggable;
	import com.crispico.flower.flexdiagram.ui.MoveResizePlaceHolder;
	import com.crispico.flower.flexdiagram.ui.RelationAnchor;
	import com.crispico.flower.flexdiagram.ui.ResizeAnchor;
	
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.geom.Point;
	import flash.geom.Rectangle;
	import flash.ui.Keyboard;
	
	import mx.collections.ArrayCollection;
	import mx.core.Application;
	import mx.core.Container;
	import mx.core.IVisualElement;
	import mx.core.IVisualElementContainer;
	import mx.core.UIComponent;
	import mx.managers.IFocusManagerComponent;
	
	import org.alivepdf.visibility.Visibility;
	
	import spark.components.Group;
	
	/**
	 * This class handles selection related logic for diagrams.
	 * 
	 * <p>
 	 * This tool supports "drag to select" and "drag to create" operations.
 	 * "drag to create" operation means that the user drags/draws a rectangle
 	 * on the screen as if he/she would want to select something. If the selection 
 	 * rectangle contains elements, it is in "drag to select" mode (default color:
 	 * blue) and it behaves as its names suggests
 	 * (i.e. selects elements). Otherwise (i.e. the rectangle doesn't contain elements), its 
 	 * color is green (by default) and it means that it is in "drag to create" mode. When the mouse is
 	 * released (in this mode, i.e. green rectangle), 
 	 * <code>AbstractGanttDiagramFigure.fillCreateContextMenu()</code> is invoked (that probably
 	 * adds some entries/actions that create new elements). 
	 * 
	 * @internal
	 * The tool that handles the move/resize events.
	 * 
	 * <p>While this tool is active the context menu is allowed to be shown on the diagram.
	 * 
	 * @see com.crispico.flower.flexdiagram.gantt.figure.GanttDiagramFigure#figureSelectableFromUIFunction
	 * @author Georgi
	 * @author Sorin
	 * @author Luiza
	 * 
	 * @flowerModelElementId _b0eBgL8REd6XgrpwHbbsYQ
	 */
	public class SelectMoveResizeTool extends Tool {
				
		/**
		 * @private
		 * 
		 * Denotes the IDraggable object that generated the mouse move event. 
		 * @flowerModelElementId _b0eBhb8REd6XgrpwHbbsYQ
		 */ 		
		protected var currentDraggable:IDraggable;
				
		/**
		 * @flowerModelElementId _b0eBiL8REd6XgrpwHbbsYQ
		 */
		protected var callDragAndDropOverEditPartTarget:Boolean;
		
		/**
		 * 
		 * @private
		 * @flowerModelElementId _b0eBi78REd6XgrpwHbbsYQ
		 */
		protected var deltaX:int; // deltaX = the difference on the x axis between the current position and start position
			
		
		/**
		 * 
		 * @private
		 * @flowerModelElementId _b0eBjr8REd6XgrpwHbbsYQ
		 */
		protected var deltaY:int; // deltaY = the difference on the y axis between the current position and start position
		
		//initial values 
		
		/**
		 * 
		 * @private
		 * @flowerModelElementId _b0eBkb8REd6XgrpwHbbsYQ
		 */
		protected var startX:int;
		
		/**
		 * 
		 * @private
		 * @flowerModelElementId _b0eBlL8REd6XgrpwHbbsYQ
		 */
		protected var startY:int;
		
		/**
		 * @private
		 * Keeps the object over which the mouse operates.
		 */ 
		protected var mouseDownTarget:DisplayObject = null;
		
		/**
		 * @private
		 * Keeps the IDraggable object that generated the mouse down event
		 * if the same object generates a mouse move event => a drag operation is allowed	
		 * 
		 * @flowerModelElementId _b0eBl78REd6XgrpwHbbsYQ
		 */
		protected var mouseDownDraggable:IDraggable = null;
		
		// Tells if the mouse was down on the diagram
		// Set in mouseDown on diagram, unset on mouseUp
		/**
		 * @flowerModelElementId _b0eBm78REd6XgrpwHbbsYQ
		 * @private
		 */
		protected var mouseWasDownOnDiagram:Boolean = false;
		
		// selection rectangle used for multiple selection
		/**
		 * @flowerModelElementId _b0eBn78REd6XgrpwHbbsYQ
		 * @private
		 */
		protected var multipleSelectionRectangle:MoveResizePlaceHolder = new MoveResizePlaceHolder();

		/**
		 * @private
		 * Connection place holder when creating relations in DRAG_TO_CREATE_RELATION state
		 * 
		 * @author Luiza
		 * @flowerModelElementId _6JZeQjqdEeCcnaPb1Tzsvw
		 */ 
		protected var connectionPlaceHolder:ConnectionFigure = new ConnectionFigure();
				
		/**
		 * @private
		 * EditPart used as target for drop operation. Also as target for relation creations.
		 * 
		 * @flowerModelElementId _b0eBo78REd6XgrpwHbbsYQ
		 */
		protected var targetEditPart:EditPart = null;
		
		/**
		 * @private
		 * EditPart used as source when creating relations.
		 * 
		 * @author Luiza
		 * @flowerModelElementId _6JZeQDqdEeCcnaPb1Tzsvw
		 */ 
		private var sourceEditPart:EditPart = null;
		
		// Notify the reset selection mechanism from mouseUpHandler, not to do anything
		// set by  creating multiple selection with CTRL|SHIFT aborting drag&drop operation
		/**
		 * @flowerModelElementId _b0eBp78REd6XgrpwHbbsYQ
		 * @private
		 */
		protected var dontResetSelectionOnMouseUp:Boolean = false;
		
		/**
		 * Provides information about the Tool status. This Tool can be in one of the following states:
		 * 
		 * <ul>
		 * 	<li>NORMAL</li>
		 * 	<li>SHIFT_PRESSED</li>
		 * 	<li>PAN_DRAG</li>
		 *  <li>DRAG_TO_CREATE_ELEMNT</li>
		 *  <li>DRAG_TO_SELECT_ELEMENT</li>
		 *  <li>DRAG_TO_CREATE_RELATION</li>
		 * </ul>
		 * @private
		 * @flowerModelElementId _jYq3wn9gEd6eOdiqTgIdyg
		 */
		protected var state:int;
		
		/**
		 * Provides value for NORMAL state.
		 * @private
		 * @flowerModelElementId _jYq3w39gEd6eOdiqTgIdyg
		 */
		public const NORMAL:int = 0;
		
		/**
		 * Provides value for SHIFT_PRESSED state.
		 * @private
		 * @flowerModelElementId _jYq3xH9gEd6eOdiqTgIdyg
		 */
		public const SHIFT_PRESSED:int = 1;
		
		/**
		 * Provides value for PAN_DRAG state.
		 * @private
		 * @flowerModelElementId _jYq3xX9gEd6eOdiqTgIdyg
		 */
		public const PAN_DRAG:int = 2;
		
		/**
		 * Provides value for DRAG_TO_CREATE_ELEMENT state.
		 * @private
		 * @flowerModelElementId _DzL0kBmEEeCFupVdDa_wDQ
		 */
		public const DRAG_TO_CREATE_ELEMNT:int = 3;
		 
		/**
		 * Provides value for DRAG_TO_SELECT_ELEMENT state.
		 * @private
		 * @flowerModelElementId _DzMbohmEEeCFupVdDa_wDQ
		 */
		public const DRAG_TO_SELECT_ELEMENT:int = 5;
		 
		/**
		 * The DRAG_TO_CREATE_RELATION state. The Tool enters this state when a RelationAnchor is clicked.
		 * 
		 * @flowerModelElementId _CaGMQD3XEeCdYZQGeBVpLw
		 * @private
		 */ 
		public const DRAG_TO_CREATE_RELATION:int = 6;
		 		 
		/**
		 * @private
		 * @flowerModelElementId _Tve7IHSHEeCMzr1ugdKqbA
		 */ 
		private static const DRAG_TO_CREATE_MIN_WIDTH_DEFAULT:int = 30;

		/**
		 * @private
		 * @flowerModelElementId _Tve7I3SHEeCMzr1ugdKqbA
		 */  
		private static const DRAG_TO_CREATE_MIN_HEIGHT_DEFAULT:int = 30;
		 
		/**
		 * @private
		 * 
		 * Used in DRAG_TO_CREATE_ELEMENT and DRAG_TO_CREATE_RELATION states to keep track of the context menu providing
		 * actions for elements/relations creation.
		 * Later this it must be removed if Tool returns in NORMAL state.
		 * @flowerModelElementId _DzNCsRmEEeCFupVdDa_wDQ
		 */ 
		private var lastOpenedCreationMenu:FlowerContextMenu;
		 
		/**
		 * Tool icon
		 * @private
		 * @flowerModelElementId _ghsxIMStEd605cCM5ZKHKw
		 */		
		[Embed(source='/icons/select.png')]
		[Bindable]
		protected var icon:Class;
		
		// variables used for pan scroll
		/**
		 * Default pan icon
		 * @private
		 * @flowerModelElementId _o0kuQL_MEd-r8cFez7U8gg
		 */		
		[Embed(source='/icons/hand.png')]
		[Bindable]
		protected var defaultPan:Class;
		
		/**
		 * Grab pan icon
		 * @private
		 * @flowerModelElementId _o0lVUr_MEd-r8cFez7U8gg
		 */		
		[Embed(source='/icons/hand_drag.png')]
		[Bindable]
		protected var grabPan:Class;
		
		/**
		 * This variable is used while panning, in order to calculate
		 * the delta between the last position and new position of the
		 * mouse.
		 * @private
		 * @flowerModelElementId _o0l8ZL_MEd-r8cFez7U8gg
		 */
		protected var initialPanPoint:Point = new Point();
		///////////////////////////
		
		/**
		 * Default value.
		 * @flowerModelElementId _P6iqgGpuEeCjZqR9ugnK5Q
		 */ 
		public static const SELECT_WHEN_PARTIAL_CONTAINMENT_DEFAULT:Boolean = true;
		 
	 	/**
		 * @see getter
		 * @flowerModelElementId _P6jRkWpuEeCjZqR9ugnK5Q
		 */ 
		private var _selectWhenPartialContainment:Boolean = SELECT_WHEN_PARTIAL_CONTAINMENT_DEFAULT;  
	  
		/**
	  	 * Default value.
	  	 * @flowerModelElementId _P6j4oWpuEeCjZqR9ugnK5Q
	  	 */ 
	  	public static const DRAG_TO_SELECT_COLOR_DEFAULT:uint = 0x00007F;
	  
		/**
		 * @see getter
		 * @flowerModelElementId _P6lt0GpuEeCjZqR9ugnK5Q
		 */ 
		private var _dragToSelectColor:uint = DRAG_TO_SELECT_COLOR_DEFAULT;
		
		/**
		 * Default value.
		 * @flowerModelElementId _P6mU4WpuEeCjZqR9ugnK5Q
		 */ 
		public static const DRAG_TO_CREATE_COLOR_DEFAULT:uint = 0x007F00;
		
		/**
		 * @see getter
		 * @flowerModelElementId _P6m78WpuEeCjZqR9ugnK5Q
		 */
		private var _dragToCreateColor:uint = DRAG_TO_CREATE_COLOR_DEFAULT;
		
		/**
		 * Default value.
		 * @flowerModelElementId _P6oxIGpuEeCjZqR9ugnK5Q
		 */ 
		public static const DRAG_TO_SELECT_ALPHA_DEFAULT:Number = 0.4;
		
		/**
		 * @see getter
		 * @flowerModelElementId _P6pYMWpuEeCjZqR9ugnK5Q
		 */
		private var _dragToSelectAlpha:Number = DRAG_TO_SELECT_ALPHA_DEFAULT;

		/**
		 * Default value.
		 * @flowerModelElementId _P6qmUGpuEeCjZqR9ugnK5Q
		 */ 
		public static const DRAG_TO_CREATE_ALPHA_DEFAULT:Number = 0.4;

		/**
		  * @see getter
		  * @flowerModelElementId _P6r0cWpuEeCjZqR9ugnK5Q
		  */
		private var _dragToCreateAlpha:Number = DRAG_TO_CREATE_ALPHA_DEFAULT;
		
		/**
		  * @see getter
		  * @flowerModelElementId _TvosIHSHEeCMzr1ugdKqbA
		  */ 
		private var _dragToCreateMinWidth:Number = DRAG_TO_CREATE_MIN_WIDTH_DEFAULT;
		
		/**
		  * @see getter
		  * @flowerModelElementId _TvosI3SHEeCMzr1ugdKqbA
		  */ 
		private var _dragToCreateMinHeight:Number = DRAG_TO_CREATE_MIN_HEIGHT_DEFAULT;
		
		private var _moveCursorVisible:Boolean;
		
		public function get moveCursorVisible():Boolean {
			return _moveCursorVisible;
		}
		
		public function set moveCursorVisible(_moveCursorVisible:Boolean):void {
			this._moveCursorVisible = _moveCursorVisible;
		}
		
		/**
		 * 
		 * Utility function that walks the hierarchy of an editPart
		 * until it gets a selectable one and return it.
		 * If no selectable EditPart is found, or a diagram is found
		 * return null
		 * @flowerModelElementId _b0nLcL8REd6XgrpwHbbsYQ
		 * @private
		 */
		protected function getSelectableEditPart(editPart:EditPart):EditPart {
			// walk the hierarchy
			while (editPart != null && editPart != diagramViewer.getRootEditPart()) {
				if (editPart.isSelectable()) {
					return editPart;
				}
				editPart = editPart.getParent();
			}
			
			// not found or diagramViewer meet
			return null;
		}
		
		protected function canSelectEditPart(editPart:EditPart):Boolean {
			return editPart.isSelectable();
		}
		
		/**
		 * Checks if the target of a Mouse Click event is actually the Diagram.
		 * 
		 * To override in Tools for diagrams that have a composed figure 
		 * to check if mouse click on some component means actually a click on a diagram.
		 * @flowerModelElementId _egLQAL_9Ed-eI6VQ-z2EvQ
		 * @private
		 */ 
		protected function clickedOnDiagram(clickTarget:Object):Boolean {
			// if the target is the DiagramContent canvas or RectangularGrid, then verify if its parent is on diagram
			if (clickTarget is DiagramContent || clickTarget is RectangularGrid)
				return clickedOnDiagram(clickTarget.parent);
			// we want directly the diagram and not an intermediary (as scrollThumb)
			return clickTarget == diagramViewer.getRootEditPart().getFigure(); 
		}
		
		/**
		 * Handler for mouse down event.
		 * 
		 * Adds/Removes to/from selection based on the 
		 * ctrl/shift modifiers
		 * 
		 * If the selected element is a RelationAnchor then enter in DRAG_TO_CREATE_RELATION state and 
		 * adds the connectionPlaceHolder on the diagram updating its source and target points.
		 * 
		 * @flowerModelElementId _b0nLdr8REd6XgrpwHbbsYQ
		 * @private
		 */
		override protected function mouseDownHandler(event:MouseEvent):void {
			super.mouseDownHandler(event);
			// trace("mouse down");
			
			// The context menu used for creating elements/relations must be also hidden when clicking.
			if (lastOpenedCreationMenu != null) {
				var contextMenuUnder:Boolean = false;
				var component:DisplayObject = DisplayObject(event.target);
				// Climb the component while a registered client viewer has not been found. 
				while (component != null) {
					if (component == lastOpenedCreationMenu) {
						contextMenuUnder = true; 
						break;
					}
					component = component.parent;
				}
				if (!contextMenuUnder) {
					closeLastOpenedCreationMenu(true);																			
				}
			}
			
			mouseDownTarget = DisplayObject(event.target);
			var inMultiSelectionMode:Boolean = (event.ctrlKey || event.shiftKey);
			
			// find the first IDraggable placed under mouse pointer -
			// this is usually a figure or any component that can provide an EditPart
			// it is chosen by instance and not depending on 'draggable' or 'selectable' properties
			mouseDownDraggable = getIDraggableFromDisplayObject(mouseDownTarget);
			
			//trace("mouse down on " + mouseDownDraggable);
			// EditPart chosed based on its 'selectable' property starting from mouseDownDraggable and going up on EditParts hierarchy 				
			var selectableEditPart:EditPart = (mouseDownDraggable == null) ? null : getSelectableEditPart(mouseDownDraggable.getEditPart());
			var selectionLength:int =  diagramViewer.getSelectedElements().length;
			
			//trace("selectable EP: " + selectableEditPart);
			
			// selection can proceed if the IDraggable is selectable and this tool is in NORMAL state
			var selectableElementClicked:Boolean = (state == NORMAL && selectableEditPart != null && canSelectEditPart(selectableEditPart));
			//trace("selection can proceed... " + selectableElementClicked);
					
			// if we click on a non-selectable element and we have nothing in selection
			// there is no draggable element to move or resize
			if (!selectableElementClicked && selectionLength == 0) {
				mouseDownDraggable = null;
			}
			
			// if we clicked the diagram -> deselect everything
			// if we clicked the diagram while shift or ctrl is pressed
			// than do nothing (because it is possible that we missed the
			// figure we wanted in the selection)	
			if (!inMultiSelectionMode) {
				
				if (clickedOnDiagram(mouseDownTarget)) {
					mouseWasDownOnDiagram = true;						
					diagramViewer.resetSelection();
				}
			}
			
			// close selection/creation rectangle if mouse clicked
			if (multipleSelectionRectangle.parent != null) {
				removeMultipleSelectionRectangle();
			}
			
			// in case the element will start moving keep the mouse position to compute delta
			// we keep the mouse position no matter if the we have clicked on a selectable or not selectable element 
			// this element can be selected programaticaly and if is draggable it can be moved 
			var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY)); 
			startX = mousePoint.x;
			startY = mousePoint.y;
			
			if (selectableElementClicked) {
				if (event.ctrlKey) {					
					// notify the reset selection mechanism from mouseUp handler
					// not to do anything
					dontResetSelectionOnMouseUp = true;
					if (diagramViewer.getSelectedElements().contains(selectableEditPart)) {
						if (event.ctrlKey) {
							diagramViewer.removeFromSelection(selectableEditPart);
							mouseDownDraggable = null;
						} 
					} else {
						diagramViewer.addToSelection(new ArrayCollection([selectableEditPart]));
					}
				} else {
					if (selectionLength == 0) {
						// empty selection -> add it
						diagramViewer.addToSelection(new ArrayCollection([selectableEditPart]));
					} else {
						if (!diagramViewer.getSelectedElements().contains(selectableEditPart)) {
							// selection does not contain editPart
							// reset selection and add the current editPart
							diagramViewer.addToSelection(new ArrayCollection([selectableEditPart]), true);
						} 
					}
				}
				
			// When RelationAnchor is pressed enter the DRAG_TO_CREATE_RELATION state
			} else if (mouseDownTarget is RelationAnchor) { 
				// obtain the source for the new connection
				sourceEditPart = RelationAnchor(mouseDownTarget).getEditPart();
				targetEditPart = null;
				// add connection placeholder on the diagram
				AbsolutePositionEditPartUtils.addChildFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), IVisualElement(connectionPlaceHolder));
				connectionPlaceHolder.updateFigureHandler();
				
				var p:Point;
				var rect:Rectangle;
				// if source is a Connection
				if (sourceEditPart is ConnectionEditPart) { 
					var array:Array = ConnectionEditPart(sourceEditPart).getConnectionAnchorRect();
					p = new Point(array[0], array[1]);
					rect = new Rectangle();
					
				} else {
					var actualContainer:UIComponent = UIComponent(AbsolutePositionEditPartUtils.getActualFigure(IVisualElementContainer(diagramViewer.getRootFigure())));
					rect = Rectangle(UIComponent(sourceEditPart.getFigure()).getBounds(actualContainer));
					// convert coordinates to take scroll into account
					p = actualContainer.localToContent(rect.topLeft);
				}
				
				// position the connection placeholder start
				positionConnectionPlaceHolderSourcePoint(RelationAnchor(event.target), rect, p);
				
				mousePoint = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
					
				// position the connection placeholder end
				connectionPlaceHolder._targetPoint.x = mousePoint.x; 
				connectionPlaceHolder._targetPoint.y = mousePoint.y;
				
				// disable Context Menu
				ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, false);
				
				// change state
				state = DRAG_TO_CREATE_RELATION; 
				
			} else if (state == SHIFT_PRESSED) {
				// if we are in SHIFT_PRESSED state on click we can pan the screen
				state = PAN_DRAG;
				updateCursor(grabPan);
				// keep the mouse point to compute delta pan
				initialPanPoint.x = event.stageX;
				initialPanPoint.y = event.stageY;
			}
			
			updateStatusBar();
		}
					
		/**
		 * In case the rightClick is enabled handles this event by:
		 * 	- If the current the shift key is pressed state doesn't do any thing
		 * 	- If under the mouse there was no selectable item removeEverything from seletion
		 * 	- If the rightClick target was an element that can be selected 
		 * 	  but is not selected already we have 2 cases:
		 * 			- if the control key is pressed we just add the element to selection
		 * 			- if no control key pressed we deselect everything and add this slectable 
		 * 		      element to selection
		 * 	- If the rightClick target was an element that is already selected we move the 
		 * 	  principal selection to this element
		 */ 
		protected function rightClickHandler(event:MouseEvent):void {
			var elementUnderMouse:IDraggable;
			
			// because focusManager doesn't change focus automatically on rightclick,
			// like it changes in case of left click, we manually change the focus
			// This was added because some other components, like DocumentationView,
			// have focus out handlers that wasn't triggered.
			var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
			
			if (inDiagramVisibleArea(mousePoint.x, mousePoint.y)) {
				UIComponent(diagramViewer.getRootFigure()).focusManager.setFocus(IFocusManagerComponent(diagramViewer.getRootFigure()));
			}
			
			if (event.shiftKey) return;
			
			// The context menu used for creating elements/relations must be hidden when right clicking.
			if (lastOpenedCreationMenu != null) {
				var contextMenuUnder:Boolean = false;
				var component:DisplayObject = DisplayObject(event.target);
				// Climb the component while a registered client viewer has not been found. 
				while (component != null) {
					if (component == lastOpenedCreationMenu) {
						contextMenuUnder = true; 
						break;
					}
					component = component.parent;
				}
				if (!contextMenuUnder) {
					closeLastOpenedCreationMenu(true);																			
				}
			}
			
			// close rectangle if mouse clicked on something
			if (multipleSelectionRectangle.parent != null) {
				removeMultipleSelectionRectangle();
			}
			
			// if we clicked the diagram -> deselect everything
			// if we clicked the diagram while shift or ctrl is pressed
			// than do nothing (because it is possible that we missed the
			// figure we wanted in the selection)
			if (clickedOnDiagram(event.target) && !(event.ctrlKey)) {						
				diagramViewer.resetSelection(false, true);	
				return;			
			}
			
			// find the first IDraggable (no matter if is selectable or not or if is draggable or not) placed under mouse pointer
			elementUnderMouse = getIDraggableFromDisplayObject(DisplayObject(event.target));
							
			var selectableEditPart:EditPart = null;
			if (elementUnderMouse != null) {
				selectableEditPart = getSelectableEditPart(elementUnderMouse.getEditPart());
			}
			
			if (selectableEditPart != null) {
				if (diagramViewer.getSelectedElements().contains(selectableEditPart)) {
					//if is on selection set it as the main selection
					diagramViewer.setMainSelection(diagramViewer.getSelectedElements().getItemIndex(selectableEditPart));
				} else {
					//if no control key pressed deselect all, before selecting the new element
					if (!event.ctrlKey)
						diagramViewer.resetSelection(false, true);
					
					//if is not already on selection add it
					diagramViewer.addToSelection(new ArrayCollection([selectableEditPart]), false, true);
				}
			} else {
				//if the element is not selectable and no ctrl key pressed, reset everything
				if (!event.ctrlKey && inDiagramVisibleArea(mousePoint.x, mousePoint.y))
					diagramViewer.resetSelection(false, true);
			}
		}
		
		protected function positionConnectionPlaceHolderSourcePoint(sourceAnchor:RelationAnchor, sourceRect:Rectangle, sourceLeftTopCorner:Point):void {
			connectionPlaceHolder._sourcePoint.x = sourceLeftTopCorner.x + sourceRect.width / 2;
			connectionPlaceHolder._sourcePoint.y = sourceLeftTopCorner.y + sourceRect.height / 2;
		}
		
		/**
		 * Called to decide if a 'drag to select'/ 'drag to create' operation can start.
		 * Can be overriden in subclasses to block drag to select/create rectangle from drawing when drag operation
		 * starts on particular components.
		 * <p>
		 * By default returns <code>true</code> - there are no elements that will block these operations
		 */ 
		protected function canDragOverTarget(target:DisplayObject):Boolean {
			return true;
		}
	
		/** 
		 * At first call, the function sets currentDraggable to the IDraggable figure that
		 * generates the event or for one of its parents (whose editPart is draggable). 
		 * Then it calls beginDrag() for every selected element. If 
		 * callDragAndDropOverEditPartTarget is set, a move action is in progress, so
		 * at drag & drop time, the target figure needes to be interogated wheather it can receive
		 * the new figures or not.
		 *  
		 * <p>At the future calls, the function calls drag() function for every selected element.
		 * If a move operation is in progress, dragOverEditPartTarget() function is called
		 * as well.
		 *  
		 * deltaX, deltaY are the difference for x and y - axis at every step of the move 
		 * startX, startY - the last (x, y) positions before the current move even
		 * 
		 * @flowerModelElementId _b0nLfL8REd6XgrpwHbbsYQ
		 * @private
		 */
		override protected function mouseMoveHandler(event:MouseEvent):void {
			//used for testing the runing time of this handler
			/*var flagForTimeRegistration:Boolean = false;
			
			var runStartTime:Date = new Date();
			var runEndTime:Date;*/ 
			if (state != PAN_DRAG && state != SHIFT_PRESSED) {
				// avoid calling super behavior on PAN_DRAG because that would start the autoscroll 
				// causing the diagram flicker during pan scroll
				super.mouseMoveHandler(event);
			}
			
			var i:int;		
			var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
			
			if (inDiagramVisibleArea(mousePoint.x, mousePoint.y)) {
				// inside visible area
				if (state == PAN_DRAG) {
					updateCursor(grabPan);
				}
					
				if (state == SHIFT_PRESSED) {
					updateCursor(defaultPan);
				}
				
			} else {
				// outside diagram visible area
				if (state != NORMAL) {
					// only default cursor displayed
					UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors(); 
				}
			}
			
			var allowMoveResize:Boolean = false;
			
			if (event.buttonDown) { // if the mouse fires an move event and the mouse button is clicked => drag operation
				if (state == PAN_DRAG) { // in this state, dragging scrolls the diagram
					// panning must be done only in visible area
					if (inDiagramVisibleArea(mousePoint.x, mousePoint.y) && (diagramViewer.getRootEditPart() is IScrollableEditPart)) {
						var scrollableEditPart:IScrollableEditPart = (IScrollableEditPart) (diagramViewer.getRootEditPart());
						// get delta from last position to current position
						var panDeltaX:int = initialPanPoint.x - event.stageX;
						var panDeltaY:int = initialPanPoint.y - event.stageY;				
						initialPanPoint.x = event.stageX;
						initialPanPoint.y = event.stageY;
						scrollableEditPart.handleExternalScroll(panDeltaX, panDeltaY);
					}
					
				} else if (mouseWasDownOnDiagram || (mouseDownDraggable == null && canDragOverTarget(mouseDownTarget))) {
				//	trace("mouse move: mouse was on diagram area");
					// until now the ruberband rectangle is not on the diagram.
					if (multipleSelectionRectangle.parent == null) {
						// sets rectangle's position
						multipleSelectionRectangle.resetPositionAndDimensions();
						multipleSelectionRectangle.x = mousePoint.x;
						multipleSelectionRectangle.y = mousePoint.y;
						// and add it to the diagram
						AbsolutePositionEditPartUtils.addChildFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), multipleSelectionRectangle);
					}
					// update its dimensions
					multipleSelectionRectangle.width = mousePoint.x - multipleSelectionRectangle.x;
					multipleSelectionRectangle.height = mousePoint.y - multipleSelectionRectangle.y;
									
					// the selection list inside the multipleSelectionRectangle must not 
					// be empty in order to be able to select
					var currentSelectionList:ArrayCollection = getElementsFromComponent(multipleSelectionRectangle);
					// If there are elements covered by the ruberband or the size of the ruberband is too litle then act as selection
					if (currentSelectionList.length != 0 || 
							multipleSelectionRectangle.width < _dragToCreateMinWidth || multipleSelectionRectangle.height < _dragToCreateMinHeight) {
						state = DRAG_TO_SELECT_ELEMENT;
					//	trace("drag to select (selected items: " + currentSelectionList.length + ")");
						
					} else {
						state = DRAG_TO_CREATE_ELEMNT;
					//	trace("drag to create");
					}
					
					if (state == DRAG_TO_SELECT_ELEMENT) {
						multipleSelectionRectangle.setColorAndAlpha(_dragToSelectColor, _dragToSelectAlpha);
					} else if (state == DRAG_TO_CREATE_ELEMNT) {
						multipleSelectionRectangle.setColorAndAlpha(_dragToCreateColor, _dragToCreateAlpha);
					}					
				
				} else if (state == DRAG_TO_CREATE_RELATION) {					
					// get first EditPart at given coordinates
					
					var ep:EditPart = getEditPartFromDisplayCoordinates(event.stageX, event.stageY, IFigureMeetsAdditionalConditions);
						
					// find an acceptable target EditPart
					if (dropOverEditPartOnSpecifiedXPosition(null))
						targetEditPart = getAcceptableEditPartOnSpecifiedXPosition(ep, startX, mousePoint.x);
					else
						targetEditPart = getAcceptableEditPart(ep);
					// update target point on connectionPlaceHolder
					connectionPlaceHolder._targetPoint.x = mousePoint.x;
					connectionPlaceHolder._targetPoint.y = mousePoint.y;
					
					checkTargetAndUpdateCursor();
					
				} else if (currentDraggable == null) {											
					// it is not necessary to search the draggable object under mouse again - it is already localized on mouseDown
					// also the start point position is computed on mouseDown
					// see if allows the drag - if not, go up on hierarchy until one that permits move/resize (drag) - this can lead to null value
					currentDraggable = computeIsDraggable(mouseDownDraggable);
					// can not drag if no elements selected or the selected one does not allow drag
					if (currentDraggable != null) {						
						// find the EditPart starting the drag - this is the initiator and it must be on selection
						var index:int = diagramViewer.getSelectedElements().getItemIndex(currentDraggable.getEditPart());
						if (index >= 0) {
							// this is not necessarely the last in the list of selected EditParts - when you have a multiple selection you can 
							// choose any of the selected figures to start a move/resize 
							
							// first step is to initiate drag(call beginDrag) on currentDraggable (the main selected) and the other elements on selection
							callDragAndDropOverEditPartTarget = currentDraggable.getEditPart().beginDrag(currentDraggable, startX, startY, true);
							
							// as the element is dragged, no need to show the contextMenu
							if (currentDraggable.getEditPart() is IAbsolutePositionEditPart || currentDraggable.getEditPart() is ConnectionEditPart) { 
								ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, false);
							}
							
							i = 0;							
							while (i < diagramViewer.getSelectedElements().length) {
								if (i == index) {
									// jump over the initiator as it already called beginDrag() above
									i++;
								} else {
									// for each selected elements start the drag operation (instantiate mask)
									var epToDrag:EditPart = diagramViewer.getSelectedElements()[i];
									// also check if allows dragging
									if (epToDrag.getFigure() != null && epToDrag.isDraggable()) {
										epToDrag.beginDrag(epToDrag.getFigure(), startX, startY, false);
									}
									i++;
								}
							}	
							
							// force entrance in the next step - compute the new position
							allowMoveResize = true;													
						}
					}
				
				} else {
					// currentDraggable already computed - perform next step => new size/position
					allowMoveResize = true;
				}
				
				if (allowMoveResize) {
					
					// update the current position and call the methods to update placeHolders if needed (drag function)
					deltaX = mousePoint.x - startX;
					deltaY = mousePoint.y - startY; 
						
					// if a move operation is in progress, compute the target figure
					if (callDragAndDropOverEditPartTarget) 
						targetEditPart = getTargetEditPart(diagramViewer, currentDraggable, event.stageX, event.stageY, true);
					
					if (callDragAndDropOverEditPartTarget && targetEditPart != null) {
						// 	If the current targetEditPart doesn't accept the editParts, its parent editPart
		 				// is asked. And so on, until one of them accepts the editParts. If no such edit part 
		 				// was found, the operation is denied.
		 				if (dropOverEditPartOnSpecifiedXPosition(currentDraggable.getEditPart())) {
							while (targetEditPart != null && !currentDraggable.getEditPart().dragOverTargetEditPartOnSpecifiedXPosition(currentDraggable, targetEditPart, diagramViewer.getSelectedElements(), startX, mousePoint.x)) {
								targetEditPart = targetEditPart.getParent();
							}
						} else {
							while (targetEditPart != null && !currentDraggable.getEditPart().dragOverTargetEditPart(currentDraggable, targetEditPart, diagramViewer.getSelectedElements())) {
								targetEditPart = targetEditPart.getParent();
							}
						}
						checkTargetAndUpdateCursor();	
					}
					
					//used for testing the runing time of this handler
					//flagForTimeRegistration = true;
					
					for (i = 0; i < diagramViewer.getSelectedElements().length; i++) {
						// for each selected element, call drag => update placeHolder
						
						var figureToDrag:IDraggable = IDraggable(EditPart(diagramViewer.getSelectedElements().getItemAt(i)).getFigure());
						if (figureToDrag != null && figureToDrag.getEditPart().isDraggable()) {
							figureToDrag.getEditPart().drag(currentDraggable, deltaX, deltaY, isDragInitiator(figureToDrag));
						}
					}
				}
			}
			
			updateStatusBar();
			//used for testing the runing time of this handler
			/*
			if (flagForTimeRegistration) {
				runEndTime = new Date();
				trace ("Mouse move runing time: " + (runEndTime.getTime() - runStartTime.getTime()));
			}*/
			
		}

		/**
		 * If a mouse up event was fired, a move / resize operation was ended.
		 * The drop() function is called for every selected element
		 * @flowerModelElementId _b0nLgr8REd6XgrpwHbbsYQ
		 * @private
		 */
		override protected function mouseUpHandler(event:MouseEvent):void {
			super.mouseUpHandler(event);
			
			var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
			var container:Group = Group(diagramViewer.getRootEditPart().getFigure());
			var keepSelectionRectangle:Boolean = false; // set true only if we need to keep the multiple
			var displayAreaOfSelection:Rectangle;
			
			// selection rectangle on screen (until next mouse down)			
			if (state == PAN_DRAG && event.shiftKey && inDiagramVisibleArea(mousePoint.x, mousePoint.y)) {
				// if shift pressed remain in PAN state SHIFT_PRESSED 
				updateCursor(defaultPan);
				state = SHIFT_PRESSED;
			} else if (state == DRAG_TO_SELECT_ELEMENT || state == PAN_DRAG) {
				// exit PAN_DRAG or DRAG_TO_CREATE_ELEMENT states - go back to normal cursor and reactivate autoscrollTimerHandler
				//trace ("SMRT->mouseUpHandler " + state);
				UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors();
				state = NORMAL;
				autoScrollTimer.addEventListener(TimerEvent.TIMER, autoScrollTimerHandler);	
			}
			// if connectionPlaceHolder exists on viewer and we aren't in a drag to create relation state, remove it and its creation menu
			// it's the case when the mouse up is dispached outside the creation menu
			if (connectionPlaceHolder.parent != null && state != DRAG_TO_CREATE_RELATION) {				
				endCreateRelation();
				closeLastOpenedCreationMenu();	
			}
			
			if (state == DRAG_TO_CREATE_ELEMNT) {
				// If the marquee selection rectangle has been initialized and if it measures at leat some dimensions.
				var minimumSizeAccepted:Boolean = multipleSelectionRectangle != null &&
						multipleSelectionRectangle.width > _dragToCreateMinWidth && 
						multipleSelectionRectangle.height > _dragToCreateMinHeight;
				
				if (minimumSizeAccepted) {
					// TODO sorin : foarte hack. deloc normal. Poate ca solutie de creat un client virtual SMR care sa poata comunica in mod mai usor 
					// si la care se pot abona diverse logici.

					if (diagramViewer.getCreateElementActionProvider() != null) {
						// Create new context menu.
						var createElementMenu:FlowerContextMenu = new FlowerContextMenu();
						createElementMenu.setTitle("Select element to create"); // TODO LU: get from language file
						createElementMenu.show(true);
						createElementMenu.closeAfterActionRun = true;
						createElementMenu.setSelectionProvider(diagramViewer);
																			
						// Obtain a specialized instance of context.
						var createActionContext:ActionContext = diagramViewer.getCreateElementActionProvider().getContext();

						// and delegate the filling to the client.
						diagramViewer.fillCreateElementActionContext(createActionContext, multipleSelectionRectangle);	

						// Before ContextMenu checks the visibility of the action, the context must first be assigned. 
						createElementMenu.beforeActionVisibilityEvaluatedFunction = 
							function(action:IAction):void {
								action.context = createActionContext;
							};
						// remove multipleSelectionRectangle after action execution 
						createElementMenu.afterActionExecutedFunction = 
							function(mainContextMenu:FlowerContextMenu):void {	
								removeMultipleSelectionRectangle();
							};
						createElementMenu.actionContext = createActionContext;
						
						// Next follows the magic of filling the context menu entries.
						diagramViewer.getCreateElementActionProvider().fillContextMenu(createElementMenu);	

						// Obtain the container and do some stuff to obtain the corners.
						var scrollableContainer:Group = Group(AbsolutePositionEditPartUtils.getActualFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()))); 
						var upperLeft:Point = ContextMenuUtils.getStageLocationForContainer(scrollableContainer, new Point(multipleSelectionRectangle.x,multipleSelectionRectangle.y), diagramViewer.getScaleForContextMenu());
						var downRight:Point = ContextMenuUtils.getStageLocationForContainer(scrollableContainer, new Point(multipleSelectionRectangle.x + multipleSelectionRectangle.width, multipleSelectionRectangle.y + multipleSelectionRectangle.height), diagramViewer.getScaleForContextMenu());
							
						// For having the width and the height of the rectangle we need to substract the x and y of the down right corner from the upper left corner.  
						displayAreaOfSelection = new Rectangle(upperLeft.x, upperLeft.y, downRight.x - upperLeft.x, downRight.y - upperLeft.y);  
						createElementMenu.setLocation(displayAreaOfSelection, UIComponent(diagramViewer.getRootEditPart().getFigure()));						

						// If someone has added some entries then continue.
						if (createElementMenu.hasMenuEntries()) {
							if (createElementMenu.numChildren == 2 && autoInvokeSingleCreateElementContextMenuAction()) {
								ActionEntry(createElementMenu.getChildAt(1)).runAction();
							} else {
								// we still need the rectangle on screen
								keepSelectionRectangle = true;
								// keep the creation menu, to be able later to close it.
								lastOpenedCreationMenu = createElementMenu;
								ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, false);	
							}								
						}						
					} 
				}
				state = NORMAL;	
			} else if (state == DRAG_TO_CREATE_RELATION) {				
				// clear cursor
				updateCursor(null);
				
				if (targetEditPart == null && acceptRelationToNoTarget()) { // display specific create relation context menu
					var createRelationExistingMenu:FlowerContextMenu = createRelationContextMenu("Select Relation Type");
					
					// Next follows the magic of filling the context menu entries.
					diagramViewer.getCreateRelationToExistingElementActionProvider().fillContextMenu(createRelationExistingMenu);	
					
					// position the menu next to mouse location
					var stageCursorPosition:Point = ContextMenuUtils.flex4CompatibleContainer_getCursorPosition(UIComponent(diagramViewer.getRootEditPart().getFigure()));
					var displayAreaOfSelection:Rectangle = new Rectangle(stageCursorPosition.x, stageCursorPosition.y, DiagramViewer.MINIMUM_SIZE_AREA, DiagramViewer.MINIMUM_SIZE_AREA);  
					createRelationExistingMenu.setLocation(displayAreaOfSelection, UIComponent(diagramViewer.getRootEditPart().getFigure()));
							
					if (createRelationExistingMenu.hasMenuEntries()) {						
						// keep track of it to remove later if necessary
						lastOpenedCreationMenu = createRelationExistingMenu;
						ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, false);						
					} else {
						endCreateRelation();
					}				
				} else if (targetEditPart != null && diagramViewer.getCreateRelationActionProvider() != null) {
					var createRelationMenu:FlowerContextMenu = createRelationContextMenu("Select Relation to Create");
					
					// Next follows the magic of filling the context menu entries.
					diagramViewer.getCreateRelationActionProvider().fillContextMenu(createRelationMenu);	
					
					// position the menu next to mouse location
					var stageCursorPosition:Point = ContextMenuUtils.flex4CompatibleContainer_getCursorPosition(UIComponent(diagramViewer.getRootEditPart().getFigure()));
					var displayAreaOfSelection:Rectangle = new Rectangle(stageCursorPosition.x, stageCursorPosition.y, DiagramViewer.MINIMUM_SIZE_AREA, DiagramViewer.MINIMUM_SIZE_AREA);  
					createRelationMenu.setLocation(displayAreaOfSelection, UIComponent(diagramViewer.getRootEditPart().getFigure()));
							
					if (createRelationMenu.hasMenuEntries()) {
						if (createRelationMenu.numChildren == 2 && autoInvokeSingleCreateRelationContextMenuAction()) {							
							endCreateRelation();
							ActionEntry(createRelationMenu.getChildAt(1)).runAction();
						} else {
							// keep track of it to remove later if necessary
							lastOpenedCreationMenu = createRelationMenu;
							ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, false);
						}
					} else {
						endCreateRelation();
					}
				} else if (! acceptRelationToNoTarget()) {
					endCreateRelation();
				}				
				// remove no longer necessary references
				sourceEditPart = targetEditPart = null;
				// finally go back to NORMAL state
				state = NORMAL;
			}			
			
			var deselectOnMouseUp:Boolean = true;
			if (currentDraggable != null) {
				deselectOnMouseUp = false;
				// Luiza: Mai e necesara linia comentata? E deja calculat mai sus
				//var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
				deltaX = mousePoint.x - startX;
				deltaY = mousePoint.y - startY;
						
				// call dropOverEditPart after drop on every editParts involved in drag&drop operation
				// because the effect of this operation (model modification) might destry the selection
				// and then the placeholders will not be removed.
				if (callDragAndDropOverEditPartTarget && targetEditPart != null)
					if (dropOverEditPartOnSpecifiedXPosition(currentDraggable.getEditPart()))
						currentDraggable.getEditPart().dropOverEditPartOnSpecifiedXPossition(currentDraggable, targetEditPart, diagramViewer.getSelectedElements(), deltaX, deltaY, startX, mousePoint.x);
					else
						currentDraggable.getEditPart().dropOverEditPart(currentDraggable, targetEditPart, diagramViewer.getSelectedElements(), deltaX, deltaY);
														
				dropSelectedEditParts();
				
				currentDraggable = null;
				//trace ("SMRT->mouseUpHandler DROP_Over....");
				UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors();
								
				// After the user has fininished dragging we set the context menu enabled state. 
				// A callLater is used because there are cases when drawing isn't done yet
				// so the context menu position will be incorrectly calculated
				UIComponent(getDiagramViewer().getRootFigure()).callLater(ContextMenuManager.INSTANCE.updateContextMenuEnabled, [getDiagramViewer(), true]);
			} 
			
			// clear the setting made on mouseDown
			mouseDownDraggable = null;
			mouseDownTarget = null;
			
			// clearing things used by multiple selection by rectangle 
			mouseWasDownOnDiagram = false;
			if (multipleSelectionRectangle.parent != null && !keepSelectionRectangle) {
				deselectOnMouseUp = false;
				// mark that adding multiple EditParts to selection has ended
				var currentSelectionList:ArrayCollection = getElementsFromComponent(multipleSelectionRectangle);
				// Markk that the editparts from currentSelectionList are now also selected, but does not reset selection.
				diagramViewer.addToSelection(currentSelectionList, false);
				// remove it from diagram
				removeMultipleSelectionRectangle();
				// close creation context menu
				closeLastOpenedCreationMenu(true);									
			}	
			
			// we moved reseting the selection on click on an element from selection here because we need
			// to know if the user wanted to make a drag or just select another figure
			if (!dontResetSelectionOnMouseUp && deselectOnMouseUp && diagramViewer.getSelectedElements().length > 1) {
				// we are interested in objects of type IFigure
				var obj:IDraggable = getIDraggableFromDisplayObject(DisplayObject(event.target));
				if (obj != null) {
					// and selectable
					var editPart:EditPart = getSelectableEditPart(obj.getEditPart());
					if (editPart != null && editPart.isSelectable()) {
						diagramViewer.addToSelection(new ArrayCollection([editPart]), true);
					}
				}					
			}
			// clearing things used by reset selection mechanism
			dontResetSelectionOnMouseUp = false;
				
			updateStatusBar();
		}
		
		/**
		 * This function can be overwritten by the subclasses in order to implement
		 * a custom call for the drop methods of the selected edit parts.
		 * For example by default this function calls the drop function with the isDragInitiator
		 * parameter setted to true only for the figure from where the drag operation start.
		 * Sometimes we could need to call the drag function with the isDargInitiator setted to true
		 * for other editParts also.
		 */ 
		protected function dropSelectedEditParts():void {
			for (var i:int = 0; i < diagramViewer.getSelectedElements().length; i++) {
				var drg:IFigure = (EditPart(diagramViewer.getSelectedElements().getItemAt(i)).getFigure());
				if (drg != null && drg.getEditPart().isDraggable()) { 
					drg.getEditPart().drop(currentDraggable, deltaX, deltaY, isDragInitiator(drg));	
				}
			}
		}
		
		protected function autoInvokeSingleCreateElementContextMenuAction():Boolean {
			return true;
		}
		
		protected function autoInvokeSingleCreateRelationContextMenuAction():Boolean {
			return true;
		}
		
		/**
		 * Subclases can override this method to check that for the currentEditPart we need to call the <code>dropOverEditPart</code>  with two more arguments:
		 *  <code>startX</code> and <code>dropX</code>  
		 */ 
		protected function dropOverEditPartOnSpecifiedXPosition(currentDraggableEP:EditPart):Boolean {
			return false;
		}
		/**
		 * @private
		 * @flowerModelElementId _t81AMGc-EeCu6L4Yd8Sw-Q
		 */  
		protected function useHackForGanttElementCreation():Boolean {
			return false;
		}
		
		// TODO Sorin : codul asta de mai jos nu e prea pare destul de general scris.		
		/**
		 * Gets the edit parts within a given <b>UIComponent</b>.
		 * @flowerModelElementId _DzRUIBmEEeCFupVdDa_wDQ
		 */	
		private function getElementsFromComponent(component:UIComponent):ArrayCollection {
			// add to selection everything under the selection rectangle
			var editPartList:ArrayCollection= new ArrayCollection();
			var childs:ArrayCollection = diagramViewer.getRootEditPart().getChildren(); 
			var childLength:int = childs.length;
			var fig:IFigure;
			var rect:Rectangle;
			var rootDisplay:UIComponent = UIComponent(diagramViewer.getRootEditPart().getFigure());
			var rectDiag:Rectangle = component.getRect(rootDisplay);
			// convert to content coordinates so we can 
			// process the entire Diagram area (including what is not visible)
			var rectPosition:Point = rootDisplay.localToContent(rectDiag.topLeft);
			var scaleFactor:Number = (rootDisplay is RootFigure) ? RootFigure(rootDisplay).getScaleFactor() : 1;
			rectDiag.x = rectPosition.x;
			rectDiag.y = rectPosition.y;
			// take scale into consideration
			rectDiag.width = rectDiag.width / scaleFactor;
			rectDiag.height = rectDiag.height / scaleFactor;
			// cycle through all the diagram children			
			for (var idx:int = childLength - 1; idx >= 0; idx--) {
				// we are interested only in selectable ones
				if (canSelectEditPart(childs[idx] as EditPart)) {
					fig = EditPart(childs[idx]).getFigure();
					if (fig != null) {
						// get it's visual coordinates
						rect = UIComponent(fig).getBounds(rootDisplay);
						// convert to content coordinates
						rectPosition = rootDisplay.localToContent(rect.topLeft);
						rect.x = rectPosition.x;
						rect.y = rectPosition.y;
						// take scale into consideration
						rect.width = rect.width / scaleFactor;
						rect.height = rect.height / scaleFactor;
						if (_selectWhenPartialContainment) {
							// in case of segments test if intersects the selection rectangle
							if (EditPart(childs[idx]).getFigure() is ConnectionFigure) {								
								var segmentFig:ConnectionFigure = EditPart(childs[idx]).getFigure() as ConnectionFigure;							
								for (var i:int = 0; i < segmentFig.getNumberOfSegments(); i++) {
									var segment:ConnectionSegment = segmentFig.getSegmentAt(i);
									if (segment.intersectsRect(rectDiag)) {
										// add connection figure only once
										if (!editPartList.contains(childs[idx]))
											editPartList.addItem(childs[idx]);
									}
								}
							} else { // check to see if it is inside selection retangle
								if (rectDiag.intersects(rect)) {
									editPartList.addItem(childs[idx]);
								}
							} 
						} else {
							if (rectDiag.containsRect(rect)) {
								editPartList.addItem(childs[idx]);
							}
						}
					} else {
						// the figure is not on display
						if (childs[idx] is IAbsolutePositionEditPart) {
							var boundArray:Array = IAbsolutePositionEditPart(childs[idx]).getBoundsRect();
							// no need to convert to content coordinates
							// because they already are content coordinates
							rect = new Rectangle(boundArray[0], boundArray[1], boundArray[2], boundArray[3]);
							if (_selectWhenPartialContainment) {
								// check to see if it is inside selection retangle
								if (rectDiag.intersects(rect)) {
									editPartList.addItem(childs[idx]);
								}
							} else {
								if (rectDiag.containsRect(rect)) {
									editPartList.addItem(childs[idx]);
								}
							}
						}
					}
				}					
			}
			return editPartList;
		}
		
		/**
		 * 
		 * Update the cursor icon based on the target figure 
		 * under mouse that can/cannot accept the selected figures
		 * @flowerModelElementId _b0nLiL8REd6XgrpwHbbsYQ
		 */
		protected function checkTargetAndUpdateCursor():void {
			if (targetEditPart != null)// operation allowed
				updateCursor(copyCursor);
			else // operation denied
				updateCursor(rejectCursor);
		}
		
		/**
		 * 
		 *  The function is used in <code>mouseMoveHandler</code>. It checks wheather 
		 *  the current drag operation is permitted.
		 *  
		 *  <p>If the draggable object received as argument is draggable => set
		 *  currenDraggable and return true. Otherwise, it checks its parent, 
		 *  and its parent's parent and so on. If it finds one editPart which is
		 *  draggable, set currentDraggable and return true. If it doesn't find 
		 *  any draggable EditPart, returns false.
		 *  
		 *  <p>Ex: draggable = CompartmentFigure, which is not draggable. Checks with
		 *  its parent (ClassFigure) which is draggable. The drag action will now
		 *  operate on ClassFigure
		 * @flowerModelElementId _l2MDAMliEd6hMdHnTBcXLQ
		 */ 
		protected function computeIsDraggable(draggable:IDraggable):IDraggable {
			if (draggable == null || draggable.getEditPart().isDraggable()) {
				return draggable;
			
			} else {
				var ep:EditPart = draggable.getEditPart();
				// go on parent hierarchy until find a draggable EditPart or null
				do {
					if (ep.isDraggable()) {
						return ep.getFigure();
					}
					ep = ep.getParent();
				} while (ep != null);
				
				// no draggable EditPart found
				return null;
			}
		}
		
		/**
		 * @private
		 *  
		 * If <code>draggable</code> is <code>ResizeAnchor</code> compare the figure
		 * associated with its editPart (a ClassFigure for example) with 
		 * the current figure
		 * 
		 * @flowerModelElementId _l2MDBsliEd6hMdHnTBcXLQ
		 */ 
		protected function isDragInitiator(draggable:IDraggable):Boolean {
			if (currentDraggable is ResizeAnchor || currentDraggable is ConnectionSegment)
				return (draggable == IDraggable(currentDraggable.getEditPart().getFigure()) ? true : false);
			else 
				return (draggable == currentDraggable ? true : false);
		}
		
		/**
		 * 
		 * The function returns the first IDraggable object from the current display
		 * object.
		 * 
		 * EX: click on an attribute (the textfield fires a mouse event)
		 * 	from Canvas.Class.Compartment.DecoratedLabel.Label.TextField => DecoratedLabel
		 * 
		 * @flowerModelElementId _b0nLmb8REd6XgrpwHbbsYQ
		 */
		static protected function getIDraggableFromDisplayObject(displayObj:DisplayObject):IDraggable {
			while ((displayObj != null) && !(displayObj is IDraggable))
				displayObj = displayObj.parent;
			return IDraggable(displayObj);
		}
		
		/**
		 * 
		 *  From all the objects under mouse, the function computes the IDraggable
		 *  figure that is under the movement mask. From this object, returns its
		 *  EditPart (in case the IDraggable object was found)
		 * @flowerModelElementId _1Rx1kBf2Ed-L1am98FeypA
		 * @private
		 */
		public static function getTargetEditPart(diagramViewer:DiagramViewer, currentDraggable:IDraggable, x:int, y:int, insideCallForDragAndDropOverEditPart:Boolean = false):EditPart {
			//return all the objects under the current mouse position
			var arr:Array = DisplayObject(diagramViewer.getRootEditPart().getFigure()).stage.getObjectsUnderPoint(new Point(x, y));
			var targetEditPart:EditPart = null;
			var i:int = 0;
			var p1:DisplayObject;
			var idr:IDraggable;
			
			for (i = arr.length - 1; i >= 0; i--) {
				//for every object under mouse => find the first IDraggable object
				if (!(arr[i] is MoveResizePlaceHolder)) {
					p1 = DisplayObject(arr[i]);
					idr = getIDraggableFromDisplayObject(p1);
					if (idr != null && (currentDraggable == null || idr.getEditPart() != currentDraggable.getEditPart()) && ! (insideCallForDragAndDropOverEditPart && idr.getEditPart() is ConnectionLabelEditPart)) 
						break;
				}
			}
			if (idr != null)  // if a draggable object was found, get its edit part
				targetEditPart = IDraggable(idr).getEditPart();
				
			return targetEditPart;	
		}


		/**
		 * Given the <code>DiagramViewer</code> and the coordinates relative to the content of the drawable diagram container,
		 * this iterates throught the editParts and determines the top most <code>IAbsolutePositionEditPart</code> one that 
		 * intersects with the given coordinates, thus returning the editPart of the top most "visibile" figure.   
		 * 
		 * <p/> Note: this method does not check if the editPart is a selectable or a draggable one.
		 * @author Sorin
		 * @private
		 * @flowerModelElementId _t83ccGc-EeCu6L4Yd8Sw-Q
		 */ 
		public static function getEditPartUnderPointRelativeToContent(diagramViewer:DiagramViewer, x:Number, y:Number):EditPart {
			// In here we keep top most the discovered editPart under the given coordinates.
			// By default it will consider the Diagram editPart and it will be overrided if one is found.
			var discoveredEditPart:EditPart = diagramViewer.getRootEditPart();
			
			var childrenEditPart:ArrayCollection = diagramViewer.getRootEditPart().getChildren();
			// Iterate from the top of the list because the it also dictates the top most figure.
			for (var i:int = childrenEditPart.length - 1; i >= 0; i--) {
				if (childrenEditPart[i] is IAbsolutePositionEditPart) {
					var boundsRect:Array = IAbsolutePositionEditPart(childrenEditPart[i]).getBoundsRect();
					// Coordinates are relative to the content of the diagram.
					var childRect:Rectangle = new Rectangle(boundsRect[0], boundsRect[1], boundsRect[2], boundsRect[3]);
					if (childRect.contains(x, y)) {
						discoveredEditPart = childrenEditPart[i];
						break; // We have found the top most editPart with coordinates that contains our point.
					}
				}
			}
			return discoveredEditPart;
		}
		
		
		// TODO: Luiza - De ce acest keyDownHandler ramane activ chiar daca Tool-ul se dezactiveaza?
		// Am vazut ca se ocupa de stoparea autoscroll la ESC pressed. Dar nu avem mereu un Tool activ? De ce sa ramana activ pentru
		// fiecare tool?
		/**
		 * 
		 * Keydown handler:
		 * <li> ESC key was pressed => if there is a move/resize action in progress,
		 * 		it is canceled and the model doesn't change. </li>
		 * <li> ESC key was pressed => if there is a multiple selection in progress
		 * 		it is canceled and the selection doesn't modify. </li
		 * @flowerModelElementId _b0nLpr8REd6XgrpwHbbsYQ
		 * @private
		 */
		override protected function keyDownHandler(event:KeyboardEvent):void {
			super.keyDownHandler(event);
			if (event.keyCode == Keyboard.ESCAPE) {
				if (currentDraggable != null) {					
					for (var i:int = 0; i < diagramViewer.getSelectedElements().length; i++) {
						var drg:IDraggable = IDraggable(EditPart(diagramViewer.getSelectedElements().getItemAt(i)).getFigure());
						if (drg != null && drg.getEditPart().isDraggable()) {
							drg.getEditPart().abortDrag(currentDraggable, startX, startY, isDragInitiator(drg) ? true : false);
						/*if (computeIsDraggable(drg) != null) {
								var draggableEP:EditPart = computeIsDraggable(drg).getEditPart();
								draggableEP.abortDrag(currentDraggable, startX, startY, isDragInitiator(draggableEP.getFigure()) ? true : false);
							}*/
						}
					}
					currentDraggable = null;
					mouseDownDraggable = null;
					//trace ("SMRT->keyDownHandler");
					UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors();

	 				// After the user has fininished dragging we set the context menu enabled state. 
					ContextMenuManager.INSTANCE.updateContextMenuEnabled(getDiagramViewer(), true);
					
					// notify the reset selection mechanism from mouseUp handler
					// not to do anything
					dontResetSelectionOnMouseUp = true;
				}
				
				if (multipleSelectionRectangle.parent != null) {
					// cancel multiple selection
					// clearing things used by multiple selection by rectangle 
					mouseWasDownOnDiagram = false;
					// remove the placeholder from diagram
					removeMultipleSelectionRectangle();
				}
				
				closeLastOpenedCreationMenu(true);				
				
				// abbort create relation => remove connection figure 
				if (connectionPlaceHolder.parent != null) {
					endCreateRelation();
					sourceEditPart = targetEditPart = null;
				}
				
				updateCursor(null);
				state = NORMAL;
			}
			if (!event.ctrlKey && event.keyCode == Keyboard.SHIFT && state == NORMAL) {
				// this tool should act if not deactivated (vezi TODO mai sus) and user is not editing
				if (diagramViewer != null && !diagramViewer.getInplaceEditorTool().isCurrentlyActive()) {  
					// change cursor and set state to SHIFT_PRESSED
					var fig:DisplayObject = DisplayObject(diagramViewer.getRootEditPart().getFigure());
					var stage:Stage = DisplayObject(diagramViewer.getRootEditPart().getFigure()).stage;
					var mousePoint:Point = globalToDiagram(Math.ceil(stage.mouseX), Math.ceil(stage.mouseY));
					if (inDiagramVisibleArea(mousePoint.x, mousePoint.y)) {
						updateCursor(defaultPan);
						state = SHIFT_PRESSED;
						autoScrollTimer.stop(); // no auto-scrolling done while panning
						autoScrollTimer.removeEventListener(TimerEvent.TIMER, autoScrollTimerHandler);	
					}
				}
			}
		}
		
		/**
		 * 
		 * Keyup handler:
		 * <ul>
		 * <li> Ctrl+A, Command+A pressed => selects every selectable element on the diagram </li>
		 * </ul
		 * @author Sorin
		 * @flowerModelElementId _b0w8cL8REd6XgrpwHbbsYQ
		 * @private
		 */
		private function keyUpHandler(event:KeyboardEvent):void {
			var keyCodeAForWindows:int = 65; // obviously with CTRL key because CMD key does not exist on Windows
			// The values for Mac OS were obtained by debugging and seeing the console.
			var keyCodeAWithCommandForMac:Number = 4294967295; // CMD + A
			var keyCodeAWithControlForMac:Number = 1; // CTRL + A
			if (event.ctrlKey && // control or command key pressed
					(event.keyCode == keyCodeAForWindows || // on windows
						event.keyCode == keyCodeAWithCommandForMac || event.keyCode == keyCodeAWithControlForMac)) { // on mac
				var length:int = diagramViewer.getRootEditPart().getChildren().length;

				// Collection for to be added in a single operation.
				var collectedEditParts:ArrayCollection = new ArrayCollection();
				var editPart:EditPart;
				for (var i:int = 0; i < length; i++) {
					editPart = EditPart(diagramViewer.getRootEditPart().getChildren().getItemAt(i));
					if (editPart.isSelectable()) {
						collectedEditParts.addItem(editPart);
					}
				}
				// Add the editParts to the selection, without reseting the selection. This also dispatched the notification.
				diagramViewer.addToSelection(collectedEditParts, false);
			}
			if (state == SHIFT_PRESSED) {
				// changes cursor back to default and the state if necessary
				//trace ("SMRT->keyUpHandler " + state);
				UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors();
				state = NORMAL;
			}
		}
		
		/**
		 * 
		 * The function is called from <code>AbsolutePositionSelectionAnchors</code>
		 * to update the cursor during a resize operation that involves <code>ResizeAnchors</code>
		 * @flowerModelElementId _b0w8dr8REd6XgrpwHbbsYQ
		 * @private
		 */
		public function isDragInProgress():Boolean {
			// also check if DRAG_TO_CREATE_RELATION is the current state to not allow changing the cursor - otherwise flicker appears
			// if PAN_DRAG state and a draggable element is selected on diagram then it would also produce flickers in cursor image  
			if (currentDraggable != null || state == DRAG_TO_CREATE_RELATION || state == PAN_DRAG)
				return true;
			else
				return false;
		}
		
		protected function rightClickEnabledChangeHandler(event:Event):void {
			if (diagramViewer.rightClickEnabled)
				DisplayObject(diagramViewer.getRootEditPart().getFigure()).stage.addEventListener("rightClick", rightClickHandler);
			else
				DisplayObject(diagramViewer.getRootEditPart().getFigure()).stage.removeEventListener("rightClick", rightClickHandler);
		}
		
		/**
		 * Adds the event listeners for the following events: mouseUp, mouseMove, mouseDown, keyDown, keyUp
		 * @flowerModelElementId _b0w8e78REd6XgrpwHbbsYQ
		 * @private
		 */
		override public function activate(diagramViewer:DiagramViewer):void {
			super.activate(diagramViewer);
			Application.application.stage.addEventListener(KeyboardEvent.KEY_UP, keyUpHandler);
			
			
			if (diagramViewer.ContextMenuWasActivated() && diagramViewer.rightClickEnabled == true)
				Application.application.stage.addEventListener("rightClick", rightClickHandler);
			
			diagramViewer.addEventListener("rightClickEnabledChange", rightClickEnabledChangeHandler);
				
			// When this tool is active we allow the showing of the context menu
			ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, true);
		}	
			
		/**
		 * 
		 * Remove all event listeners
		 * @flowerModelElementId _b0w8gb8REd6XgrpwHbbsYQ
		 * @private
		 */
		override public function deactivate():void {
			// In case the placeholder for the creation element exists clean it
			if (multipleSelectionRectangle.parent != null) {
				// cancel multiple selection
				// clearing things used by multiple selection by rectangle 
				mouseWasDownOnDiagram = false;
				// remove the placeholder from diagram
				removeMultipleSelectionRectangle();	
			}
			// In case the menu for creation element exists clean it also.
			closeLastOpenedCreationMenu();				
			
			if (connectionPlaceHolder.parent != null) {
				AbsolutePositionEditPartUtils.removeChildFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), IVisualElement(connectionPlaceHolder));
				sourceEditPart = targetEditPart = null;
			}
			
			// When this tool is not active we allow the showing of the context menu
			ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, false);
			Application.application.stage.removeEventListener(KeyboardEvent.KEY_UP, keyUpHandler);
			
			if (diagramViewer.ContextMenuWasActivated() && diagramViewer.rightClickEnabled)
				Application.application.stage.removeEventListener("rightClick", rightClickHandler);
			
			diagramViewer.removeEventListener("rightClickEnabledChange", rightClickEnabledChangeHandler);
			super.deactivate();
		}
		
		/**
		 * The label of the current tool.
		 * @private
		 * @flowerModelElementId _gh2iIMStEd605cCM5ZKHKw
		 */
		override public function getLabel():String {
			return "Selection";
		}
		
		/**
		 * Returns the icon for the current tool
		 * @private
		 * @flowerModelElementId _gh2iJcStEd605cCM5ZKHKw
		 */
		override public function getIcon():Class {
			return icon;
		}
				
		/**
		 * To override in subclasses to add information in diagram status bar.
		 * 
		 * @author Luiza
		 * @flowerModelElementId _XqZccOwaEd-Mq65kNpXUPA
		 * @private
		 */ 
		protected function updateStatusBar():void {	
		}
		
		////////////////////////////////////////////	
		// functions for create relation behavior //
		////////////////////////////////////////////
		/** This function it is sent as a parameter for calling the <code>getEditPartFromDisplayCoordinates()</code>
		 *  method in order for this function to return <code>null</code> if the coordinates indicate the connectionPlaceHolder
		 */ 
		public function IFigureMeetsAdditionalConditions(disp:IFigure):Boolean {
			return disp != connectionPlaceHolder && disp.getEditPart() != diagramViewer.getRootEditPart();
		}
		
		/**
		 * Given <code>target</code> EditPart, searches an acceptable EditPart (one that can be linked with <coode>source</code> with 
		 * at least one connection) going recursively on parent EditParts.
		 * @flowerModelElementId _rlxmEEAqEeCEz721UNEmHg
		 * @private
		 */ 
		protected function getAcceptableEditPart(target:EditPart):EditPart {
			if (target == null) {
				return null;
			}
			
			if (target == targetEditPart) {
				return target; // same with the last accepted EditPart => keep it and stop checking
			}
			
			if (target is IConnectableEditPart) {
				if ( AbsoluteLayoutEditPart(diagramViewer.getRootEditPart()).isConnectionAceepted(IConnectableEditPart(sourceEditPart), IConnectableEditPart(target))) {
					return target;
				}
			} 
			
			return getAcceptableEditPart(target.getParent());
		}
		
		protected function getAcceptableEditPartOnSpecifiedXPosition(target:EditPart, startX:Number, endX:Number):EditPart {
			if (target == null) {
				return null;
			}
			
			if (target is IConnectableEditPart) {
				if ( AbsoluteLayoutEditPart(diagramViewer.getRootEditPart()).isConnectionAcceptedOnSpecifiedPosition(IConnectableEditPart(sourceEditPart), IConnectableEditPart(target), startX, endX)) {
					return target;
				}
			} 
			return getAcceptableEditPartOnSpecifiedXPosition(target.getParent(), startX, endX);
		}
		
		/**
		 * Retrieves a list of accepted ConnectionEditParts that can be created between <code>sourceEditPart</code> and <code>target</code>.
		 * @flowerModelElementId _rlxmFEAqEeCEz721UNEmHg
		 * @private
		 */ 
		public static function getAcceptedConnections(source:IConnectableEditPart, target:IConnectableEditPart):ArrayCollection {
			var connectionEditParts:ArrayCollection = new ArrayCollection;
			var acceptableOutgoingConnections:ArrayCollection = source.getAcceptedOutgoingConnectionEditParts();
			var acceptableIncommingConnections:ArrayCollection = target.getAcceptedIncommingConnectionEditParts();
			
			// compare the acceptable outgoing ConnectionEditParts and the acceptable incomming ones and retreive the intersection
			// of the two sets
			for (var i:int = 0; i < acceptableOutgoingConnections.length; i++) {
				for (var j:int = 0; j < acceptableIncommingConnections.length; j++) {
					if (acceptableIncommingConnections[j] == acceptableOutgoingConnections[i]) {
						connectionEditParts.addItem(acceptableOutgoingConnections[i]);
						break;
					}
				}			
			}
			
			return connectionEditParts;
		}
		
		/**
		 * Removes the connection placeholder and enables FlowerContextMenu.
		 * This should happen when DRAG_TO_CREATE_RELATION state is over or the creation is aborted 
		 * (see keyDownHandler when ESC is pressed and mouseUpHandler)
		 * 
		 * @flowerModelElementId _rlyNI0AqEeCEz721UNEmHg
		 * @private
		 */ 
		protected function endCreateRelation():void {
			// remove the connection place holder
			AbsolutePositionEditPartUtils.removeChildFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), IVisualElement(connectionPlaceHolder));
			// enable context menu
			ContextMenuManager.INSTANCE.updateContextMenuEnabled(getDiagramViewer(), true);
		}
		
		/**
		 * Creates an instance of <code>FlowerContextMenu</code> to be used in DRAG_TO_CREATE_RELATION state.
		 * It gets and sets the corresponding <code>ActionContext</code>, sets the before & after action execution functions.
		 * @author Cristina
		 */ 
		private function createRelationContextMenu(title:String):FlowerContextMenu {
			// create new context menu and set it's coordinates
			var createRelationMenu:FlowerContextMenu = new FlowerContextMenu();
			createRelationMenu.setTitle(title);
			createRelationMenu.show(true);
			createRelationMenu.closeAfterActionRun = true;
			createRelationMenu.setSelectionProvider(diagramViewer);
										
			// Obtain a specialized instance of context.
			var createRelationActionContext:ActionContext = diagramViewer.getCreateRelationActionProvider().getContext();
			// and delegate the filling to the client.
			diagramViewer.fillCreateRelationActionContext(createRelationActionContext, connectionPlaceHolder, sourceEditPart, targetEditPart);	

			// Before ContextMenu checks the visibility of the action, the context must first be assigned. 
			createRelationMenu.beforeActionVisibilityEvaluatedFunction = 
				function(action:IAction):void {
					action.context = createRelationActionContext;
				};
								
			createRelationMenu.afterActionExecutedFunction = 
				function(mainContextMenu:FlowerContextMenu):void {	
					// The connection place holder must be removed if still on diagram
					if (connectionPlaceHolder.parent != null) {
						endCreateRelation();
					}
				};		
			createRelationMenu.actionContext = createRelationActionContext;
			
			return createRelationMenu;
		}
		
		////////////////////////////////////////////
		
		/**
		 * Closes the opened creation menu. <br>
		 * If <code>updateContextMenu</code> is <code>true</code>, refreshes the viewer's context menu.
		 * @private
		 * @author Cristina
		 * @flowerModelElementId _iAawkKIuEeCwBaW0OK82xA
		 */ 
		private function closeLastOpenedCreationMenu(updateContextMenu:Boolean=false):void {
			if (lastOpenedCreationMenu != null) {
				lastOpenedCreationMenu.closeMainContextMenu();
				lastOpenedCreationMenu = null;
				if (updateContextMenu) {
					ContextMenuManager.INSTANCE.updateContextMenuEnabled(diagramViewer, true);
				}
			}
		}
		
		/**
		 * Removes the multiple selection rectangle if exists.
		 * @private
		 * @author Cristina
		 * @flowerModelElementId _iAawlaIuEeCwBaW0OK82xA
		 */ 
		private function removeMultipleSelectionRectangle():void {
			// The placeholder that represents the bounds of the to be created element, must be hidden when clicking again.
			if (AbsolutePositionEditPartUtils.containsFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), multipleSelectionRectangle) > 0) {
				// so next time when click down and up the context menu should not be shown again.
				// reset first its dimensions
				multipleSelectionRectangle.resetPositionAndDimensions();
				AbsolutePositionEditPartUtils.removeChildFigure(IVisualElementContainer(diagramViewer.getRootEditPart().getFigure()), multipleSelectionRectangle);
			}
		}
		
		/**
		 * Does not accept lock-unlock behavior
		 * @flowerModelElementId _SGIFEETOEeCbnenRcrXIzw
		 * @private
		 */ 
		override public function canLock():Boolean {
			return false;
		}
		
		/**
		 * Controls the selection mode.
		 * 
		 * <ul>
		 * 	<li>If "true", an element is selected even if it is "partially" contained by
		 * 		the selection rectangle (i.e. the selection rectangle intersects the element
		 * 		=> it is selected).
		 * 	<li>If "false", an element is selected only if it is fully contained by the 
		 * 		selection rectangle.
		 * </ul>
		 * 
		 * @see #SELECT_WHEN_PARTIAL_CONTAINMENT_DEFAULT 
		 * @flowerModelElementId _P7JHcGpuEeCjZqR9ugnK5Q
		 */
		public function get selectWhenPartialContainment():Boolean {
			return _selectWhenPartialContainment;
		}

		/**
		 * @flowerModelElementId _P7KVkWpuEeCjZqR9ugnK5Q
		 */
		public function set selectWhenPartialContainment(value:Boolean):void {
			_selectWhenPartialContainment = value;
		}
		
		/**
		 * The color for the selection rectangle when it is in "drag to select" mode.
		 * 
		 * @see #DRAG_TO_SELECT_COLOR_DEFAULT
		 * @flowerModelElementId _P7LjsWpuEeCjZqR9ugnK5Q
		 */ 
		public function get dragToSelectColor():uint {
			return _dragToSelectColor;
		}
		
		/**
		 * @flowerModelElementId _P7NY4WpuEeCjZqR9ugnK5Q
		 */
		public function set dragToSelectColor(value:uint):void {
			_dragToSelectColor = value;
		}
		
		/**
		 * The color for the selection rectangle when it is in "drag to create" mode.
		 * 
		 * @see #DRAG_TO_CREATE_COLOR_DEFAULT
		 * @flowerModelElementId _P7OnAWpuEeCjZqR9ugnK5Q
		 */ 
		public function get dragToCreateColor():uint {
			return _dragToCreateColor;
		}
		
		/**
		 * @flowerModelElementId _P7QcMGpuEeCjZqR9ugnK5Q
		 */
		public function set dragToCreateColor(value:uint):void {
			_dragToCreateColor = value;
		}
		
		/**
		 * The alpha value for the selection rectangle when it is in "drag to select" mode.
		 * 
		 * @see #DRAG_TO_SELECT_ALPHA_DEFAULT
		 * @flowerModelElementId _P7RqUWpuEeCjZqR9ugnK5Q
		 */ 
		public function get dragToSelectAlpha():Number {
			return _dragToSelectAlpha;
		} 
		
		/**
		 * @flowerModelElementId _P7SRYWpuEeCjZqR9ugnK5Q
		 */
		public function set dragToSelectAlpha(value:Number):void {
			_dragToSelectAlpha = value;
		}
		
		/**
		 * The alpha value for the selection rectangle when it is in "drag to create" mode.
		 * 
		 * @see #DRAG_TO_CREATE_ALPHA_DEFAULT
		 * @flowerModelElementId _P7UGkWpuEeCjZqR9ugnK5Q
		 */ 
		public function get dragToCreateAlpha():Number {
			return _dragToCreateAlpha;
		}

		/**
		 * @flowerModelElementId _P7VUsWpuEeCjZqR9ugnK5Q
		 */
		public function set dragToCreateAlpha(value:Number):void {
			_dragToCreateAlpha = value;
		}
		
		/**
		 * The width (in pixels) for the selection rectangle in order to trigger 
		 * a "drag to create" operation.
		 * 
		 * <p>
		 * Note that if the value is 0, elements can be created (i.e. the 
		 * create context menu shows) only by clicking.  
		 * 
		 * @see com.crispico.flower.flexdiagram.gantt.tools.GanttSelectMoveResizeTool#DRAG_TO_CREATE_MIN_WIDTH_DEFAULT
		 * @flowerModelElementId _Tvx2EHSHEeCMzr1ugdKqbA
		 */ 		
		public function get dragToCreateMinWidth():int {
			return _dragToCreateMinWidth;
		}

		/**
		 * @flowerModelElementId _Tvx2E3SHEeCMzr1ugdKqbA
		 */
		public function set dragToCreateMinWidth(value:int):void {
			_dragToCreateMinWidth = value;
		}
		
		/**
		 * The height (in pixels) for the selection rectangle in order to trigger 
		 * a "drag to create" operation.
		 * 
		 * <p>
		 * Note that if the value is 0, elements can be created (i.e. the 
		 * create context menu shows) only by clicking.
		 *   
		 * @see com.crispico.flower.flexdiagram.gantt.tools.GanttSelectMoveResizeTool#DRAG_TO_CREATE_MIN_HEIGHT_DEFAULT
		 * @flowerModelElementId _Tvx2FnSHEeCMzr1ugdKqbA
		 */ 		
		public function get dragToCreateMinHeight():int {
			return _dragToCreateMinHeight;
		}

		/**
		 * @flowerModelElementId _Tv7nEXSHEeCMzr1ugdKqbA
		 */
		public function set dragToCreateMinHeight(value:int):void {
			_dragToCreateMinHeight = value;
		}
		
		/**
		 * Returns true (or false) if the tool has (or has not) a feature for accepting the creation of a relation without no element target (in fact the target to be the diagram)
		 * Defaut returns true, but the subclasses can change this behavior
		 */ 
		protected function acceptRelationToNoTarget():Boolean {
			return true;
		} 
		
	}
}