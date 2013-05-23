package com.crispico.flower.flexdiagram.tool {
	
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IAbsoluteLayoutFigure;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.toolbar.Toolbar;
	
	import flash.display.DisplayObject;
	import flash.display.Stage;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.geom.Point;
	import flash.ui.Keyboard;
	import flash.utils.Timer;
	
	import mx.containers.dividedBoxClasses.BoxDivider;
	import mx.controls.scrollClasses.ScrollBar;
	import mx.core.Application;
	import mx.core.Container;
	import mx.core.UIComponent;
	import mx.managers.CursorManager;
	import mx.styles.CSSStyleDeclaration;
	import mx.styles.StyleManager;
	
	import spark.components.Group;
	
	/**
	 * 
	 *  Everytime an user interaction need to be captured (i.e. listeners
	 *  have to be installed), a <code>Tool</code> has to be used.
	 *  
	 *  The <code>Tool</code> listens and calls different EditPart methods in order
	 *  to delegate the control.
	 *  
	 *  There are 2 kinds of tools:
	 *  <ul>
	 *  		<li>mutually exclusive (only one of them can be activated at 
	 *  the same time); </li>
	 *  		<li>ones that can be activated no matter what other tool is 
	 *  active (be it mutually exclusive or not); </li>
	 *  </ul>
	 *  For more information see <code>Tool.activate()</code>	 * 
	 * @author Cristi
	 * @flowerModelElementId _bzkpq78REd6XgrpwHbbsYQ
	 */
	public class Tool {
	
		/**
		 * When some tool want to catch events after/before some other tool 
		 * we want to register events with different priority.
		 */
		protected var listenersPriority:int = 0; 
	 
		/**
		 * Generic editPart used by the current tool.
		 * @flowerModelElementId _b1D3Y78REd6XgrpwHbbsYQ
		 */
		protected var editPart:EditPart;
		
		/**
		 * The diagramViewer where the current
		 * tool is activated.
		 * @flowerModelElementId _b1D3Z78REd6XgrpwHbbsYQ
		 */
		protected var diagramViewer:DiagramViewer;
	
		/**
		 *  current cursor class => needed to set cursor if the required one is not currently set.
		 * @flowerModelElementId _b1D3a78REd6XgrpwHbbsYQ
		 */
		protected var currentCursor:Class = null;
		
		/**
		 * last cursor ID - needed because the cursor can also be set from outside of Tool
		 * @flowerModelElementId _b1D3cL8REd6XgrpwHbbsYQ
		 */ 
		protected var cursorID:int;
	
		/**
		 *  the cursor icon if the current operation is not allowed
		 * @flowerModelElementId _b1D3dL8REd6XgrpwHbbsYQ
		 */
		protected var rejectCursor:Class;
		
		/**
		 * the cursor icon if the current operation is allowed
		 * @flowerModelElementId _b1D3eb8REd6XgrpwHbbsYQ
		 */
		protected var copyCursor:Class;

		/**
		 * If the getIFigureFromDisplay() has encontered a ScrollBar.
		 * @flowerModelElementId _b1D3fr8REd6XgrpwHbbsYQ
		 */
		protected var wasScrollBarClicked:Boolean = false;
		
		/**
		 * The timer is started when the mouse leaves the visible
		 * area of the diagram and the mouse button is pressed. 
		 * It is stopped when the tool is deactivated or if the
		 * mouse enters the visible area.
		 * 
		 * The handler for a <code>TimerEvent.Timer</code> event
		 * updates the scroll positions
		 * @flowerModelElementId _XkSucM63Ed6Br7amU0X43w
		 */
		protected var autoScrollTimer:Timer = new Timer(100, 3000);
		
		/**
		 * The value added to the current horizontal scroll 
		 * position. It can be a positive or a negative value.
		 * The value is computed by the 
		 * <code>computeScrollbarIncrement()</code> method and
		 * it is used by the timer's handler.
		 * @flowerModelElementId _XkSuds63Ed6Br7amU0X43w
		 */
		private var horizontalScrollPositionIncrement:int = 0;
		
		/**
		 * The value added to the current vertical scroll
		 * position. 
		 * @see horizontalScrollPositionIncrement
		 * @flowerModelElementId _XkSue863Ed6Br7amU0X43w
		 */
		private var verticalScrollPositionIncrement:int = 0;
		
		/**
		 * For a tool that handles autoScroll the auto scroll is 
		 * triggered every time when move the mouse outside the 
		 * diagram visible area with a button pressed.
		 * 
		 * This flag it is used on mouse down to disable this feature
		 * for the next mouse move if we are on some special cases.  
		 * 
		 * @see #mouseDownHandler
		 * @author Daniela
		 */
		protected var mouseDownDisplayObjectDisableAutoScroll:Boolean = false;
		
		/**
		 * Set to true when the mouse is pressed, and to false when the
		 * mouse is realsed.
		 * Also, set to false when the tool is deactivated.
		 */
		protected var dragInProgress:Boolean = false;		
		
		public function Tool() {
			// TODO CS: am facut ac. lucru pentru compatibilitatea cu Flex 4 a Gantt; probabil tr. gasita o solutie
			// definitiva (caci in Gantt nu prea se folosesc aceste cursoare)
			
			// TODO: the usage of static method StyleManager.getStyleDeclaration is deprecated and should be replace with 
			// FlexGlobals.topLevelApplication.styleManager.getStyleDeclaration. But at this point the FlexGlobals.topLevelApplication
			// is null. Find another place to move the setting of the rejectCursor and of the copyCursor where the topLevelApplication should be available
			var styleDeclaration:CSSStyleDeclaration = StyleManager.getStyleDeclaration("mx.managers.DragManager");
			//var styleDeclaration:CSSStyleDeclaration = FlexGlobals.topLevelApplication.styleManager.getStyleDeclaration("DragManager");
			
			if (styleDeclaration != null) {
				rejectCursor = styleDeclaration.getStyle("rejectCursor");
				copyCursor = styleDeclaration.getStyle("copyCursor");
			}
		}	
			
		/**
		 * <p>
		 * Overridding methods should call super() at the begining (i.e. before doing their processing).	  
		 * 
		 * <p>
		 * Some tools are mutually exclusive, and only one can
		 * be active at the same time. These tools are activated 
		 * with <code>viewer.activateTool(tool)</code>
		 * 
		 * <p>
		 * Others don't interfere with the execution of other tools
		 * and can be activated while muttualy exclusive tools are 
		 * active. These tools are activated with 
		 * <code>tool.activate(viewer)</code>
		 * 
		 * <p>
		 * The method registers listeners for the following events:
		 * <ul>
		 * 		<li> MouseEvent.MOUSE_MOVE </li>
		 * 		<li> MouseEvent.MOUSE_DOWN </li>
		 * 		<li> MouseEvent.ROLL_IN </li>
		 * 		<li> MouseEvent.ROLL_OUT </li>
		 * 		<li> KeyboardEvent.KEY_DOWN </li>
		 * </ul>
		 * 
		 * @flowerModelElementId _b1D3g78REd6XgrpwHbbsYQ
		 */
		public function activate(diagramViewer:DiagramViewer):void {
			if (this.diagramViewer == null)
				this.diagramViewer = diagramViewer;
			else 
				if (this.diagramViewer != diagramViewer)
					throw new Error("Tool is already active on another diagram");	
			Application.application.stage.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler, false, listenersPriority);
			Application.application.stage.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler, false, listenersPriority);
			Application.application.stage.addEventListener(MouseEvent.MOUSE_UP, mouseUpHandler, false, listenersPriority);
			Application.application.stage.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler, false, listenersPriority);
			DisplayObject(diagramViewer.getRootEditPart().getFigure()).addEventListener(MouseEvent.ROLL_OUT, rollHandler, false, listenersPriority);
			DisplayObject(diagramViewer.getRootEditPart().getFigure()).addEventListener(MouseEvent.ROLL_OVER, rollHandler, false, listenersPriority);
			if (toolHandlesAutoScroll())
				autoScrollTimer.addEventListener(TimerEvent.TIMER, autoScrollTimerHandler);
		}
	
		/**
		 * Unregister all the listeners.
		 * Overridding methods should call super() at the end (i.e. after having done their processing).
		 * 
		 * @flowerModelElementId _7ZNJo-CUEeGdYcOEhSk3ug
		 */
		public function deactivate():void {
			if (toolHandlesAutoScroll()) {
				autoScrollTimer.stop();
				autoScrollTimer.removeEventListener(TimerEvent.TIMER, autoScrollTimerHandler);
			}
			DisplayObject(diagramViewer.getRootEditPart().getFigure()).removeEventListener(MouseEvent.ROLL_OUT, rollHandler);
			DisplayObject(diagramViewer.getRootEditPart().getFigure()).removeEventListener(MouseEvent.ROLL_OVER, rollHandler);		
			Application.application.stage.removeEventListener(MouseEvent.MOUSE_UP, mouseUpHandler);
			Application.application.stage.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveHandler);
			Application.application.stage.removeEventListener(MouseEvent.MOUSE_DOWN, mouseDownHandler);
			Application.application.stage.removeEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler);
			// reset current diagram viewer
			diagramViewer = null;
			dragInProgress = false; 
		}
		
		/**
		 * The method is called when a <code>MouseEvent.MOUSE_DOWN</code> event was 
		 * fired.
		 * 
		 * This method decides if the future autoscroll will be disabled or not
		 * 
		 * The <code>mouseDownDisplayObjectDisableAutoScroll</code> value is used by the
		 *  <code>computeScrollbarIncrement()</code>method.
		 *  If this value is not true, no scrollbar correction is computed.
		 * 
		 * @author Daniela
		 * @see #computeScrollbarIncrement()
		 *  
		 * @flowerModelElementId _XkSuhc63Ed6Br7amU0X43w
		 */
		protected function mouseDownHandler(event:MouseEvent):void {
			dragInProgress = true;
			
			var mouseDownDisplayObject:Object = event.target;
			mouseDownDisplayObjectDisableAutoScroll = false;
			var mousePoint:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
			
			//we disable the autoscroll outside the diagram
			if (!inDiagramVisibleArea(mousePoint.x, mousePoint.y)) {
				mouseDownDisplayObjectDisableAutoScroll = true;
				return;
			}
			
			if (mouseDownDisplayObject is Toolbar || 
				mouseDownDisplayObject is ScrollBar ||
				mouseDownDisplayObject is BoxDivider)
				mouseDownDisplayObjectDisableAutoScroll = true;
		}
		
		/**
		 * The method is called when a <code>MouseEvent.MOUSE_MOVE</code> event was
		 * fired. If the mouse button is clicked, call <code>computeScrollbarIncrement()</code>
		 * method to compute wheather a scrollbar correction is needed or not.
		 * 
		 * @flowerModelElementId _bl2IIM3iEd6WxI387H-vcA
		 */
		protected function mouseMoveHandler(event:MouseEvent):void { 
			var rootFigure:IAbsoluteLayoutFigure = IAbsoluteLayoutFigure(diagramViewer.getRootEditPart().getFigure()); 
			var visibleArray:Array = rootFigure.getVisibleAreaRect();
			var p:Point = globalToDiagram(Math.ceil(event.stageX), Math.ceil(event.stageY));
			if (event.buttonDown) 
				computeScrollbarIncrement(p);
		}
		
		/**
		 * The method is called when a <code>MouseEvent.MOUSE_UP</code> event was
		 * fired. If the current target of the event is the Stage and the timer is
		 * running it is stopped (the mouse button was released outside).
		 * @flowerModelElementId _0pBfYPqdEd618vxgakeTjA
		 */
		protected function mouseUpHandler(event:MouseEvent):void {
			if (event != null && event.currentTarget is Stage)
				if (toolHandlesAutoScroll()) {
					if (autoScrollTimer.running)
						autoScrollTimer.stop();
				}
			dragInProgress = false;
		}
		
		/**
		 * The method is called when a <code>Keyboard.KEY_DOWN</code> event was fired.
		 * It stops the autoscroll operation (if it is in progress) 
		 * if the ESC key was pressed.
		 * 
		 * @flowerModelElementId _-lK6QM69Ed695OmK6ZsozA
		 */
		protected function keyDownHandler(event:KeyboardEvent):void {
			if (event.keyCode == Keyboard.ESCAPE) 
				if (toolHandlesAutoScroll())
					autoScrollTimer.stop();
		}
		
		/**
		 * The method checks if the current mouse position is inside
		 * or outside the visible area. If the event was generated by 
		 * scrollbar, toolbar or context menu, there is no need to check
		 * if the mouse is inside the visible area.
		 * 
		 * @flowerModelElementId _XkSuis63Ed6Br7amU0X43w
		 */
		protected function computeScrollbarIncrement(crtPoint:Point):void {
			horizontalScrollPositionIncrement = 0;
			verticalScrollPositionIncrement = 0;
			if (mouseDownDisplayObjectDisableAutoScroll) 
				return;
			var rootFigure:IAbsoluteLayoutFigure = IAbsoluteLayoutFigure(diagramViewer.getRootEditPart().getFigure());
			var visibleArray:Array = rootFigure.getVisibleAreaRect();
			if (crtPoint.x <= visibleArray[0]) { // crtPoint.x < x
				horizontalScrollPositionIncrement = -5;
				if (crtPoint.y <= visibleArray[1]) // crtPoint.y < y
					verticalScrollPositionIncrement = -5;
				else if (crtPoint.y >= visibleArray[1] + visibleArray[3]) // crtPoint.y > y + height	
					verticalScrollPositionIncrement = 5;
			} else if (crtPoint.x >= visibleArray[0] + visibleArray[2]) { // crtPoint.x > x + width
				horizontalScrollPositionIncrement = 5;
				if (crtPoint.y <= visibleArray[1]) // crtPoint.y < y
					verticalScrollPositionIncrement = -5;
				else if (crtPoint.y >= visibleArray[1] + visibleArray[3]) // crtPoint.y > y + height
					verticalScrollPositionIncrement = 5;
			} else if (crtPoint.y <= visibleArray[1]) {// crtPoint.y < y
				verticalScrollPositionIncrement = -5;
			} else if (crtPoint.y >= visibleArray[1] + visibleArray[3]) { // crtPoint.y > y + height
				verticalScrollPositionIncrement = 5;
			}
			
			if (toolHandlesAutoScroll()) {
				if (horizontalScrollPositionIncrement != 0 || verticalScrollPositionIncrement != 0) {
					// at least one of the scrollbar needs to be updated
					if (!autoScrollTimer.running)
						autoScrollTimer.start();
				} else {
					if (autoScrollTimer.running)
						autoScrollTimer.stop();
				}
			}
		}
		
		/**
		 * The method is called every 100 miliseconds and it 
		 * updates the scroll positions
		 * @flowerModelElementId _XkSukM63Ed6Br7amU0X43w
		 */
		protected function autoScrollTimerHandler(event:TimerEvent):void {
//			var rootFigure:Container = Container(diagramViewer.getRootEditPart().getFigure());
//			rootFigure.horizontalScrollPosition += horizontalScrollPositionIncrement;
//			rootFigure.verticalScrollPosition += verticalScrollPositionIncrement; 
//			rootFigure.validateNow();
		}
		
		/**
		 * The method is called when a ROLL_OUT or ROLL_IN event
		 * is fired. It controlls the timer
		 * @flowerModelElementId _XkcfdM63Ed6Br7amU0X43w
		 */ 
		protected function rollHandler(event:MouseEvent):void {
			if (event.type == MouseEvent.ROLL_OVER) {
				// the mouse is inside the viewport area
				if (toolHandlesAutoScroll())
					autoScrollTimer.stop();
				event.stopImmediatePropagation();
			}
		}

		/**
		 * @flowerModelElementId _b1D3jr8REd6XgrpwHbbsYQ
		 */
		protected function updateCursor(cursor:Class, priority:int = 1, xOffSet:Number = 0, yOffSet:Number = 0):void {
			if (cursor != null) {
				if (currentCursor != cursor 
					|| UIComponent(diagramViewer.getRootFigure()).cursorManager.currentCursorID == CursorManager.NO_CURSOR 
					|| cursorID != UIComponent(diagramViewer.getRootFigure()).cursorManager.currentCursorID) {
					UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors();
						cursorID = UIComponent(diagramViewer.getRootFigure()).cursorManager.setCursor(cursor, priority, xOffSet, yOffSet);
						currentCursor = cursor;
				} 
			} else { 
				// set the cursor to the normal arrow 
				// only if it is not already normal arrow
				if (UIComponent(diagramViewer.getRootFigure()).cursorManager.currentCursorID != CursorManager.NO_CURSOR) {
					UIComponent(diagramViewer.getRootFigure()).cursorManager.removeAllCursors();
				}
			}
		}
		
		
				
		/**
		 * Retrieves an EditPart under the given coordinates if any. 
		 * Returns <code>null</code> if none found.
		 * 
		 * The callers of this method can add restriction to the founded IFigures by using the parameter
		 * IFigureMeetsAdditionalConditionsFunction.
		 * 
		 * If the IFigureMeetsAdditionalConditionsFunction returns true it meens that 
		 * the current founded  IFigure is accepted and the getEditPartFromDisplayCoordinates method will return
		 * its editPart, else the search continue for another object under the mouse. 
		 * By default there are no additional conditions
		 * 
		 * @flowerModelElementId _R-u7sDqeEeCcnaPb1Tzsvw
		 * @private
		 */ 		
		protected function getEditPartFromDisplayCoordinates(x:int, y:int, IFigureMeetsAdditionalConditionsFunction:Function = null):EditPart {
			//return all the objects under the current mouse position
			var arr:Array = DisplayObject(diagramViewer.getRootEditPart().getFigure()).stage.getObjectsUnderPoint(new Point(x, y));
			var disp:IFigure;
			var i:int;
			for (i = arr.length - 1; i >= 0;  i--) {
				if (arr[i] is ScrollBar) {
					// scroll bar is not a place to drop but to scroll
					return null;
				}
				
				disp = getIFigureFromDisplay(arr[i]);
				if (disp != null) {
					if (IFigureMeetsAdditionalConditionsFunction != null && !IFigureMeetsAdditionalConditionsFunction(disp))
						continue;
					return disp.getEditPart();
				}
			}
			return null;
		}
		
		/** 
		 * Utility function that iterates over all the parents of 
		 * obj until it founds a figure, and then returns the figure's
		 * EditPart
		 * @flowerModelElementId _bztzkb8REd6XgrpwHbbsYQ
		 */
		protected function getEditPartFromDisplay(obj:Object):EditPart {
			var fig:IFigure = getIFigureFromDisplay(obj);
			if (fig != null) {
				return fig.getEditPart();
			}
			return null;
		}
		
		/**
		 * Utility function that traverse a DisplayObject's hierarcy
		 * searching for an object of IFigure type
		 * 
		 * Returns null if no such oject is found.
		 * @flowerModelElementId _b1D3k78REd6XgrpwHbbsYQ
		 */
		protected function getIFigureFromDisplay(obj:Object):IFigure {
			wasScrollBarClicked = false;
			
			// in order for us to traverse its hierrarchy
			// it has to be a DisplayObject
			if (!(obj is DisplayObject)) {
				return null;
			}
			
			// traverse all the obj's hierarchy	
			while (obj != null) {
				// scroll bar is not a place to drop but to scroll
				if (obj is ScrollBar) {
					wasScrollBarClicked = true;
					return null;
				}
				if (obj is IFigure) {
					// found it
					return IFigure(obj);
				}
				obj = DisplayObject(obj).parent;
			}
			
			// no IFigure found on the obj's hierarchy
			return null;
		}
		
		/**
		 * Converts from stage (global) coordinates to Diagram related coordinates
		 * @flowerModelElementId _b1D3mb8REd6XgrpwHbbsYQ
		 */
		protected function globalToDiagram(x:Number, y:Number):Point { 
			var localPoint:Point = UIComponent(diagramViewer.getRootEditPart().getFigure()).globalToLocal(new Point(x, y));
			localPoint = UIComponent(diagramViewer.getRootEditPart().getFigure()).localToContent(localPoint);
			return localPoint;
		}
		
		/**
		 * Get the diagramViewer where this tool is currently activated
		 * @flowerModelElementId _b1NoYr8REd6XgrpwHbbsYQ
		 */
		public function getDiagramViewer():DiagramViewer {
			return diagramViewer;
		}
		/**
		 * The icon of the Tool.
		 * (e.g. to be shown in the toolbar next to the Tool name.)
		 * @flowerModelElementId _zHZ40MK8Ed6-yKDFMO4rZg
		 */
		public function getIcon():Class {
			throw new Error("getIcon() should be implemented.");
		}
		/**
		 * Returns the label of the tool. 
		 * (e.g. to be shown in the Toolbar.)
		 * @flowerModelElementId _zVkQoMK8Ed6-yKDFMO4rZg
		 */
		public function getLabel():String {
			throw new Error("getLabel() should be implemented.");
		}
		
		/**
		 * It can be usefull from outside to know when the tool is in drag state. 
		 * E.g. Figures for glow activation/deactivation
		 * 
		 * @author mircea
		 */
		public function isMouseDragInProgress():Boolean {
			return dragInProgress;
		}
		
		/**
		 * This function should be called by Tools that support lock-unlock behavior to announce when
		 * they finished working. An unlocked Tool will be deactivated as soon as its job is over.
		 * 
		 * <p>
		 * Note that there are Tools that work in locked behavior by default: they must not dispatch events when finished
		 * because their work can be declared ended only by user when selecting another Tool (see SelectMoveResizeTool or ZoomTool).
		 * However,(see CreateElementTool) there are Tools that have a well delimited action like creation Tools.
		 * For theese, the job ends when the user finished a drag, a drop, etc.
		 * 
		 * @see #canLock();
		 * 
		 * @author Luiza
		 * @author Cristi
		 * @flowerModelElementId _e_pQ4EPsEeCByZSKEK5nkQ
		 */
		protected function jobFinished():void {
			diagramViewer.jobFinishedForExclusiveTool(this);
		}
		
		/**
		 * Subclasses must override this function to define the lock-unlock behavior. If a Tool supports this behavior then
		 * it also must call jobFinished() when its work is done.
		 * <p>
		 * An unlocked Toll will automatically be deactivated when its job is finished. However, on locked mode, it will not be
		 * deactivated until the user doesn't choose another Tool.
		 * 
		 * Tools that work on locked behavior should return <code>false</code>. Creation Tools should return <code>true</code>.
		 * 
		 * @see #jobFinished()
		 * 
		 * @author Luiza
		 * @flowerModelElementId _SGX8sETOEeCbnenRcrXIzw
		 */ 
		public function canLock():Boolean {
			throw new Error("canLock() should be implemented. ");
		}
		
		/**
		 * For a tool by default the autoscroll is enabled
		 * A subclass tool can override thiss method if it wants to deactivate
		 * the autoscroll
		 */ 
		protected function toolHandlesAutoScroll():Boolean {
			return true;
		}
		
		/**
		 * Returnes <code>true</code> if the given coordinates are inside the visible area of the diagram.
		 * The coordinates must be in diagram's coordinate space obtained with 
		 * globalToDiagram().
		 * 
		 * @flowerModelElementId _o0vGUb_MEd-r8cFez7U8gg
		 * @private
		 */
		protected function inDiagramVisibleArea(x:int, y:int):Boolean {
			if (diagramViewer.getRootEditPart().getFigure() is IAbsoluteLayoutFigure) {
				var rootFigure:IAbsoluteLayoutFigure = IAbsoluteLayoutFigure(diagramViewer.getRootEditPart().getFigure());
				var visibleArray:Array = rootFigure.getVisibleAreaRect();
				// Root figures have a scale factor
				if (x < visibleArray[0] || x > visibleArray[0] + visibleArray[2]) return false;
				if (y < visibleArray[1] || y > visibleArray[1] + visibleArray[3]) return false;
				return true;
			}
			else {
				var figure:Container = Container(diagramViewer.getRootEditPart().getFigure());
				if (x < figure.horizontalScrollPosition || x > figure.width) 
					return false;
				if (y < figure.verticalScrollPosition || y > figure.height) 
					return false;
				return true;
			}
			return false;
		}
	}
}