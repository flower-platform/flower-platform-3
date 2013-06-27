package org.flowerplatform.web.mobile.text {
	
	import flash.events.FocusEvent;
	
	import mx.events.FlexEvent;
	
	import spark.skins.mobile.TextInputSkin;
	
	/**
	 * @author Mariana
	 */
	public class ValidatingTextInputSkin extends TextInputSkin {
		
		override protected function createChildren():void {
			super.createChildren();
			
			// trigger validation when the text input loses focus
			textDisplay.addEventListener(FocusEvent.FOCUS_OUT, function(evt:FocusEvent):void {
				textDisplay.dispatchEvent(new FlexEvent(FlexEvent.VALUE_COMMIT));
			});
		}
		
		/**
		 * Copied from super.measure(), except for the part where the text is set, to avoid
		 * an initial validation when the application starts.
		 */
		override protected function measure():void {
			var paddingLeft:Number = getStyle("paddingLeft");
			var paddingRight:Number = getStyle("paddingRight");
			var paddingTop:Number = getStyle("paddingTop");
			var paddingBottom:Number = getStyle("paddingBottom");
			var textHeight:Number = getStyle("fontSize") as Number;
			
			// width is based on maxChars (if set)
			if (hostComponent && hostComponent.maxChars)
			{
				// Grab the fontSize and subtract 2 as the pixel value for each character.
				// This is just an approximation, but it appears to be a reasonable one
				// for most input and most font.
				var characterWidth:int = Math.max(1, (getStyle("fontSize") - 2));
				measuredWidth =  (characterWidth * hostComponent.maxChars) + 
					paddingLeft + paddingRight;
			}
			
			measuredHeight = paddingTop + textHeight + paddingBottom;
		}
		
	}
}