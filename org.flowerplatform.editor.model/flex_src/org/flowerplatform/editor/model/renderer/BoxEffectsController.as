/* license-start
* 
* Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
* 
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation version 3.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
* 
* Contributors:
*   Crispico - Initial API and implementation
*
* license-end
*/
package org.flowerplatform.editor.model.renderer {
	import flash.display.DisplayObject;
	import flash.display.GradientType;
	import flash.display.Graphics;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	
	import mx.effects.EffectInstance;
	import mx.effects.Glow;
	import mx.effects.easing.Linear;
	import mx.events.EffectEvent;
	import mx.utils.ColorUtil;
	
	
	public class BoxEffectsController {
		private var _figure:DisplayObject;
		
		// supported effects 
		protected var _glowEffect:Glow;
		
		protected var glowStarted:Boolean = false;
		
		private var _useGlowOnHover:Boolean;
		
		public var color:uint = 0x4682B4;
		
		public function BoxEffectsController(figure:DisplayObject, useGlowOnHover:Boolean=true, color:uint = 0x4682B4) {
			_figure = figure;
			this.color = color;
			this.useGlowOnHover = useGlowOnHover;
		}
		
		public function get useGlowOnHover():Boolean {
			return _useGlowOnHover;
		}
		
		public function set useGlowOnHover(value:Boolean):void {
			if (!_useGlowOnHover && value) {
				// wasn't enabled and now is
				_useGlowOnHover = true;
				// instantiate glow
				_glowEffect = new Glow();
				// init with default values
				glowEffect.color = color;
				glowEffect.blurXTo = glowEffect.blurYTo = 10;
				glowEffect.alphaTo = 0.6;
				glowEffect.easingFunction = Linear.easeIn;
				// add listeners for glow
				figure.addEventListener(MouseEvent.ROLL_OVER, startGlowHandler);
				figure.addEventListener(MouseEvent.ROLL_OUT, stopGlowHandler);
			} else if (_useGlowOnHover && !value) {
				// was enabled and now is not
				_useGlowOnHover = false;
				//_glowEffect = null;
				figure.removeEventListener(MouseEvent.ROLL_OVER, startGlowHandler);
				figure.removeEventListener(MouseEvent.ROLL_OUT, stopGlowHandler);
			}
			// else do nothing 
		}
		
		public function get figure():DisplayObject {
			return _figure;
		}
		
		public function get glowEffect():Glow {
			return _glowEffect;
		}
		
		protected function startGlowHandler(event:Event):void {
			// if the user chooses not to use effects on figure, don't start 
			if (!_useGlowOnHover) {
				return;				
			}
			
			if (glowEffect.isPlaying) {
				glowEffect.end();
			}
			
			glowEffect.play([figure]);
			
			glowStarted = true;
		}
		
		protected function stopGlowHandler(event:Event):void {
			if (glowStarted) {
				glowEffect.startDelay = 0;
				if (glowEffect.isPlaying) {
					glowEffect.reverse();
				} else {
					glowEffect.play([figure], true);
				}
				glowEffect.addEventListener(EffectEvent.EFFECT_END, glowEffectEndHandler);
			}
		}
		
		protected function glowEffectEndHandler(event:EffectEvent):void {
			glowEffect.removeEventListener(EffectEvent.EFFECT_END, glowEffectEndHandler);
			var afterGlowFilters:Array = figure.filters;
			
			// remove the GlowFilter added by the glow effect on the figure
			afterGlowFilters.pop();
			// reset the filters on the figure so that the figure can notice the change 
			figure.filters = afterGlowFilters;
			
			glowStarted = false;
		}
	}
}		
