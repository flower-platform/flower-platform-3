package com.crispico.flower.util.effects
{

	import mx.effects.effectClasses.AnimatePropertyInstance;
	
	/**
	 * From http://ntt.cc/2008/06/22/fade-from-one-color-to-another-animating-color-transitions-in-flex.html.
	 * @flowerModelElementId _jhHJAI-IEeGlZvO-ph04FQ
	 */
	public class FadeColorInstance extends AnimatePropertyInstance {
		
		/**
		 *  
		 * The start color values for each of the r, g, and b channels 
		 * @flowerModelElementId _jhHwEo-IEeGlZvO-ph04FQ
		 */
		protected var startValues:Object;;
		
		/**
		 *  
		 * The change in color value for each of the r, g, and b channels.  
		 * @flowerModelElementId _jhIXII-IEeGlZvO-ph04FQ
		 */
		protected var delta:Object;
		
		/**
		 * @flowerModelElementId _jhIXIo-IEeGlZvO-ph04FQ
		 */
		public function FadeColorInstance(target:Object) {
			super(target);
		}
		
		/**
		 * @flowerModelElementId _jhI-MY-IEeGlZvO-ph04FQ
		 */
		override public function play():void {
			// We need to call play first so that the fromValue is
			// correctly set, but this has the side effect of calling
			// onTweenUpdate before startValues or delta can be set,
			// so we need to check for that in onTweenUpdate to avoid
			// run time errors.
			super.play();
			
			// Calculate the delta for each of the color values
			startValues = intToRgb(fromValue);
			var stopValues:Object = intToRgb(toValue);
			delta = {
						r: (startValues.r - stopValues.r) / duration,
						g: (startValues.g - stopValues.g) / duration,
						b: (startValues.b - stopValues.b) / duration
					};
			
		}
		
		/**
		 * @flowerModelElementId _jhJlQY-IEeGlZvO-ph04FQ
		 */
		override public function onTweenUpdate(value:Object):void
		{
			// Bail out if delta hasn't been set yet
			if (delta == null) {
				return;
			}
			
			// Catch the situation in which the playheadTime is actually more
			// than duration, which causes incorrect colors to appear at the 
			// end of the animation.
			var playheadTime:int = this.playheadTime;
			if (playheadTime > duration) {
				// Fix the local playhead time to avoid going past the end color
				playheadTime = duration;
			}
			 
			// Calculate the new color value based on the elapased time and the change
			// in color values
			var colorValue:int = ((startValues.r - playheadTime * delta.r) << 16)
								+ ((startValues.g - playheadTime * delta.g) << 8)
								+ (startValues.b - playheadTime * delta.b);
			
			// Either set the property directly, or set it as a style
			if (!isStyle) {
				target[property] = colorValue;
			} else {
				target.setStyle(property, colorValue);
			}
		}
		
		/**
		 * @flowerModelElementId _jhKMUY-IEeGlZvO-ph04FQ
		 */
		private function intToRgb(color:int):Object	{
			var r:int = (color & 0xFF0000) >> 16;
			var g:int = (color & 0x00FF00) >> 8;
			var b:int = color & 0x0000FF;
			return {r:r, g:g, b:b};
		}		
	}
} 