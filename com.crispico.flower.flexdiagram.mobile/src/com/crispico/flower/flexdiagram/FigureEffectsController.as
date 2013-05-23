package com.crispico.flower.flexdiagram {
	
	import com.crispico.flower.flexdiagram.tool.Tool;
	
	import flash.display.DisplayObject;
	import flash.display.GradientType;
	import flash.display.Graphics;
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.geom.Matrix;
	
	import mx.effects.EffectInstance;
	import mx.effects.Glow;
	import mx.effects.Move;
	import mx.effects.Parallel;
	import mx.effects.Resize;
	import mx.effects.easing.Linear;
	import mx.events.EffectEvent;
	import mx.utils.ColorUtil;
	
	/**
	 * Utility class that adds various effects (glow, resize, move) on a host figure. Offers possibility to draw a gradient colored rectangle.
	 * To use the functionality offered by this class, a figure should create (contain) a FigureEffectsController instance
	 * and pass itself as parameter.
	 * 
	 * <p>The figure may acces and customize the effects. It can call <code>drawGradientColoredRectangle()</code> 
	 * (from <code>updateDisplayList()</code>) passing its Graphics object.
	 *
	 * <p>If used, the <code>glowEffect</code> will be created and automatically start on mouse roll over event and make a reverse play 
	 * on roll out event. Users may choose to suppress the glow by setting "createGlow" parameter on <code>false</code> in the constructor.
	 * 
	 * <p>To move or a resize a figure (with or without effect) users should call <code>refreshPositionAndSize()</code> function.
	 * 
	 * @see refreshPositionAndSize()
	 * @see drawGradientColoredRectangle()
	 * 
	 * @see Glow
	 * @see Resize
	 * @see Move
	 * 
	 * @author Luiza
	 * 
	 * @flowerModelElementId _3vlkoM_pEd-LVLAmkCpx7g
	 */ 	
		
	public class FigureEffectsController {
		private var _figure:DisplayObject;
		
		// supported effects 
		protected var _glowEffect:Glow;
		
		protected var _resizeEffect:Resize;
		
		protected var _moveEffect:Move;
		
		/**
		 * Used to run move and resize effects togather
		 */ 
		protected var parallel:Parallel;
		
		/**
		 * Becomes <code>true</code> when <code>glowEffect</code> starts and returns to <code>false</code> when 
		 * a glow cycle finished: the glow played normal and reverse(light up and down).
		 * 
		 * @see #startGlowHandler()
		 * @see #stopGlowHandler()
		 * @see #glowEffectEndHandler()
		 */ 
		protected var glowStarted:Boolean = false;
		
		/**
		 * The figure that uses this class can have or not effects.
		 * It cannot be modified from the flex side.
		 * @author Cristina
		 * @flowerModelElementId _mIOugPfMEd-YZILiw6fSRw
		 */
		private var _useTransitionEffects:Boolean;

		private var _useGlowOnHover:Boolean;
		
		/**
		 * Creates a new FigureEffectsControler instance.
		 * If user does not set on <code>false</code> the <code>useHoverEffects</code> parameter, the <code>glowEffect</code> is
		 * instantiated and initialized in the following form:
		 * <ul>
		 * 	<li>color = 0x4682B4 (blue-gray)
		 * 	<li>blurXTo = blurYTo = 10
		 *  <li>alphaTo = 0.6
		 * 	<li>easingFunction = Linear.easeIn 
		 * </ul>
		 * 
		 * <p>By default, this effect will create a blue-gray glow around the figure with a linear acceleration. 
		 * The user is free to customize the effect by resseting or adding other parameters.
		 * The effect starts on mouse roll over event and run in reverse on roll out.
		 * 
		 * @parameter figure the figure on which the effects will play
		 * @parameter useHoverEffects when <code>true</code> a glow effect will play on the figure.
		 * @flowerModelElementId _3vuHgs_pEd-LVLAmkCpx7g
		 */ 
		public function FigureEffectsController(figure:DisplayObject, useGlowOnHover:Boolean=true, useTransitionEffects:Boolean=true) {
			_figure = figure;
			_useTransitionEffects = useTransitionEffects;
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
				glowEffect.color = 0x4682B4;
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
		
		/**
		 * The figure on which the effects will play.
		 * @flowerModelElementId _3vs5YM_pEd-LVLAmkCpx7g
		 */ 
		public function get figure():DisplayObject {
			return _figure;
		}
		
		/**
		 * Public getter that may be used to customize the effect's properties.
		 * Initialized on construction.
		 * @flowerModelElementId _3vtgcs_pEd-LVLAmkCpx7g
		 */ 
		public function get glowEffect():Glow {
			return _glowEffect;
		}
		
		/**
		 * Public getter that may be used to customize the effect's properties.
		 * Lazy initialization on first access.
		 * @flowerModelElementId _3vtgdM_pEd-LVLAmkCpx7g
		 */ 
		public function get moveEffect():Move {
			if (_moveEffect == null) {
				_moveEffect = new Move(_figure);
			}	
			return _moveEffect;
		}
		
		/**
		 * Public getter that may be used to customize the effect's properties.
		 * Lazy initialization on first access.
		 * @flowerModelElementId _3vuHgM_pEd-LVLAmkCpx7g
		 */ 
		public function get resizeEffect():Resize {
			if (_resizeEffect == null) {
				_resizeEffect = new Resize(_figure); 
			}
			return _resizeEffect;
		}
		
		/**
		 * Plays the <code>glowEffect</code> on the figure, but only if the active tool does 
		 * not anounce mouse drag in progress. Otherwise ugly glow on/off effects may appear 
		 * during a rapid drag.
		 * 
		 * <p>If the effect starts then sets <code>glowStarted</code> on <code>true</code> 
		 * announcing <code>rollOutHandler()</code> that the effect can also play reverse.
		 * @flowerModelElementId _3vxK0c_pEd-LVLAmkCpx7g
		 */ 
		protected function startGlowHandler(event:Event):void {
			//trace ("start " + figure + " " + new Date());
			// if the user chooses not to use effects on figure, don't start 
			if (!_useGlowOnHover) {
				return;				
			}
			
			var canPlay:Boolean = true;			
			if (figure is IFigure && IFigure(figure).getEditPart()) {
				var tool:Tool = IFigure(figure).getEditPart().getViewer().getActiveTool();
				if (tool != null) {
					canPlay = !tool.isMouseDragInProgress();
				}
			}
			
			//trace ("	" + canPlay);
			//trace ("	" + glowEffect);
			if (canPlay) {
				if (glowEffect.isPlaying) {
					glowEffect.end();
				}
				
				glowEffect.play([figure]);
				
				glowStarted = true;
			}
		}
		
		/**
		 * Plays the glowEffect in reverse form, only if <code>glowStarted</code> is 
		 * <code>true</code>.
		 * @flowerModelElementId _3vxx4M_pEd-LVLAmkCpx7g
		 */ 
		protected function stopGlowHandler(event:Event):void {
			//trace ("stop " + figure + " " + new Date());
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
		
		/**
		 * Removes any glow marks from the figure and resets <code>glowStarted</code> 
		 * to <code>false</code>.
		 * @flowerModelElementId _3vxx5M_pEd-LVLAmkCpx7g
		 */ 
		protected function glowEffectEndHandler(event:EffectEvent):void {
			glowEffect.removeEventListener(EffectEvent.EFFECT_END, glowEffectEndHandler);
			var afterGlowFilters:Array = figure.filters;
			
			// remove the GlowFilter added by the glow effect on the figure
			afterGlowFilters.pop();
			// reset the filters on the figure so that the figure can notice the change 
			figure.filters = afterGlowFilters;
			
			glowStarted = false;
		}
		
		/**
		 * Repositions and/or resizes the figure. If "withEffect" parameter is <code>false</code> 
		 * the figure is modified directly; otheriwse plays <code>moveEffect</code> and/or 
		 * <code>resizeEffect</code>.
		 * 
		 * <p>If one of the numbers parameters is NaN, it's value is taken from the figure 
		 * (i.e. that property is left unchanged). If a move and resize is instructed, the move
		 * and resize effects are played in parallel.
		 * @flowerModelElementId _J7ZYsNKmEd-1M_MsA2KVmw
		 */ 
		public function refreshPositionAndSize(xTo:Number, yTo:Number, widthTo:Number, heightTo:Number, withEffect:Boolean=true):void {
			var move:Boolean = !isNaN(xTo) || !isNaN(yTo), resize:Boolean = !isNaN(widthTo) || !isNaN(heightTo);
			
			if (withEffect && _useTransitionEffects) {
				moveEffect.xTo = xTo;
				moveEffect.yTo = yTo;
				resizeEffect.widthTo = widthTo;
				resizeEffect.heightTo = heightTo;
				
				if (move && resize) { // do both move and resize
					if (parallel == null) {
						parallel = new Parallel();
						parallel.addChild(moveEffect);
						parallel.addChild(resizeEffect);
					}
					if (parallel.isPlaying) {
						parallel.stop();
					}
					parallel.play([figure]);
				} else if (move) { // do just move	
					if (moveEffect.isPlaying) {
						moveEffect.stop(); // use stop instead of end because end method will stop all MoveEffect instances that are running (see end doc)
					}					
					moveEffect.play([figure]);
				} else if (resize) { // do just resize
					if (resizeEffect.isPlaying) {
						resizeEffect.stop();
					}					
					resizeEffect.play([figure]);
				}
			} else {
				// move
				figure.x = isNaN(xTo) ? figure.x : xTo;
				figure.y = isNaN(yTo) ? figure.y : yTo;
				//resize
				figure.width = isNaN(widthTo) ? figure.width : widthTo;
				figure.height = isNaN(heightTo) ? figure.height : heightTo;	
			}
		}
		
		/**
		 * Useful for the mechanism that auto-grows the size of a container when its 
		 * children grow.
		 * @flowerModelElementId _J70PcNKmEd-1M_MsA2KVmw
		 */ 
		public function stopMoveResize():void {
			if (parallel && parallel.isPlaying) {
				parallel.stop();
			} else if (_resizeEffect && _resizeEffect.isPlaying) {
				resizeEffect.stop();
			} else if (_moveEffect && _moveEffect.isPlaying) {
				moveEffect.stop();
			}
		}
		
		/**
		 * Sets gradient options and calls <code>graphics.beginGradientFill()</code>.
		 */ 
		public function beginGradientFill(graphics:Graphics, color:uint, width:Number, height:Number):void {
			var fillColors:Array = [ColorUtil.adjustBrightness2(color, 45), color];
       		var matrix:Matrix = new Matrix();
       		matrix.createGradientBox(width, height, Math.PI/2);
         	graphics.beginGradientFill(GradientType.LINEAR, fillColors, [1, 1], [100, 255], matrix);
		}
		
		/**
		 * Figures may optionally use this utility function.
		 * 
		 * <p>Draws a gradient colored rectangle based on the given parameters. Figure must provide the Graphics, the color and the bounds of 
		 * the rectangle. By default, the rectangle has rounded corners. The user may also give different valuse for "cornerRadius" parameter to
		 * modify the style of the corners. 
		 * @flowerModelElementId _J8I_ktKmEd-1M_MsA2KVmw
		 */ 
		public function drawGradientColoredRectangle(graphics:Graphics, color:uint, fromX:Number, fromY:Number, width:Number, height:Number, cornerRadius:Number=10, borderColor:uint = 0, borderThickness:int = 0):void {
        	if (borderThickness > 0) {
	       		var matrix:Matrix = new Matrix();
    	   		matrix.createGradientBox(width, height, Math.PI/2);
       			graphics.lineStyle(borderThickness);
          		graphics.lineGradientStyle(GradientType.LINEAR, [ColorUtil.adjustBrightness2(borderColor, 45), borderColor], [1, 1], [100, 255], matrix);
       		}
         	beginGradientFill(graphics, color, width, height);
          	graphics.drawRoundRect(fromX, fromY, width, height, cornerRadius);
          	graphics.endFill();
		}
	}
}