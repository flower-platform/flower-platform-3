package com.crispico.flower.flexdiagram.print {
	
	import flash.display.DisplayObject;
	import flash.text.TextFormat;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Label;
	import mx.controls.Text;
	import mx.core.*;
	
	/**
	 * This class is used by the printing algorithm to force Labels to show all text without truncation.
	 * <p>
	 * Desigend to be used as single instance. User should call the "INSTANCE" static variable to obtain the instance of this class.
	 * User will apply a scale factor on a given Container then call "blockTextTruncation()" passing the Container; then make any 
	 * other operations like printing.
	 * <p>
	 * In the end user must call "restoreOriginalFonts()" to make sure all the labels inside the container get back to their original font size.
	 * 
	 * <p> Note that calling the following code is necessary to make sure DiagramScaleListener will notice text truncation:
	 * <code> UIComponentGlobals.layoutManager.usePhasedInstantiation = false;
	 * 		  UIComponentGlobals.layoutManager.validateNow(); </code>
	 * 
	 * @author Luiza
	 * @flowerModelElementId _ym7-MDUSEeCTrKdImkKvZg
	 */ 
	public class DiagramScaleListener {
		
		public static const INSTANCE:DiagramScaleListener = new DiagramScaleListener();
		
		/**
		 * Dictionary that keeps a list for each font style applied on the labels of a diagram.
		 * <p>
		 * The key is built using the font plus a "B" or an "I" depending if it is Bold or Italic and the initial font size.
		 * <br>Example: "ArialB11", "Verdana10" etc.
		 * 
		 * <p> 
		 * The first two items in the list are numbers indicating the initial font size and the current font size
		 * - after eventual decreasing - applied on the labels.
		 * The rest of the items are Labels that started from the initial font size and that now have the current font size.
		 */ 
		private var labels:Dictionary = new Dictionary();
			
		/**
		 * Clears the labels dictionary.
		 */ 	
		private function clearDictionary():void {
			for (var key:Object in labels) {
				delete labels[key];
			}
		}
		
		/**
		 * Giving a font key, retrieves the list of labels mapped at the given key if any, otherwise, creates a new collection
		 * and maps it at key, setting the first two elements: originalFontSize and currentSize.
		 */ 
		private function getLabelsAtKey(key:Object, originalFontSize:Number, currentSize:Number): ArrayCollection {
			if (labels[key] == null) {
				var value:ArrayCollection = new ArrayCollection();
				//trace("Add font key: " + key + " original size: " + originalFontSize);
				value.addItem(originalFontSize);
				value.addItem(currentSize);
				
				labels[key] = value;	
				
				return value;
			}
			return labels[key];
		}
		
		use namespace mx_internal;
		
		/**
		 * Recursive function that traces all the children of the given Container to find any Label elements.
		 * For each new container calls blockTextTruncation again. 
		 * For each found Label calls decreaseFontSizeIfNeeded().
		 * @flowerModelElementId _ym9zYzUSEeCTrKdImkKvZg
		 */ 
		public function blockTextTruncation(content:Container):void {
			var children:Array = content.getChildren();
			
			for (var i:int = 0; i < children.length; i++) {
				var child:DisplayObject = DisplayObject(children[i]);
				if (child is Label && !(child is Text)) {
					decreaseFontSizeIfNeeded(Label(child));
				} else if (child is Container) {
					blockTextTruncation(Container(child));
				}
			}
		}
		
		/**
		 * Go through the elements of the "labels" dictionary and restore original fonts. 
		 * Clears the dictionary.
		 * @flowerModelElementId _ym_BgTUSEeCTrKdImkKvZg
		 */ 
		public function restoreOriginalFonts():void {
			for (var key:Object in labels) {
				var value:ArrayCollection = labels[key];
				//trace(key + "[" + (value.length - 2) +  " labels] go back to " + value[0]);
				
				// go back to original font size;	
			 	for (var i:int = 2; i < value.length; i++) {
			 		setFontSize(int(value[0]), Label(value[i]));
			 	}
			 	delete labels[key];
			}
		}
		
		/**
		 * If the given Label truncates its text to fit the size, then make its font smaller until the text is no longer
		 * truncated. Adds the label to the "labels" Dictionary.
		 */ 
		private function decreaseFontSizeIfNeeded(label:Label):void {
			var textField:IUITextField = label.getTextField();
			var tFormat:TextFormat = textField.getTextFormat();
			var originalFontSize:int = int(tFormat.size);			
			var key:String = tFormat.font;
			
			if (tFormat.bold as Boolean)
				key = key + "B";
			if (tFormat.italic as Boolean)
				key = key + "I";
				
			key = key + originalFontSize;
									
			// just in case it was truncated with "..."
			// reset the text to original form
			textField.text = label.text;
					
			// try to resize font to make it smaller until the text no longer needs to be truncated
			// font sie is always rounded up to an integer; it can't be set as partial numbers like 10.5 
			while (textField.truncateToFit() && tFormat.size > 1) {
				textField.text = label.text;
				tFormat.size = int(tFormat.size) - 1;
				textField.setTextFormat(tFormat);	
			}
			
			var value:ArrayCollection = getLabelsAtKey(key, originalFontSize, int(tFormat.size));
			
			// level all the labels with same font style so that they all get to the same font size 
			if (tFormat.size < value[1]) {
				value[1] = tFormat.size;
				for (var i:int = 2; i < value.length; i++) {
					setFontSize(value[1], Label(value[i]));
				}
				
			} else if (tFormat.size > value[1]) {
				setFontSize(value[1],label);
			}
			value.addItem(label);
		
			//trace("font " + key +  " size changed to: " + value[1]);	
		}
		
		/**
		 * Sets the font of the given Label to "size".
		 */ 
		private function setFontSize(size:Number, label:Label):void {
			var textField:IUITextField = label.getTextField();
			var tFormat:TextFormat = textField.getTextFormat();
			tFormat.size = size;
			textField.setTextFormat(tFormat);
		}

	}
}