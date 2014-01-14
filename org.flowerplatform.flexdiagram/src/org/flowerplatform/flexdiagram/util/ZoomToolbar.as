package org.flowerplatform.flexdiagram.util {
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.ui.Keyboard;
	
	import flashx.textLayout.formats.TextAlign;
	
	import mx.events.FlexEvent;
	
	import org.flowerplatform.flexdiagram.DiagramShell;
	import org.flowerplatform.flexdiagram.renderer.DiagramRenderer;
	import org.flowerplatform.flexdiagram.tool.ZoomTool;
	
	import spark.components.HGroup;
	import spark.components.Label;
	import spark.components.TextInput;
	import spark.layouts.VerticalAlign;

	/**
	 * Displays the current zoom percent.
	 *
	 * @see DiagramEditorFrontend.createChildren()
	 *  
	 * @author Cristina Constantinescu
	 */ 
	public class ZoomToolbar extends HGroup {
		
		private var diagramShell:DiagramShell;
								
		private var textInput:TextInput;
		
		public function ZoomToolbar(diagramShell:DiagramShell) {
			if (diagramShell.tools[ZoomTool] == null) {
				throw new Error("Zoom tool hasn't been register!");
			}
			this.diagramShell = diagramShell;
			this.diagramShell.addEventListener("scaleChanged", updateText);	
			
			verticalAlign = VerticalAlign.MIDDLE;			
		}
			
		private function get diagramRenderer():DiagramRenderer {
			return DiagramRenderer(diagramShell.diagramRenderer);
		}
		
		override protected function createChildren():void {			
			super.createChildren();
			
			var label:Label = new Label();
			label.text = "Zoom:";
			addElement(label);
			
			textInput = new TextInput();
			textInput.setStyle("textAlign", TextAlign.RIGHT);
			textInput.width = 50;
			textInput.addEventListener(KeyboardEvent.KEY_DOWN, keyDownHandler1);
			updateText();
			
			addElement(textInput);
		}	
		
		private function updateText(event:Event = null):void {
			// the general scaleFactor is the arithmetic average of scaleX and scaleY
			textInput.text = ((diagramRenderer.scaleY + diagramRenderer.scaleX) / 2 * 100).toFixed(0).toString() + "%";			
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
				if ((value.charAt(i) >= '0' && value.charAt(i) <= '9') || (value.charAt(i) == '.' && aux.indexOf('.') < 0)) {
					aux += value.charAt(i);
				} else {
					break;
				}
			}
			if (aux == "") {
				return new Number(100);
			}
			return new Number(aux);
		}
		
		protected function keyDownHandler1(event:KeyboardEvent):void {			
			if (event.keyCode == Keyboard.ENTER) {
				// get acceptable scaleFactor
				var scaleFactor:Number = getPercentNumberFromString(textInput.text) / 100;
				if (scaleFactor < ZoomTool(diagramShell.tools[ZoomTool]).minScale) { 
					// too small, set it to zoom tool min value
					scaleFactor = ZoomTool(diagramShell.tools[ZoomTool]).minScale;
				} else if (scaleFactor > ZoomTool(diagramShell.tools[ZoomTool]).maxScale) { 
					// too big, set it to zoom tool max value
					scaleFactor = ZoomTool(diagramShell.tools[ZoomTool]).maxScale;
				}			
				
				diagramRenderer.setScaleFactor(scaleFactor);					
			}
		}
				
	}
}