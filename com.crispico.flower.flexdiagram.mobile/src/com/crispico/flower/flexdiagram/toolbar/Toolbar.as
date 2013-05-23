package  com.crispico.flower.flexdiagram.toolbar {
	
	import com.crispico.flower.flexdiagram.DiagramViewer;
	import com.crispico.flower.flexdiagram.RootFigure;
	import com.crispico.flower.flexdiagram.action.AutoResizeContainer;
	import com.crispico.flower.flexdiagram.event.ToolEvent;
	import com.crispico.flower.flexdiagram.tool.Tool;
	import com.crispico.flower.flexdiagram.tool.zoom.ZoomComboBox;
	
	import flash.display.DisplayObject;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.events.TimerEvent;
	import flash.geom.Point;
	import flash.utils.Timer;
	
	import mx.collections.ArrayCollection;
	import mx.containers.Canvas;
	import mx.containers.Panel;
	import mx.containers.VBox;
	import mx.controls.listClasses.ListBase;
	import mx.controls.scrollClasses.ScrollBar;
	import mx.core.Application;
	import mx.core.Container;
	import mx.core.ScrollPolicy;
	import mx.core.UIComponent;
	import mx.events.DropdownEvent;
	import mx.events.EffectEvent;
	import mx.events.FlexMouseEvent;
	import mx.events.ListEvent;
	
	/**
	 * Toolbar component. This component also contains a zoom ComboBox.
	 * 
	 * <p>
	 * Updates the selected state of the buttons based on <code>ToolEvent</code>s dispatched
	 * by the viewer.
	 *  
	 * @author Cristi	 
	 * @flowerModelElementId _Yqif0MK5Ed6-yKDFMO4rZg
	 */
	public class Toolbar extends VBox {

		/**
		 * Link to the active viewer so that
		 * we can activate the selected tool in 
		 * the context of the new viewer.
		 */
		protected var viewer:DiagramViewer = null;

		/**
		 * The ZoomComboBox is treated specially
		 */
		protected var zoomCombo:ZoomComboBox = null;

		/**
		 * Height of the panel's header.
		 * 
		 * It is defined as a constant so that we can have access
		 * to it, because Panel.getHeaderHeight is protected, and we
		 * don't have access to it.
		 */
		protected const PANEL_HEADER_HEIGHT:Number = 20;	

		private var autoResizeContainers:ArrayCollection = new ArrayCollection();
		
		/**
		 * Set to true between the moment the mouseWheel listener is called 
		 * and <code>timerHandler() is called</code> if the mouse remains on the same panel.
		 */ 
		private var mouseWheelSamePanel:Boolean = false;	
		
		/**
		 * This timer is started when a mouseWheel event is catched 
		 * by an <code>AutoResizeContainer</code> corresponding to a child of the toolbar. 
		 * In this case no rollOver event is caught any longer for the toolbar children.
		 * Only after the dalay set has passed, and no other mouseWheel event was caught, 
		 * the rollOver event will be caught again.
		 */ 
		private var timer:Timer = new Timer(300, 1);
		
		/**
		 * Getter for the current viewer
		 */
		public function getViewer():DiagramViewer {
			return viewer;
		}
		
		/**
		 * Setter for the current viewer
		 * 
		 * Empties the toolbar.
		 * If toolProvider is not null, also fills the Toolbar
		 */	
		public function setViewer(viewer:DiagramViewer, toolProvider:IToolProvider = null):void {
			if (this.viewer != null) {
				// listeners for the tool lifecycle
				this.viewer.removeEventListener(ToolEvent.EXCLUSIVE_TOOL_ACTIVATED, exclusiveToolActivatedHandler);
				this.viewer.removeEventListener(ToolEvent.EXCLUSIVE_TOOL_DEACTIVATED, exclusiveToolDeactivatedHandler);
				this.viewer.removeEventListener(ToolEvent.EXCLUSIVE_TOOL_LOCKED, exclusiveToolLockedHandler);				
				
				zoomCombo.rootFigure = null;
			}

			// empty the toolbar
			removeAll();

			this.viewer = viewer;
			
			if (this.viewer != null) {
				// listeners for the tool lifecycle
				this.viewer.addEventListener(ToolEvent.EXCLUSIVE_TOOL_ACTIVATED, exclusiveToolActivatedHandler);
				this.viewer.addEventListener(ToolEvent.EXCLUSIVE_TOOL_DEACTIVATED, exclusiveToolDeactivatedHandler);
				this.viewer.addEventListener(ToolEvent.EXCLUSIVE_TOOL_LOCKED, exclusiveToolLockedHandler);				
	
				// update zoomCombobox
				zoomCombo.rootFigure = RootFigure(viewer.getRootFigure());
				
				// fills the toolbar
				if (toolProvider != null) {
					toolProvider.fillToolbar(this);
				}
			}			
		}
		
		private function findButtonCorrespondingToTool(tool:Tool):ToolbarButtonWithTool {
			// search for the button in the panels
			for each (var currentPanel:Object in getChildren()) {
				if (currentPanel is Panel) {
					// cycle through all it's children
					for each (var currentButton:Object in Panel(currentPanel).getChildren()) {
						if (currentButton is ToolbarButtonWithTool) {
							if (ToolbarButtonWithTool(currentButton).tool == tool) {
								return ToolbarButtonWithTool(currentButton);
							}
						}
					}
				}
			}
			
			// search for the button in the floating windows;
			// CS: this could be optimized, in order to avoid scanning all the AutoResizeContainer; but the logic
			// that does the switch (to floating mode and back) is so twisted, that I am affraid to touch it
			for each (var arc:AutoResizeContainer in autoResizeContainers) {
				if (arc.numChildren > 0 && arc.getChildAt(0) is Panel) {
					for each (currentButton in Panel(arc.getChildAt(0)).getChildren()) {
						if (currentButton is ToolbarButtonWithTool) {
							if (ToolbarButtonWithTool(currentButton).tool == tool) {
								return ToolbarButtonWithTool(currentButton);
							}
						}
					}
					
				}
			}
			return null;
		}
		
		/**
		 * Finds the button that corresponds to the tool, and selects it.
		 */ 
		private function exclusiveToolActivatedHandler(event:ToolEvent):void {
			var button:ToolbarButtonWithTool = findButtonCorrespondingToTool(event.tool);
			if (button != null) {
				button.selected = true;
			}
		}
		
		/**
		 * Finds the button that corresponds to the tool, and deselects it.
		 */ 
		private function exclusiveToolDeactivatedHandler(event:ToolEvent):void {
			var button:ToolbarButtonWithTool = findButtonCorrespondingToTool(event.tool);
			if (button != null) {
				button.selected = false;
				button.setLocked(false);
			}
		}

		/**
		 * Finds the button that corresponds to the tool, and locks it.
		 */ 
		private function exclusiveToolLockedHandler(event:ToolEvent):void {
			var button:ToolbarButtonWithTool = findButtonCorrespondingToTool(event.tool);
			if (button != null) {
				button.setLocked(true);
			}
		}

		private function buttonClickHandler(event:Event):void {
			if (event.currentTarget is ToolbarButtonWithTool) {
				var toolbarButtonWithTool:ToolbarButtonWithTool = ToolbarButtonWithTool(event.currentTarget);
				// we force an activation because the viewer might not be active, and we
				// want the user to see the visual feedback (i.e. the clicked button changing color)
				viewer.activate(true);
				viewer.activateTool(toolbarButtonWithTool.tool);
			} else if (event.currentTarget is StandAloneToolbarButton) {
				StandAloneToolbarButton(event.currentTarget).executeAction();
			}
		}
		
		/**
		 * Constructor.
		 * 
		 * Also sets defaut style and creates the ZoomComboBox.
		 */
		public function Toolbar() {
			super();
			setStyle("borderStyle", "outset");
			setStyle("verticalGap", 4);
			// add ZoomComboBox.			
			var zoomPanel:Panel = createPanel("Zoom");
			
			zoomCombo = new ZoomComboBox();
			zoomCombo.percentWidth = 100;
			zoomPanel.addChild(zoomCombo);
			zoomPanel.horizontalScrollPolicy = ScrollPolicy.OFF;
			zoomCombo.addEventListener(DropdownEvent.OPEN, dropdownHandler);
			horizontalScrollPolicy = ScrollPolicy.OFF;
			timer.addEventListener(TimerEvent.TIMER, timerHandler);	
			addEventListener(MouseEvent.MOUSE_WHEEL, function(event:MouseEvent):void {
				if (mouseWheelSamePanel) {
					for (var i:int = 0; i < numChildren; i++) {
						if (getChildAt(i) is Canvas) {
							var arc:AutoResizeContainer = AutoResizeContainer(autoResizeContainers.getItemAt(i));
							if (arc.parent.mouseX < arc.x || arc.parent.mouseX > (arc.x + arc.width) ||
								arc.parent.mouseY < (arc.y + event.delta * 3) || arc.parent.mouseY > (arc.y + arc.height + event.delta * 3)) {
									arc.removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
									arc.removeEventListener(EffectEvent.EFFECT_END, effectEndHandler);
									arc.removeEventListener(MouseEvent.ROLL_OUT, arc.rollOutHandler);
									removeChildAt(i);
									addChildAt(arc.getChildAt(0), i);
									arc.show(false);
									arc.addEventListener(MouseEvent.ROLL_OVER, arc.rollOverHandler);
								}
						}
					}
				}
				mouseWheelSamePanel = false;
				if (verticalScrollBar && 
					!((verticalScrollBar.scrollPosition == 0 && event.delta > 0) || 
					(verticalScrollBar.scrollPosition == maxVerticalScrollPosition && event.delta < 0))) { 
					timer.reset();
					timer.start();
				}					
			}, false, int.MAX_VALUE);	
		}
		
		/**
		 * Creates and adds to the Toolbar a new ToolbarButtonWithTool embedding the given tool.
		 * The button will be added to the specified group. If the group does not exist it will be created.
		 * 
		 * @param activated Boolean if it is true, activate the tool when added to toolbar
		 * 
		 * @flowerModelElementId _SPFNyMV2Ed6e7_D-POUIfw
		 */
		public function addTool(tool:Tool, group:String):void {		
			// add a new ToolbarButton to represent the tool
			var toolButton:ToolbarButtonWithTool = new ToolbarButtonWithTool();
			toolButton.tool = tool;
			addToolbarButton(toolButton, group);
		}
		
		/**
		 * Adds a ToolbarButton to the toolbar. Users should use this function to add StandAloneToolbarButtons.
		 * <p>
		 * If the requested group does not exist, a new one with the given name is created.
		 * 
		 * @author Luiza
		 * @flowerModelElementId _yhCxkDUSEeCTrKdImkKvZg
		 */ 
		public function addToolbarButton(toolbarButton:ToolbarButton, group:String):void {
			// search to all it's children to find a panel
			if (group == null || group.length == 0) {
				throw new Error("Name of the group must be provided");
			}
			
			var groupPanel:Panel = null;
			
			for each (var pan:Panel in getChildren()) {
				if (pan.title == group) {
					groupPanel = pan;
					break;
				}
			}
			
			// couldn't find a panel named like we want
			// create a new one
			if (groupPanel == null) {
				groupPanel = createPanel(group);
			}
			
			toolbarButton.minWidth = 10;
			toolbarButton.percentWidth = 100;
			toolbarButton.addEventListener(MouseEvent.CLICK, buttonClickHandler);
			
			groupPanel.addChild(toolbarButton);
		}	
		
		public override function set verticalScrollBar(value:ScrollBar):void {
			// if a vertical scroll bar is shown, resize the toolbar by adding the scrollbar width
			if (value != null && value != verticalScrollBar)
				width += ScrollBar.THICKNESS;
			else if (value == null && verticalScrollBar != null)
				width -= ScrollBar.THICKNESS; //resize back if the scrollBar is no longer needed	
			// for all child Canvas get the AutoResizecontainer correspoding and move ScrollBar.Thickness to the right or left
			// change the position only if it is the las child of its parent
			if (parent.getChildIndex(this) == parent.numChildren - 1) { 
				for (var i:int = 0; i < numChildren; i++) {
					if (getChildAt(i) is Canvas) {
						var arc:AutoResizeContainer = AutoResizeContainer(autoResizeContainers.getItemAt(i));					
						if (value != null && value != verticalScrollBar)
							arc.x -= ScrollBar.THICKNESS;						
						else if (value == null && verticalScrollBar != null)
							arc.x += ScrollBar.THICKNESS;
					}
				}				
			}
			super.verticalScrollBar = value;
				
			if (value != null) {
				value.addEventListener(MouseEvent.MOUSE_DOWN , function(event:MouseEvent):void {
					for (var i:int = 0; i < numChildren; i++) {
						if (getChildAt(i) is Canvas) {
							var arc:AutoResizeContainer = AutoResizeContainer(autoResizeContainers.getItemAt(i));							
							arc.removeEventListener(EffectEvent.EFFECT_END, effectEndHandler);
							arc.removeEventListener(MouseEvent.ROLL_OUT, arc.rollOutHandler);
							arc.show(false);
							removeChildAt(i);
							var child:DisplayObject = arc.getChildAt(0);
							arc.removeChildAt(0);
							addChildAt(child, i);
							arc.addEventListener(MouseEvent.ROLL_OUT, arc.rollOutHandler);
							getChildAt(i).addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);	
						}
					}		
				});
			}
		}
		
		/**
		 * Utility function that removes a ToolbarButton from the toolbar.
		 * If the group Panel containing the button becomes empty, also removes the Panel.
		 * @flowerModelElementId _YAIGkcVOEd6OufWQWnlzRw
		 */
		protected function removeButton(button:ToolbarButton):void {
			var panParent:Panel = Panel(button.parent);
			
			// remove event listener
			button.removeEventListener(MouseEvent.CLICK, buttonClickHandler);
			
			Panel(panParent).removeChild(button);
			
			if (button is ToolbarButtonWithTool) {
				ToolbarButtonWithTool(button).tool = null;
			}
			
			// if no children left in parent Panel then remove it also
			if (panParent.getChildren().length == 0) {
				// remove event listeners
				panParent.removeEventListener(MouseEvent.CLICK, panelClicked);
				panParent.removeEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
				panParent.removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
				autoResizeContainers.removeItemAt(getChildIndex(panParent));			
				// and remove the panel
				removeChild(panParent);
			}
						
		}
		
		/**
		 * Utility function that creates a panel and adds it to the toolbar.
		 */
		protected function createPanel(title:String):Panel {
			var panel:Panel = new Panel();
			panel.title = title;
			panel.percentWidth = 100;
			panel.minWidth = 7;
			
			panel.setStyle("headerHeight", PANEL_HEADER_HEIGHT);
			panel.setStyle("borderThicknessLeft", 3); 
			panel.setStyle("borderThicknessTop", 0); 
			panel.setStyle("borderThicknessRight", 3); 
			panel.setStyle("borderThicknessBottom", 3); 
			panel.setStyle("verticalGap", 0); 
			panel.setStyle("cornerRadius", 0); 
			
			panel.addEventListener(MouseEvent.CLICK, panelClicked);
			panel.addEventListener(MouseEvent.ROLL_OVER, rollOverHandler, false, 1);
			
			addPanel(panel);
			var arc:AutoResizeContainer = new AutoResizeContainer(Container(Application.application), false, false);
			autoResizeContainers.addItem(arc);						
			return panel;
		}
		
		/**
		 * This method sorts the categories from the toolbar
		 */
		protected function addPanel(panelToAdd:Panel):void {
			// temporary fix for the sort order of the toolbar categories.
			// other diagrams have some other groupds: Use Case Relations, Use Case Elements etc
			var order:Array = ["Zoom", "Common", "Relations", "AS Elements", "Java Elements", "Elements"];			
			var panels:Array = getChildren();
			var index:int = panels.length;
			for (var i:int = 0; i < panels.length; i++) {
				var p:Panel = panels[i];			
				if (order.indexOf(panelToAdd.title) < order.indexOf(p.title) && order.indexOf(panelToAdd.title) != -1) {
					index = i;
					break;
				}
			}
			addChildAt(panelToAdd, index);
		}
		
		/**
		 * When the mouse clicks on the panel this function is called.
		 * 
		 * It checks to see if the mouse is clicked on the 
		 * head of the panel and
		 * <ul>
		 * 		<li>If the panel is collapsed it expands it</li>
		 *  	<li>If the panel is expanded it collapses it</li>
		 * </ul>
		 */
		protected function panelClicked(event:MouseEvent):void {
			// get the panel the click was on
			var disp:DisplayObject = DisplayObject(event.target);
			var panelFound:Boolean = false;

			// Although the MouseClick handler is instaled on a panel
			// the event.target received is the Panel's child the click 
			// happened (UITextField / UIComponent), so that's why
			// the following while is needed
			// 
			while (disp != null) {
				if (disp is Panel) {
					panelFound = true;
					// found, exit cycle
					break;
				}
				disp = disp.parent;
			}
			if (panelFound) {
				// check to see if the click is on the header
				
				// transform coordinates into content
				var point:Point = disp.globalToLocal(new Point(event.stageX, event.stageY));
				var visibleButton:Boolean = false;
				// if the click is on the header
				if  (point.x < disp.width && point.y < PANEL_HEADER_HEIGHT) {
					// the mouse is on the panel, then the temporary canvas should change it's size also;
					var tempCanvas:Canvas = Canvas(getChildAt(autoResizeContainers.getItemIndex(disp.parent)));
					tempCanvas.height = PANEL_HEADER_HEIGHT + 3;
					//AutoResizeContainer(disp.parent).measuredWidth = 0;
		 			// 3 is the bottom border
					if (disp.height <= PANEL_HEADER_HEIGHT + 3) {
						// it is colapsed -> expand it
						visibleButton = true;
					} else { 
						// it is expanded -> collapse it
						visibleButton = false;
						
					}
					for each (var obj:Object in Panel(disp).getChildren()) {
						if (obj is UIComponent) {
							UIComponent(obj).visible = visibleButton;
							UIComponent(obj).includeInLayout = visibleButton;
							if (visibleButton) {
								tempCanvas.height += UIComponent(obj).height;								
							}
								
						}
					}
					// update the autoResizeContainer position, as it might have changed due to changing in the scroll bar position.
					if (verticalScrollBar && verticalScrollPosition + (disp.height - tempCanvas.height)  > verticalScrollBar.maxScrollPosition) {
						if (!visibleButton) {
							var delta:Number = verticalScrollBar.maxScrollPosition - (disp.height - tempCanvas.height) - verticalScrollPosition;;
							if (verticalScrollPosition + delta < 0) { // no more vertical scrollBar -> change also the x coordinate
								delta = -verticalScrollPosition;
							}
							disp.parent.y -= delta;
						}
					}
					
					// tell the panel that we have modified it's size
					disp.height = tempCanvas.height;
					disp.parent.height = disp.height;
					Panel(disp).invalidateSize();
					AutoResizeContainer(disp.parent).invalidateSize();
				}
			}
		}
				
		/**
		 * Utility function that can remove one tool,
		 * or all the tools.
		 * 
		 * @return Boolean
		 * 	true if the tool requested has been found and deleted, or
		 * all tools have been deleted.
		 * 	false if the tool requested could not be found
		 */
		protected function removeAll():Boolean {
			// cycle through all the cildren of this toolbar
			for each (var currentPanel:Object in getChildren()) {
				if (currentPanel is Panel) {
					// cycle through all it's children
					for each (var currentButton:Object in Panel(currentPanel).getChildren()) {
						if (currentButton is ToolbarButton) {
							removeButton(ToolbarButton(currentButton));
						} 
					}
				}
			}
			
			return true;
		}	
		
		/**
		 * When the user rolls the mouse over a panel (child of the toolbar), this method is called.
		 * It creates a blank copy (keeps the same dimensions) of the child, adding it to the <code>Toolbar</code>,
		 * while adding the original child to an <code>AutoResizeContainer</code>.
		 * 
		 * It is called also on a fake rollOver simulation event 
		 * (when there is a redraw of elements after a mouseWheel event).
		 * 
		 * @param event It can be null, if the call is on rollOver simulation
		 * @param target It should be not null if the rollOver is simulated
		 * @author Ioana 
		 */ 
		private function rollOverHandler(event:MouseEvent, target:UIComponent = null):void {			
			if (event != null && (event.buttonDown || timer.running)) {			
				return;
			}
			if (event)
				var component:UIComponent = UIComponent(event.currentTarget);
			else 
				component = target;		
			var arc:AutoResizeContainer = null;
			// originalWidth = the original witdh of the component
			var originalWidth:Number = width - borderMetrics.left - borderMetrics.right;
			if (verticalScrollBar)
				originalWidth -= ScrollBar.THICKNESS;
			// the child can be either ARC or subPanel of toolbar
			if (component is AutoResizeContainer) { 
				// the resize was not ended, the original X and width are not the correct ones, recalculate
			 	arc = AutoResizeContainer(component);
			 	var tempCanvas:DisplayObject = DisplayObject(getChildAt(autoResizeContainers.getItemIndex(arc)));
			 	var arcPosition:Point = this.contentToGlobal(new Point(tempCanvas.x, tempCanvas.y));
				arc.x = arcPosition.x;
				arc.width = originalWidth;
				arc.y = arcPosition.y;							
			} else if (component.parent == this) {				
				component.removeEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
				var childIndex:int = getChildIndex(component);
				arc = AutoResizeContainer(autoResizeContainers.getItemAt(childIndex));
				arcPosition = this.contentToGlobal(new Point(component.x, component.y));
				arc.x = arcPosition.x;
				arc.y = arcPosition.y;
				arc.width = originalWidth;
				arc.height = component.height;
				tempCanvas = new Canvas();
				tempCanvas.x = component.x;
				tempCanvas.y = component.y;
				removeChildAt(childIndex);
				addChildAt(tempCanvas, childIndex);
				arc.addChild(component);
				tempCanvas.width = originalWidth;
				tempCanvas.height = component.height;
				arc.measuredWidth = Math.max(component.measuredWidth, originalWidth);
				arc.show(true, false);
				arc.dispatchEvent(new MouseEvent(MouseEvent.ROLL_OVER));
				arc.addEventListener(EffectEvent.EFFECT_END, effectEndHandler);
			}
			if (arc) {
				arc.addEventListener(MouseEvent.MOUSE_WHEEL, mouseWheelHandler, false, 1);
				arc.addEventListener(MouseEvent.ROLL_OUT, rollOutHandler, false, -1);
			}			
		}
		
		/**
		 * When the user rolls the mouse out the currently open <code>AutoResizeContainer</code>, 
		 * this method is called.
		 * Besides the normal behaviour of a <code>AutoResizeContainer</code>, this methods also sets new listeners.
		 * 
		 * @author Ioana 
		 */ 
		private function rollOutHandler(event:MouseEvent):void {
			var arc:AutoResizeContainer = AutoResizeContainer(event.currentTarget);
			arc.removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
			arc.addEventListener(MouseEvent.ROLL_OVER, rollOverHandler, false, 1);			
			arc.removeEventListener(MouseEvent.MOUSE_WHEEL, mouseWheelHandler);
			// necessary to make the effect_end changes when roll out event is dispatched 
			arc.dispatchEvent(new EffectEvent(EffectEvent.EFFECT_END));
		}
		
		/**
		 * When a resize "to small" effect of the <code>AutoResizeContainer</code> is ended, this method is called.
		 * It removes the <code>AutoResizeContainer</code>, 
		 * and replace the empty child of the toolbar with it's original child.
		 * 
		 * If it is a resize "toBig", it reset the y coordinate of the <code>AutoResizeContainer<code>
		 * (possible to be changed after a mouseWheel event) 
		 * @author Ioana
		 */ 
		private function effectEndHandler(event:EffectEvent):void {
			// event.target is AutoResizeContainer
			var arc:AutoResizeContainer = AutoResizeContainer(event.target); 
			if (arc.isMinimized()) {
				var childIndex:int = autoResizeContainers.getItemIndex(arc);				
				arc.getChildAt(0).addEventListener(MouseEvent.ROLL_OVER, rollOverHandler, false, 1);
				arc.removeEventListener(EffectEvent.EFFECT_END, effectEndHandler);
				arc.removeEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
				removeChildAt(childIndex);
				var child:DisplayObject = arc.getChildAt(0);
				arc.removeChild(child);
				addChildAt(child, childIndex);
				arc.show(false);
			} else {
				//recompute the y coordinate for arc
				arc = AutoResizeContainer(event.target);
			 	var tempCanvas:DisplayObject = DisplayObject(getChildAt(autoResizeContainers.getItemIndex(arc)));
			 	var arcPosition:Point = this.contentToGlobal(new Point(tempCanvas.x, tempCanvas.y));
				arc.y = arcPosition.y;								
			}
				
		}
		
		/**
		 * When the dropdown of the <code>zoomCombo</code> change it's state (from open to close and the reverse),
		 * this method is called. 
		 * It removes (for open) and adds(for close) the <code>MouseEvent.ROLL_OUT</code> listeners 
		 * of the <code>AutoResizeContainer</code> correspodnign to the combobox, 
		 * as the combo needs to be resized "to big" when the mouse is over the dropdown.
		 * 
		 * @author Ioana
		 */ 
		private function dropdownHandler(event:Event):void {
			var arc:AutoResizeContainer = null;
			if (event.currentTarget == zoomCombo && zoomCombo.parent.parent is AutoResizeContainer)
				arc = AutoResizeContainer(UIComponent(event.currentTarget).parent.parent);
			else if (event.currentTarget is ListBase && ListBase(event.currentTarget).owner.parent.parent is AutoResizeContainer)
				arc = AutoResizeContainer(ListBase(event.target).owner.parent.parent);
			else 
				return;
			if (event.type == DropdownEvent.OPEN) {
				arc.removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
				arc.removeEventListener(MouseEvent.ROLL_OUT, arc.rollOutHandler);
				zoomCombo.dropdown.addEventListener(FlexMouseEvent.MOUSE_DOWN_OUTSIDE, dropdownHandler);
				// change of the value in the zoom combo box (with dropdown on close)
				zoomCombo.addEventListener(ListEvent.CHANGE, dropdownHandler);
				// ESCAPE or ENTER when dropdown open, but possible without a change in value - closes dropdown and roll out the autoResizeContainer
				zoomCombo.addEventListener(KeyboardEvent.KEY_UP, dropdownHandler);
			} else if (event.type == FlexMouseEvent.MOUSE_DOWN_OUTSIDE || event.type == ListEvent.CHANGE || event.type == KeyboardEvent.KEY_UP) {
				if ((zoomCombo.dropdown && !zoomCombo.dropdown.enabled) || !zoomCombo.dropdown) {
					if (zoomCombo.dropdown && event.type == FlexMouseEvent.MOUSE_DOWN_OUTSIDE)
						zoomCombo.dropdown.removeEventListener(FlexMouseEvent.MOUSE_DOWN_OUTSIDE, dropdownHandler);
					if (event.type == ListEvent.CHANGE || event.type == KeyboardEvent.KEY_UP) {
						zoomCombo.removeEventListener(ListEvent.CHANGE, dropdownHandler);
						zoomCombo.removeEventListener(KeyboardEvent.KEY_UP, dropdownHandler);
					}
					arc.addEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
					arc.addEventListener(MouseEvent.ROLL_OUT, arc.rollOutHandler);		
					// the related object must not be either be the text field, the arrow button, or the zoomCombo itself		
					if ((event is FlexMouseEvent && FlexMouseEvent(event).relatedObject != zoomCombo && 
						FlexMouseEvent(event).relatedObject.parent.parent != zoomCombo && 
						FlexMouseEvent(event).relatedObject.parent != zoomCombo) || 
						event is ListEvent || event is KeyboardEvent)	
						arc.dispatchEvent(new MouseEvent(MouseEvent.ROLL_OUT));
				}
			} 
		}

		/**
		 * Handler for the scroll wheel of the mouse.
		 * Scroll the toolbar and the autoResizeContainer under the mouse.
		 * It activates a timer so the rollOver on the Panels is not listened during this timer.
		 * 
		 * Needs to compute the difference the vertical scroll bar will be moved by (similar computing as <code>Container.mouseWheelHandler()</code>), 
		 * for changing the y coordinate of the autoResizeContainer that dispatched this event.
		 * 
		 * @author Ioana
		 */		
		private function mouseWheelHandler(event:MouseEvent):void {
			if (event.currentTarget is AutoResizeContainer) {
				if (!verticalScrollBar || AutoResizeContainer(event.currentTarget).isResizeEffectPlaying())
					return;	
				var scrollDirection:int = event.delta <= 0 ? 1 : -1;
	
	            var lineScrollSize:int = verticalScrollBar ?
	                                     verticalScrollBar.lineScrollSize :
	                                     1;
	
	            // Make sure we scroll by at least one line
	            var scrollAmount:Number =
	                Math.max(Math.abs(event.delta), lineScrollSize);
	
	            // Multiply by 3 to make scrolling a little faster
	            var oldPosition:Number = verticalScrollBar.scrollPosition;
	            var newScrollPos:Number = oldPosition + 3 * scrollAmount * scrollDirection;
	            if (newScrollPos > maxVerticalScrollPosition)
	            	newScrollPos = maxVerticalScrollPosition;
	            if (newScrollPos < 0 )
	            	newScrollPos = 0;
	            var diff:Number = newScrollPos - oldPosition;
				if (diff != 0) {
					timer.reset();
					timer.start();
					dispatchEvent(event);
					for (var i:int = 0; i < numChildren; i++) {
						if (getChildAt(i) is Canvas) {
							mouseWheelSamePanel = false;
							var arc:AutoResizeContainer = AutoResizeContainer(autoResizeContainers.getItemAt(i));
							// if the y of the autoResizeContainer is changed certain listener can be activated
							// and this is not wanted - the activation of the listeners should be done only if the panel does not change on mouseWheel event  
							arc.removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
							arc.removeEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
							arc.removeEventListener(EffectEvent.EFFECT_END, effectEndHandler);
							arc.removeEventListener(MouseEvent.ROLL_OUT, arc.rollOutHandler);
							arc.y -= diff;
							
							// if after a mouse wheel, we are on the same group, just do the scroll
							if (arc.parent.mouseX >= arc.x && arc.parent.mouseX <= (arc.x + arc.width) &&
								arc.parent.mouseY >= arc.y  && arc.parent.mouseY <= arc.y + arc.height) {
								arc.addEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
								arc.addEventListener(MouseEvent.ROLL_OUT, arc.rollOutHandler);
								arc.addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
								arc.addEventListener(EffectEvent.EFFECT_END, effectEndHandler);
								mouseWheelSamePanel = true;	
								return;									
							}
							// otherwise, close the autoResizeContainers open, 
							// and remove the roll_over listeners for the panels while timer is running	
							var childIndex:int = autoResizeContainers.getItemIndex(arc);
							removeChildAt(childIndex);
							var child:DisplayObject = arc.getChildAt(0);
							arc.removeChildAt(0);
							addChildAt(child, childIndex);
							arc.show(false);
							arc.addEventListener(MouseEvent.ROLL_OVER, arc.rollOverHandler);
						} 
					}	
					dispatchEvent(event);	
					// remove the ROLL_OVER listener for panels in Toolbar
					for each (child in getChildren())	
						if (child is Panel) {							
							child.removeEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
						}		
				}
		   }
		}
		
		/**
		 * When the timer ends its delay, this method is called. 
		 * It reactivates certain listeners for the panels (children of Toolbar).
		 * It simulates a fake rollOver for the panel under the mouse.
		 * 
		 * @author Ioana
		 */ 
		private function timerHandler(event:TimerEvent):void {
			if (timer.currentCount == 0 || mouseWheelSamePanel)
				return;			
			timer.reset()	
			mouseWheelSamePanel = true;
			for each (var child:DisplayObject in getChildren()) { //add again the ROLL_OVER listener for panels in Toolbar
				child.addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);			
			}
			// do rollOver for panel under the mouse
			var arr:Array = stage.getObjectsUnderPoint(new Point(stage.mouseX, stage.mouseY));
			for (var i:int = arr.length - 1; i >= 0; i--) {
				if (arr[i] is Panel || arr[i].parent is Panel) {
					var panel:Panel = arr[i] is Panel ? arr[i] : arr[i].parent;
					callLater(rollOverHandler, [null, panel]);					
				} 
			}
		}
	}
}