package com.crispico.flower.flexdiagram.ui {
	import com.crispico.flower.flexdiagram.EditPart;
	
	import mx.controls.Alert;
	import mx.core.UIComponent;
	
	/**
	 * Subcomponents for <code>AbstractSelectionAnchors</code>. The type
	 * is useful for the EditPart drag related methods (they know what kind of resize
	 * operation is desired from it).
	 *
	 * @author Crist
	 * @flowerModelElementId _b3mOGL8REd6XgrpwHbbsYQ
	 */
	public class ResizeAnchor extends UIComponent implements IDraggable {
	
		/**
		 * Anchor type.
		 * Values are defined as constants inside <code>AbsolutePositionSelectionAnchors</code>
		 * @flowerModelElementId _b3mOHb8REd6XgrpwHbbsYQ
		 */
		private var type:String;
		
		/**
		 * Wide of the anchor
		 * @flowerModelElementId _b3mOIb8REd6XgrpwHbbsYQ
		 */
		private var wide:int = 2;
		
		/**
		 * Constant for 2 colors used when displaying anchors
		 * @flowerModelElementId _b3mOJr8REd6XgrpwHbbsYQ
		 */
		protected const BLACK:uint = 0x000000;
		
		/**
		 * @flowerModelElementId _b3mOK78REd6XgrpwHbbsYQ
		 */
		protected const WHITE:uint = 0xffffff;
		
		/**
		 * Constructor
		 * @flowerModelElementId _b3vX8L8REd6XgrpwHbbsYQ
		 */
		public function ResizeAnchor(type:String) {
			this.type = type;
		}
		
		public function getType():String {
			return type;
		}
		
		/**
		 * @flowerModelElementId _b3vX-b8REd6XgrpwHbbsYQ
		 */
		public function getEditPart():EditPart {
			return AbstractSelectionAnchors(parent).getEditPart();
		}
		
		/**
		 * Updates display for resize anchor.
		 * @flowerModelElementId _b3vX_b8REd6XgrpwHbbsYQ
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth,unscaledHeight);
			graphics.clear();
			
			// inside anchor color
			var inside:uint = WHITE;
			// outside anchor color
			var margin:uint = BLACK;
			if (parent != null && AbstractSelectionAnchors(parent).getMainSelection()) {
				inside = BLACK;
				margin = WHITE;
			}
			graphics.beginFill(inside);
			graphics.drawRect(-wide, -wide, +wide*2+1, +wide*2+1);
			graphics.endFill();
			graphics.lineStyle(1,margin);
			graphics.drawRect(-wide-1, -wide-1, +wide*2+2, +wide*2+2);			
		}
	}
}