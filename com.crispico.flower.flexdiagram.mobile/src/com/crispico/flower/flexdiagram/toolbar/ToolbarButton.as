package com.crispico.flower.flexdiagram.toolbar {
	import com.crispico.flower.flexdiagram.util.common.BitmapContainer;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Label;
	import mx.core.*;

	/**
	 * Component similar to a toggle button used by <code>Toolbar</code>.
	 * <p>
	 * Consists of:
	 * <ul>
	 *  <li>an icon - usually the Tool's icon
	 *  <li>a label - usually the Tool's title
	 *  <li>a special lock icon controlled by <code>setLocked(Boolean)</code> function
	 * </ul>
	 *  
	 * @author Luiza
	 * 
	 * @flowerModelElementId _YARQcMVOEd6OufWQWnlzRw
	 */
	public class ToolbarButton extends HBox {
	    
		[Embed(source="/icons/lock.png")]
		private static const Lock:Class;
		
		private static const DEFAULT_SELECT_COLOR:uint = 0xB2E1FF;
		
		private static const DEFAULT_HIGHLIGHT_COLOR:uint = 0xDFF2FF;
		
		private static const LOCKED_TOOL_COLOR:uint = 0xFFF955;
				
		protected var toolIcon:BitmapContainer;
		
		protected var lockIcon:BitmapContainer;
		
		protected var titleLabel:Label;
				
		/**
		 * Indicates this button was clicked and still active.
		 */ 
		private var _selected:Boolean = false;
		
		/**
		 * Indicates the mouse is over. The _selected state has priority meaning that a selected ToolbarButton
		 * won't change color in highlightColor when mouse is over.
		 */ 
		private var _highlighted:Boolean = false;
		
		private var _selectColor:uint = DEFAULT_SELECT_COLOR;
		
		private var _highlightColor:uint = DEFAULT_HIGHLIGHT_COLOR;
		
		public function ToolbarButton() {
			super();
			
			// special styles so this component is similar to a LinkButton
			setStyle("horizontalAlign", "left");
			setStyle("verticalAlign", "middle");
			setStyle("horizontalGap", 2);
			setStyle("paddingBottom", 2);
			setStyle("paddingTop", 2);
			setStyle("paddingLeft", 4);
			setStyle("paddingRight", 4);
			setStyle("fontWeight", "bold");
			horizontalScrollPolicy = ScrollPolicy.OFF;
			verticalScrollPolicy = ScrollPolicy.OFF;
			
			// use a BitmapContainer(or an Image) beacuse this component is a Container and can't load directly a Bitmap
			// its children must be IUIComponent objects.
			toolIcon = new BitmapContainer();
			titleLabel = new Label();
			
			// enable hand cursor over the ToolbarButton area
			this.buttonMode = true; 
			titleLabel.useHandCursor = true;
			titleLabel.buttonMode = true;
			titleLabel.mouseChildren = false;	
			addChild(toolIcon);
			addChild(titleLabel);
						
			addEventListener(Event.ADDED_TO_STAGE, addedToStageHandler);
		}
				
		/**
		 * Background color for the ToolbarButton when the user selects it.
		 * 
		 * Default 0xB2E1FF
		 */ 
		public function get selectColor():uint {
			return _selectColor;
		}
		
		public function set selectColor(color:uint):void {
			if (color != _selectColor) {
				_selectColor = color;
				invalidateDisplayList();
			}
		}
		
		/**
	     * BackgroundColor of the ToolbarButton when the mouse is over
	     * 
	     * Default 0xBDECFF
	     */ 
		public function get highlightColor():uint {
			return _highlightColor;
		}
		
		public function set highlightColor(color:uint):void {
			if (color != _highlightColor) {
				_highlightColor = color;
				invalidateDisplayList();
			}
		}
		
		public function get selected():Boolean {
			return _selected;
		}
		
		public function set selected(isSelected:Boolean):void {
			if (_selected != isSelected) {
				_selected = isSelected;
				invalidateDisplayList();
			}
		}
				
		public function setIcon(iconClass:Class):void {
			toolIcon.bitmapData = (new iconClass()).bitmapData;
		}
		
		public function setLabel(newTitle:String):void {
			titleLabel.text = newTitle;
		}
		
		/**
		 * Sets this ToolbarButton to locked or unlocked state. This will cause the lock icon to appear or disappear properly.
		 */ 
		public function setLocked(locked:Boolean):void {
			if (locked && lockIcon == null) {
				lockIcon = new BitmapContainer();
				lockIcon.bitmapData = (new Lock()).bitmapData;
				addChild(lockIcon);	
			}
			
			if (lockIcon) {			
				lockIcon.visible = locked;
			}
			if (locked) {
				selectColor = LOCKED_TOOL_COLOR;				
			} else {
				selectColor = DEFAULT_SELECT_COLOR;
			}
		}
		
		/**
		 * Draw particular features like the button's background when selected or highlighted.
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			           	 	
            graphics.clear();
            
			if (_selected || _highlighted) {
				graphics.beginFill(_selected ? selectColor : highlightColor);
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
				graphics.endFill();
			}		
		}
		
		/**
		 * Add listeners. This is good practice to make sure listeners are activated only when necessary.
		 */ 
		private function addedToStageHandler(event:Event):void {
			addEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
			addEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
			addEventListener(MouseEvent.ROLL_OUT, rollOutHandler); 
		}
		
		/**
		 * Remove listeners. this is good practice to make sure listenera are deactivate when no longer necessary.
		 */ 
		private function removedFromStageHandler(event:Event):void {
			removeEventListener(Event.REMOVED_FROM_STAGE, removedFromStageHandler);
			removeEventListener(MouseEvent.ROLL_OVER, rollOverHandler);
			removeEventListener(MouseEvent.ROLL_OUT, rollOutHandler);
		}
		
		/**
		 * Change to highlight features.
		 */ 
		private function rollOverHandler(event:MouseEvent):void {
			_highlighted = true;
			invalidateDisplayList();
		}
		
		/**
		 * Remove highlight features.
		 */  
		private function rollOutHandler(event:MouseEvent):void {
			_highlighted = false;
			invalidateDisplayList();
		}
	}
}