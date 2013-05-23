package  com.crispico.flower.flexdiagram.ui {
	import flash.events.Event;
	
	import mx.containers.VBox;
	import mx.controls.Label;
	import mx.core.IVisualElement;
	import mx.core.UIComponent;
	import mx.events.FlexEvent;
	
	/**
	 * 
	 * A component with border and semi-transparent background that can be used
	 * during move (and/or resize) operations for rectangular shaped EditParts.
	 *
	 * @author cristi
	 * @author Georg
	 * @flowerModelElementId _b3mODb8REd6XgrpwHbbsYQ
	 */
	public class MoveResizePlaceHolder extends UIComponent implements IVisualElement {
		
		private var color:uint = 0xCCCCCC;
		
		public function MoveResizePlaceHolder() {
			super();
			// default alpha
			alpha = 0.4;
		}
		
		// SMR-HACK
		public function setColorAndAlpha(newColor:uint, newAlpha:Number):void {
			if (newColor != color || newAlpha != alpha) {
				color = newColor;
				alpha = newAlpha;				
				invalidateDisplayList();
			}
		}
		
		// SMR-HACK
		public function resetPositionAndDimensions():void {
			x = y = width = height = 0;
		}
		
		/**
		 * @flowerModelElementId _b3mOEr8REd6XgrpwHbbsYQ
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			graphics.lineStyle(1);
			graphics.beginFill(color, alpha);
			drawPlaceHolder(unscaledWidth, unscaledHeight);
		}
		
		protected function drawPlaceHolder(width:Number, height:Number):void {
			graphics.drawRect(0, 0, width, height);
		}
	}
}