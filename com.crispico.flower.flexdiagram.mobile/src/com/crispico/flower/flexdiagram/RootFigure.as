package com.crispico.flower.flexdiagram {
	import com.crispico.flower.flexdiagram.contextmenu.ContextMenuManager;
	
	import flash.display.DisplayObjectContainer;
	import flash.geom.Point;
	
	import mx.containers.Canvas;
	import mx.core.IVisualElementContainer;
	import mx.core.ScrollPolicy;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	import mx.events.PropertyChangeEventKind;
	import mx.events.ResizeEvent;
	import mx.events.ScrollEvent;
	import mx.managers.IFocusManagerComponent;
	
	import spark.components.Group;
	
	use namespace mx_internal; 
	/**
	 * Container for diagram figure. The class provides methods for 
	 * zoom, to control the scrollbars positions and to access (add/remove
	 * elements to) the diagram.
	 * 
	 * @author Georgi
	 * @flowerModelElementId _b6H9t78REd6XgrpwHbbsYQ
	 */ 
	[SecureSWF(rename="off")]
	public class RootFigure extends Group implements IAbsoluteLayoutFigure, IComposedFigure, IFocusManagerComponent {
		
		private const MIN_GRID_SIZE:int = 15;
		
		private const MAX_GRID_SIZE:int = 30;
		
		/**
		 * The diagram containing the classes/interfaces and 
		 * connections.
		 * @flowerModelElementId _IggjmsVeEd6x1dpkaVcaXg
		 */
		protected var diagramContent:DiagramContent = new DiagramContent();
		
		/**
		 * The grid displayed in the back of the figures will always exist but it will be 
		 * visible or not accourding to a style.
		 * 
		 * @author Luiza
		 */
		private var _grid:RectangularGrid;
		
		/**
		 * The grid's square size. This is computed internally, accourding to the 
		 * _snapSize and the diagram scale.
		 * 
		 * @author Luiza
		 */ 
		private var _gridSize:int = 24;
		
		/**
		 * The snap value. This is set by user and defines the snap for this diagram at 
		 * scale 100%. 
		 * 
		 * Figures should not use this value when they apply snap to grid 
		 * during move/resize operations.
		 * 
		 * @author Luiza
		 */ 
		private var _snapSize:int = 12;
		
		/**
		 * This is the actual snap value computed from the initial #snapSize, accourding to the
		 * diagram scale factor.
		 * 
		 * Figures should use this when apply snap to grid during move/resize operations.
		 * 
		 * @author Luiza
		 */ 
		private var _currentSnapSize:int;				
		
		/**
		 * Numeric constant that will help at on #currentSnapSize computation.
		 * When applyed will push the snapSize faster towards 1 so it gets to 1 when scale is around a predefined vale (let's say 3),
		 * or to slower the growth when scaling down towards 0.1 (this way it will not get to very big values when the scale is 
		 * very small);
		 * 
		 * Note: the computed value when snapSize is 6 is 0.5.
		 */
		private var snapShift:Number = 0.5;
				
		/**
		 * Current zoom percent. 
		 * @flowerModelElementId _Iggjn8VeEd6x1dpkaVcaXg
		 */
		private var scaleFactor:Number = 1;
		
		/**
		 * Minimum zoom percent : 0.1
		 * @flowerModelElementId _IggjpMVeEd6x1dpkaVcaXg
		 */
		private const MIN_SCALE:Number = 0.1; 
		
		/**
		 * Maximum zoom percent: 4
		 * @flowerModelElementId _IggjqcVeEd6x1dpkaVcaXg
		 */
		private const MAX_SCALE:Number = 4; 
		
		/**
		 * Indicates that the diagram must use printW and printH when computing the visualAreaRect()
		 * 
		 * @author Luiza
		 */ 
		public var printSize:Boolean = false;
		
		/**
		 * The with of the diagram in full size (having all its figures visible)
		 * 
		 * @author Luiza
		 */ 
		public var printW:Number = 0;
		
		/**
		 * The height of the diagram in full size (having all its children visible)
		 * 
		 * @author Luiza
		 */ 
		public var printH:Number = 0;
		
		public var useGrid:Boolean;
		
		/**
		 * @flowerModelElementId _b6H9v78REd6XgrpwHbbsYQ
		 */
		public function RootFigure(useGrid:Boolean = true, useBackgroundColor:Boolean = true) {
			super();
			this.useGrid = useGrid;				
			// set the default background = white
			if (useBackgroundColor) {
				setStyle("backgroundColor", 0xFFFFFF);
			}
			
			addEventListener(ScrollEvent.SCROLL, scrollHandler);
			addEventListener(ResizeEvent.RESIZE, resizeHandler);
		}
		
		public function get snapSize():int {
			return _snapSize;
		}
		
		public function set snapSize(value:int):void {
			
			if (_snapSize != value && value > 0) {
				_snapSize = value;
				// this is the max scale that marks the point where snapSize becomes 1
				// it actually happens with 0.5 earlier because of the roundings (aproximation)		
				var maxScaleForMinSnap:Number = 3.5;
						
				var maxSnapShift:Number = snapSize / maxScaleForMinSnap;
			
				// this is a constant that will help at the new snapSize computation
				// when applyed will push the snap size faster towards 1 so it gets to 1 when scale is around maxScaleForMonSnap
				// this will also help when scaling down to grow slower when scale goes towards 0.1
				snapShift = (maxSnapShift - 1) / (maxScaleForMinSnap - 1);
			}
		}
		
		public function get currentSnapSize():int {
			return _currentSnapSize;
		}
						
		override protected function createChildren():void {
			super.createChildren();
			if (useGrid) {				
				// add a name to be identified for eligible style
				grid.name = "grid";
				// add first the grid - to be displayed in background
				grid.size = this._gridSize;
				// draw dashed by default
				grid.dashSize = 1;
	
				grid.x = grid.y = 0;
				
				addChild(grid);
			}
			
			// set the diagram content background completely transparent
			diagramContent.setStyle("backgroundAlpha", 0);
			diagramContent.x = diagramContent.y = 0;
			
			// then add the diagramContent
			addElement(diagramContent);
		}
		
		/**
		 * @author Sorin
		 * @flowerModelElementId _MQTOANTTEd6xf5x0n0rk9Q
		 * 
		 *	Daniela: Made public because when programatically changing the 
		 * <code>verticalScrollPosition</code> or the <code>horizontalScrollPosition</code>
		 * the scroll event it isn't triggered. In this case this handler needs to be called
		 * explicitly.
		 */
		public function scrollHandler(event:ScrollEvent):void {
			if (getEditPart() != null) {
				getEditPart().refreshVisualChildren();
				// See handler for details. Basicly it waits for the scroll position to be commited in order to update Context Menu position.
				addEventListener(FlexEvent.UPDATE_COMPLETE, updateCompleteAfterScrollHandler);
			}
		}
		
		/**
		 * @flowerModelElementId _MQTOBtTTEd6xf5x0n0rk9Q
		 */
		protected function resizeHandler(event:ResizeEvent):void {
			if (getEditPart() != null)
				getEditPart().refreshVisualChildren();
			sizeGrid();
		}
		
		/**
		 * In order to update the position of the Context Menu when scrolling,
		 * we need to be notified when the scrollbar positions has been changed. This changing
		 * happens in the <code>updateDisplayList</code> phase, so after we can call 
		 * the Context Menu framework to update the position for associated Context Menu.
		 * 
		 * <p/> The adding of this listener is not done from the beggining of existance of
		 * this component, in order not to do unnecesarry processing, and we only add this listener
		 * after detecting a scroll and we remove the update complete listener as soon as we are notified
		 * about one.
		 * @author Sorin  
		 * @flowerModelElementId _ARUvANU2Ed-_pe20qTUaAg
		 */ 
		private function updateCompleteAfterScrollHandler(event:FlexEvent):void {
			removeEventListener(FlexEvent.UPDATE_COMPLETE, updateCompleteAfterScrollHandler);
			ContextMenuManager.INSTANCE.updatePosition(getEditPart().getViewer());
		}
		
		/**
		 * Returns the EditPart of the diagram content.
		 * @flowerModelElementId _b6H9zL8REd6XgrpwHbbsYQ
		 */
		public function getEditPart():EditPart {
			return diagramContent.getEditPart();
		}
		
		/**
		 * Setts the EditPart for the diagram content.
		 * @flowerModelElementId _b6H90L8REd6XgrpwHbbsYQ
		 */
		public function setEditPart(newEditPart:EditPart):void{
			diagramContent.setEditPart(newEditPart);
		}
		
		/**
		 * The method return the visible view port. The dimensions take in consideration
		 * the current zoom percent.
		 * @flowerModelElementId _b6H91b8REd6XgrpwHbbsYQ
		 */
		public function getVisibleAreaRect():Array {
			// this workaround is needed because verticalScrollPosition can go beyond the limits of the scrollbar
			// during wheel scroll; we cannot use directly scrollBar.scrollPosition neither because during wheel
			// scroll its value is not yet updated (i.e. there is a discrepancy between the 2 values and the one from
			// the container is the right one this time)
			// TODO CC: mobile support
//			var verticalScrollPosition:int = this.verticalScrollPosition;
//			if (verticalScrollPosition < 0)
//				verticalScrollPosition = 0;
//			else if (verticalScrollBar != null && verticalScrollPosition > verticalScrollBar.maxScrollPosition)
//				verticalScrollPosition = verticalScrollBar.maxScrollPosition;
//			
			if (!printSize) {
				return [horizontalScrollPosition / scaleFactor, verticalScrollPosition / scaleFactor, width / scaleFactor, height / scaleFactor];
			} else {
				return [0, 0, printW, printH];
			}
		}
		
		/**
		 * @flowerModelElementId _imIUkEstEeCbK9Qdqt9oUA
		 */
		public function setScrollableArea(maxScrollWidth:Number, maxScrollHeight:Number):void {
			diagramContent.setMaxScrollDimensions(maxScrollWidth, maxScrollHeight);
		}
		
		/**
		 * @author Daniela
		 * Unlike setScrollableArea function this function doesn't set only the measurewidth 
		 * and measuredHeight of the diagram content, it sets its width and height.
		 * 
		 * So when the ScrollableArea it is set throw this function, the dimensions setted by 
		 * setScrollableArea function are ignored.
		 * 
		 * This can be used for setting the rootFigure dimensions without taking in account 
		 * its child figures dimensions
		 */ 
		public function setImperativeScrollableArea(maxScrollWidth:Number, maxScrollHeight:Number):void {
			diagramContent.width = maxScrollWidth + 5 / diagramContent.scaleX;
			diagramContent.height = maxScrollHeight + 5 / diagramContent.scaleY;
		}
		
		/**
		 * The method returns the current zoom percent. It is called
		 * from classes that use <code>DisplayObject.getBounds()</code>
		 * method in order to take in consideration the current zoom
		 * percent.
		 * @flowerModelElementId _MQTOC9TTEd6xf5x0n0rk9Q
		 */
		public function getScaleFactor():Number {
			return scaleFactor;
		}
		
		/**
		 * The method used to maintain the zoom percent between the 
		 * limits defined by MIN_SCALE (0.1) and MAX_SCALE (4).
		 * It is called from <code>zoomToRectangle()</code> and
		 * <code>zoomToPercent()</code> methods.
		 * @flowerModelElementId _IggjsMVeEd6x1dpkaVcaXg
		 */
		private function enforceLimits():void {
			if (scaleFactor < MIN_SCALE)
				scaleFactor = MIN_SCALE;
			else if (scaleFactor > MAX_SCALE)
				scaleFactor = MAX_SCALE;
		}
		
		/**
		 * The method does the following actions:
		 * <ul>
		 * 		<li> scale the diagram content using the specified
		 * 			current zoom percent; </li>
		 * 		<li> update the scrollbar positions; </li>
		 * 		<li> update the zoom combo box (if there is any);</li>
		 * </ul>
		 * @flowerModelElementId _IgptgMVeEd6x1dpkaVcaXg
		 */
		private function scaleAndUpdateScrollBarPositions(dx:Number, dy:Number):void {
			diagramContent.scaleX = scaleFactor;
			diagramContent.scaleY = scaleFactor;
			
			// no need to call invalidateSize() on diagramContent as it is already called when scale changes
			applyScaleOnGrid(scaleFactor);
			// TODO CC: mobile support
//			if (horizontalScrollBar == null && getExplicitOrMeasuredWidth() > width) {
//				// horizontal scrollbar not present but needed => add it
//				horizontalScrollPolicy = ScrollPolicy.ON;
//			} else if (horizontalScrollBar != null && getExplicitOrMeasuredWidth() < width){
//				// horizontal scrollbat present and not needed anymore => remove it
//				horizontalScrollPolicy = ScrollPolicy.OFF;
//			}
//			if (verticalScrollBar == null && getExplicitOrMeasuredHeight() > height) {
//				// vertical scrollbar not present but needed => add it
//				verticalScrollPolicy = ScrollPolicy.ON;
//			} else if (verticalScrollBar != null && getExplicitOrMeasuredHeight() < height) {
//				// vertical scrollbat present and not needed anymore => remove it
//				verticalScrollPolicy = ScrollPolicy.OFF;
//			}
			// the call to <code>validateNow()<code> method is required because the 
			// scrollbars should be created and the canvas should update its new (scaled) sizes
			// before setting a scroll position (otherwise, new set scrollposition > current 
			// maxScrollPostion => and the value that we try to set appears to be out of the
			// valid interval and will not be set)
			//validateNow();			
			
			// actually invalidateSize performs even better with the same results.
			// @author Luiza
			// TODO: daca nu apar pobleme in urmatoarea perioada se va sterge definitiv comentariul de mai sus
			invalidateSize();
			
			horizontalScrollPosition += dx; 
			verticalScrollPosition += dy;
			
			//notify zoom combo box
			var notification:PropertyChangeEvent = new PropertyChangeEvent(PropertyChangeEvent.PROPERTY_CHANGE, false, false, PropertyChangeEventKind.UPDATE, "scaleFactor", null, (scaleFactor * 100).toFixed(2) + "%");
			dispatchEvent(notification);
		}
		
		/**
		 * The method computes the zoom percent needed using the width and 
		 * height of the room rectangle and calls <code>scaleAndUpdateScrollBarPositions</code>
		 * method to perform the scale action.
		 * @flowerModelElementId _Igpth8VeEd6x1dpkaVcaXg
		 */
		public function zoomToRectangle(x:Number, y:Number, width:Number, height:Number):void {
			var percent:Number = Math.min(unscaledHeight / height, unscaledWidth / width);
			if ((percent >= MAX_SCALE && scaleFactor == MAX_SCALE) ||
				(percent >= MIN_SCALE && scaleFactor == MIN_SCALE)) {
					scaleAndUpdateScrollBarPositions(- horizontalScrollPosition + x * scaleFactor, - verticalScrollPosition + y * scaleFactor);
			} else {	
				scaleFactor = percent;
				enforceLimits();
				scaleAndUpdateScrollBarPositions(- horizontalScrollPosition + x * scaleFactor, - verticalScrollPosition + y * scaleFactor);
			}
		}
		
		/**
		 * The method computes the zoom percent needed using the percent received
		 * as parameter:
		 * 		<li> if 'delta' parameter is set, the given percent needs to be 
		 * 			added to the current zoom percent and the zoom effect should
		 * 			be started </li>
		 * 		<li> if 'delta' parameter is not set, the given percent becomes 
		 * 			the current zoom percent; in this case x and y values are 
		 * 			used to compute the increment value used to update the scroll
		 * 			bars position; </li>
		 * @param x and y represent the mouseX and mouseY position before the zoom
		 * 		operation
		 * 
		 * @flowerModelElementId _IgptkMVeEd6x1dpkaVcaXg
		 */ 
		public function zoomToPercent(x:Number, y:Number, percent:Number, delta:Boolean=false):void {
			var oldPercent:Number = scaleFactor;
			var dx:Number = 0;
			var dy:Number = 0;
			if (delta) {
				scaleFactor += percent;
				enforceLimits();
				scaleAndUpdateScrollBarPositions(x * scaleFactor / oldPercent - x, y * scaleFactor / oldPercent - y);
			} else {
				scaleFactor = percent;
				enforceLimits();
				scaleAndUpdateScrollBarPositions(-horizontalScrollPosition, -verticalScrollPosition);
			}
			// refresh viasual children to add figures becoming visible when zoom out or to remove figures
			// when they become invisible on zoom in
			getEditPart().refreshVisualChildren();
		}
			
		/**
		 * Override needed to take in consideration the current zoom percent.
		 * Called from <code>Tool()</code> class.
		 * @flowerModelElementId _Igpts8VeEd6x1dpkaVcaXg
		 */
		override public function localToContent(point:Point):Point {
			var p:Point = super.localToContent(point);
			p.x = Math.ceil(p.x / scaleFactor);
			p.y = Math.ceil(p.y / scaleFactor);
			return p;
		}
		
		
		public function getVisualChildrenContainer():IVisualElementContainer {
			return diagramContent;
		}
		
		override protected function measure():void {
			super.measure();
			sizeGrid();
		}
		
		private function sizeGrid():void {
			if (grid != null && grid.visible) {
				grid.width = Math.max(diagramContent.measuredWidth, width);
				grid.height = Math.max(diagramContent.measuredHeight, height);
			}
		}
		
		/**
		 * Brings the given number - <code>num</code> to the closer multiple of <code>multipleOf</code>.
		 * It can decrease or increase the number to obtain it.
		 */ 
		public function clampToMultipleOf(num:Number, multipleOf:int):int {
			var rest:int = int(num) % multipleOf;
			
			if (multipleOf - rest  <= multipleOf / 2) {
				return int(num) + multipleOf - rest;
			} else {
				return int(num) - rest;
			}
		}
		
		/**
		 * Brings the given number - <code>num</code> to the closer divider of <code>dividerOf</code>.
		 * It can decrease or increase the number to obtain it, but it chooses the value closest to the given one.
		 */ 
		public function clampToDividerOf(num:int, dividerOf:int):int {
			if (dividerOf % num == 0) {
				// already a divider
				return num;
				
			} else {
				var dividerBig:int = num + 1;
				// try to find nex divider by growing the number
				for (var i:int = dividerBig; i <= dividerOf; i++) {
					if (dividerOf % i == 0) {
						dividerBig = i;
						break;
					}
				}
				
				// try to find the next divider by decreasing the number
				var dividerSmall:int = num - 1;
				
				for (i = dividerSmall; i >= 1; i--) {
					if (dividerOf % i == 0) {
						dividerSmall = i;
						break;
					}
				}
				
				if (dividerBig - num <= dividerOf - dividerSmall) {
					return dividerBig;
				} else {
					return dividerSmall;
				}
			}
		}
		
		/**
		 * Updates the grid and the snap size accourding to the new scale factor.
		 * 
		 * @author Luiza
		 */ 	
		private function applyScaleOnGrid(scaleFactor:Number):void {			
			if (grid == null || grid.visible == false) {
				return; // not necessary when the grid does not show
			}
						
			// compute a new snap size		
			var newSnapSize:Number = snapSize;			 
			
			if (scaleFactor <= 1) {
				// the snap size must still be a multiple of the original snap but still it must change inverse proportionally with the scale
				// => snap grows as the scale goes down and figures are displayed smaller
				newSnapSize = clampToMultipleOf(Math.round(snapSize / (scaleFactor * (1 + snapShift * (scaleFactor - 1)))), snapSize);
				newSnapSize = Math.min(newSnapSize, 10 * snapSize);
				
			} else {
				// the new snap size must be a divider of the original snap
				// snap decreases as the scale goes up and the figures become bigger
				newSnapSize = clampToDividerOf(Math.round(snapSize / (scaleFactor * (1 + snapShift * (scaleFactor - 1)))), snapSize);
				newSnapSize = Math.max(newSnapSize, 1);
			}
				
			// compute the value of the new snap reported to the current scale
			// this is how the user's eye percieve the current snap	
			var snapAtCurrentScale:Number = newSnapSize * scaleFactor;				
			
			// compute the new grid size as a multiple of snapAtCurrentScale					
			var newGridSize:Number = snapAtCurrentScale;
				
			if (newGridSize < 12) {
				while(newGridSize < 15) {
					newGridSize += snapAtCurrentScale;
				}
			} else if (newGridSize > 32) {	
				// this should rarely happen
				for (var i:int = 2; i < snapAtCurrentScale / 2 && newGridSize > MAX_GRID_SIZE; i++) {
					// divide with 2, 3 ... to display a smaller grid 
					if (int(snapAtCurrentScale / i) * i == snapAtCurrentScale) {
						newGridSize = snapAtCurrentScale / i;
						i = 1;
					}
				}
			}
			
			// update currentSnapSize
			_currentSnapSize = newSnapSize;
			
			// update the grid size
			grid.size = Math.round(newGridSize);
		}
				
		/**
		 * Interpret specific flower styles, otherwise call super.
		 * 
		 * @author Luiza
		 */ 	
		override public function setStyle(styleProp:String, newValue:*):void {			   
						
			if (styleProp == "flowerShowGrid") {
				if (grid != null) {
					grid.visible = Boolean(newValue);
					grid.includeInLayout = Boolean(newValue);
					sizeGrid();
					if (!newValue) {
						// reset to default when no grid shown
						_currentSnapSize = 1; 
					}
					// recompute _currentSnapSize
					applyScaleOnGrid(scaleFactor);
				}
			} else if (styleProp == "flowerSnapSize") {
				snapSize = int(newValue);
				applyScaleOnGrid(scaleFactor);
				
			} else {
				super.setStyle(styleProp, newValue);
			}
		}
		
		public function get grid():RectangularGrid {
			if (useGrid && _grid == null) {
				_grid = new RectangularGrid;
			}
			return _grid;
		}
		
		/**
		 * By default setting the mouseEnabled on a container doesn't set the same value on its
		 * contentPane, but we want that the contentPane.mouseEnabled have the same value as rootFigure.mouseEnabled.
		 * 
		 * Letting the default behaviour, we have a problem if the rootFigure has a child with a 
		 * gape inside (that lets the event pass throw it). If additionaly the root figure has 
		 * mouseEnabled setted to false its contentPane catches mouse events on the gap area of the childFigure.
		 * 
		 * We override this method because a rootFigure adds a contentPane only when it is needed and because 
		 * we don't have an event that triggeres when this happens.
		 */
//		override public function set creatingContentPane(value:Boolean):void {
//			super.creatingContentPane = value;
//			// if content pane creation ended set the value of the mouseEnabled property 
//			// of the content pane to be equal to the mouseEnabled property of the rootFigure
//			if (value == false) {
//				this.contentPane.mouseEnabled = this.mouseEnabled;
//			}
//				
//		}
	}
}