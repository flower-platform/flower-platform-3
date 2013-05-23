package com.crispico.flower.flexdiagram.action.zorder {
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IEditPartWithOrderedElements;
	
	
	/**
	 * @flowerModelElementId _u9J78FOOEeCqc81Ch_V53g
	 */
	public class SendBackwardAction extends RelativeArrangeAction {
		
		[Embed(source = "/icons/send_backward.gif")]
		private var IconClass:Class;
		
		public function SendBackwardAction() {
			label = "Send Backward";
			image = IconClass;
			
		   // Order the lists descending based on the indexList. Because this action moves elements from a bigger index 
		   // to a smaller one, the movements must occur in reverse order, so that smaller indexes should not be affected and 
		   // the computed indexList remains valid until the last operation. 
			sortOption = SORT_OPTION_DESCENDING;
		} 
		
		/**
		 * Retrieves the first intersecting EditPart, not included in the <code>ignoreList</code> and positioned under the given 
		 * <code>editPart</code>.
		 * This will be used as reference to find the position where the selected <code>editPart</code> should move.
		 */ 
		override protected function getIntersectingEditPart(editPart:EditPart, ignoreList:Array):EditPart {
			var parent:EditPart = editPart.getParent();
									
			for (var i:int = parent.getChildren().getItemIndex(editPart) - 1; i >= 0; i--) {
				var ep:EditPart = EditPart(parent.getChildren().getItemAt(i));
				// TODO:Luiza - incerc sa iau in considerare doar ep care au modelul in aceeasi lista
				// aici ar fi util acel sort index/custom z-index care mi-ar fi suficient sa aleg ep potrivit
				if (ignoreList.indexOf(ep) == -1 && IEditPartWithOrderedElements(parent).intersects(editPart, ep)) {
					return ep;
				}
			}
			
			return null;
		}		
	}
}