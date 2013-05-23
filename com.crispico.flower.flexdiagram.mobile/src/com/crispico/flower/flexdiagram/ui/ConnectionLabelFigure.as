package com.crispico.flower.flexdiagram.ui {
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IFigure;
	import com.crispico.flower.flexdiagram.connection.ConnectionLabelEditPart;
	
	import mx.controls.Label;

	/**
	 * The figure associated with a <code>ConnectionLabelEditPart</code>.
	 * @author Georgi
	 * @flowerModelElementId _VpKzkABREd-COsJ6v0d3bA
	 */
	public class ConnectionLabelFigure extends Label implements IFigure {
	
		/**
		 * The associated editPart.
		 */
		private var editPart:ConnectionLabelEditPart;
		
		/**
		 * Selection rect display attributes
		 */
		protected const SELECTION_RECT_THICKNESS:Number = 1;
	
		/**
		 * Selection rectangle color
		 */
		protected const SELECTION_RECT_LINE_COLOR:uint = 0x000000;
		
		/**
		 * Selection rectangle alpha
		 */
		protected const SELECTION_RECT_LINE_ALPHA:Number = 1;
		
		/**
		 * Selection rectangle fill color 
		 */
		protected const SELECTION_RECT_FILL_COLOR:uint = 0x98AFC7; 
		
		/**
		 * Selection rectangle fill alpha
		 */
		protected const SELECTION_RECT_FILL_ALPHA:Number = 1;
		
		/**
		 * Called from <code>ConnectionLabelEditPart.refreshVisualChildren()</code>.
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();								
			if (getEditPart() != null && getEditPart().getSelected()) {
				// draw selection rect
				graphics.lineStyle(SELECTION_RECT_THICKNESS, SELECTION_RECT_LINE_COLOR, SELECTION_RECT_LINE_ALPHA);
				graphics.beginFill(SELECTION_RECT_FILL_COLOR, SELECTION_RECT_FILL_ALPHA);
				graphics.drawRect(0, 0, unscaledWidth, unscaledHeight);
			}
		}
	
		public function setEditPart(ep:EditPart):void {
			this.editPart = ConnectionLabelEditPart(ep);
		}
		
		public function getEditPart():EditPart {
			return editPart;
		}
	}
}