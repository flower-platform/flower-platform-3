package com.crispico.flower.util.effects
{

	import mx.effects.AnimateProperty;
	import mx.effects.IEffectInstance;	 
	import com.crispico.flower.util.effects.FadeColorInstance;

	/**
	 * Taken from http://ntt.cc/2008/06/22/fade-from-one-color-to-another-animating-color-transitions-in-flex.html.
	 * When changing from a color to another, the color property does not increase incrementaly, 
	 * but for each channel red, green and blue (from color 0x111111, next will be like 0x121212, not 0x111112).
	 * 
	 * @flowerModelElementId _jg6UsI-IEeGlZvO-ph04FQ
	 */
	public class FadeColor extends AnimateProperty {
	
		/**
		 * @flowerModelElementId _jg67wo-IEeGlZvO-ph04FQ
		 */
		public function FadeColor(target:Object=null) {
			super(target);
			instanceClass = FadeColorInstance;
		}		
	} 
} 