package com.crispico.flower.flexdiagram.ui {
	import mx.core.UIComponent;

	/**
	 * A component displayed during drag&drop of SubEditParts to indicate
	 * where the drop will occur (because for SubEditParts there is a move & insert)
	 * 
	 * @author mircea
	 * @flowerModelElementId _1TuWUBf2Ed-L1am98FeypA
	 */
	public class InsertionVisualFeedback extends UIComponent {
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			graphics.clear();
			graphics.lineStyle(1);
			graphics.drawRect(1, 0, unscaledWidth - 1, unscaledHeight);
			graphics.drawRect(0, -2, 1, unscaledHeight + 4); 
			graphics.drawRect(unscaledWidth - 1, -2, 1, unscaledHeight + 4); 
		}
		
	}
}