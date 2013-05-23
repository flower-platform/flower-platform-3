package com.crispico.flower.flexdiagram.tool.zoom {
	import com.crispico.flower.flexdiagram.RootFigure;
	
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import mx.collections.ArrayCollection;
	import mx.controls.ComboBox;
	import mx.core.mx_internal;
	import mx.events.FlexEvent;
	import mx.events.PropertyChangeEvent;
	
	/**
	 * Visual component used to select a default value or to type a new value 
	 * for the zoom. These values are used by PermanentZoomTool. There is one 
	 * instance of this class per viewer.
	 * 
	 * @author Georgi
	 * @flowerModelElementId _IQtNAMVeEd6x1dpkaVcaXg
	 */
	public class ZoomComboBox extends ComboBox {
		
		/**
		 * The associated rootFigure.
		 */
		private var _rootFigure:RootFigure = null;
		
		public function ZoomComboBox() {
			super();
			editable = true;
			addEventListener(FlexEvent.CREATION_COMPLETE, creationCompleteHandler);
		}
		
		/**
		 * When the active <code>RootFigure</code> changes, the combobox
		 * should remove the old listener (if any) and to update its text.
		 */
		public function set rootFigure(rFigure:RootFigure):void {
			if (_rootFigure != null)
				_rootFigure.removeEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateComboBox);
			_rootFigure = rFigure;
			if (rFigure != null) {
				_rootFigure.addEventListener(PropertyChangeEvent.PROPERTY_CHANGE, updateComboBox);
				// update the current text			
				text = Math.round(_rootFigure.getScaleFactor()*100).toFixed(0).toString()+"%";
			}
		}
		
		/**
		 * Updates the combo box selected value if the scale factor 
		 * has changed.
		 * If the value is a round number, it shows it without any digits.
		 */
		private function updateComboBox(event:PropertyChangeEvent):void {
			if (event.property == "scaleFactor") {
				var newValue:Number = getPercentNumberFromString(String(event.newValue));
				if (newValue.toFixed(0) != String(newValue))
					text = newValue.toFixed(2);
				else
					text = newValue.toFixed(0);
					text = text + "%";	
				invalidateDisplayList();
			}
		}
		
		/**
		 * Convert a string into a Number by parsing it.
		 * It should contain only numbers and '.' character.
		 * 
		 * <p>
		 * The function reads the text and returns the first n
		 * characters that fit the format. The rest of them are
		 * ignored.
		 * 
		 * <p>
		 * If the there is no Number obtain (from parsing)
		 * the function returns the number 100. (no zoom is needed
		 * and the current width/height is kept)
		 */
		private function getPercentNumberFromString(value:String):Number {
			var aux:String = ""; 
			for (var i:int = 0; i < value.length; i++) {
				if (( value.charCodeAt(i) >= 48 && value.charCodeAt(i) <= 57 ) 
					|| ( value.charCodeAt(i) == 46 && aux.indexOf('.') < 0 ) ){
					aux += value.charAt(i);
				}
				else 
					break;
			}
			if (aux == "")
				return new Number(100);
			return new Number(aux);
		}
		
		/**
		 * Listener for Keyboard.KEY_DOWN. The function used to take the text 
		 * from the text input (when it is typed by the user), correct it and 
		 * call the zoom function. If the typed text doesn't contain '%' character, 
		 * it is added to the end.
		 * @flowerModelElementId _IQ2-D8VeEd6x1dpkaVcaXg
		 */
		override protected function keyDownHandler(event:KeyboardEvent):void {
			super.keyDownHandler(event);
             // If the editable field currently has focus, we should handle the event.
             // We will receive this event agaian later at he ComboBox level and we don't want to repeat the algorithm
			if (event.keyCode == Keyboard.ENTER && event.currentTarget == textInput){
				var s:String = (getPercentNumberFromString(textInput.text)).toString();
				s += "%";
				textInput.text = s;
				_rootFigure.zoomToPercent(0, 0, getPercentNumberFromString(textInput.text) / 100);
			}
		}
		
		/**
		 * The method is called when a default value was selected from the 
		 * dropdown and it closes.
		 */
		override public function close(trigger:Event = null):void {
			super.close(trigger);
			textInput.text = selectedLabel;
			var delta:Number = getPercentNumberFromString(textInput.text) / 100;
			_rootFigure.zoomToPercent(0, 0, delta);
		} 		
		
		/**
		 * The method is called after the combo box was created. 
		 */
		private function creationCompleteHandler(event:FlexEvent):void {
			var valueList:ArrayCollection = new ArrayCollection(["10%","25%","50%","75%","100%","150%","200%","400%"]);
			this.dataProvider = valueList;
			selectedIndex = 4;
			invalidateDisplayList();
		}
		
		/**
		 * When the component is being re-drawn, make sure that the editing cursor is
		 * at the beginning of the editable <code>TextInput</code>.
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			// the method has changed from Flex 3 to 4
			var textInput:Object = this.textInput;
			if (parseInt(mx_internal::VERSION.charAt(0)) == 3) {
				// flex 3
				textInput.setSelection(0, 0);
			} else {
				// flex 4
				textInput.selectRange(0, 0);
			}
			super.updateDisplayList(unscaledWidth, unscaledHeight);
		}
		
	
	}
}