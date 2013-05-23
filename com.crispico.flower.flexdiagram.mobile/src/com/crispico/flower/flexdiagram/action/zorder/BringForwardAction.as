package com.crispico.flower.flexdiagram.action.zorder {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IEditPartWithOrderedElements;
	
	/**
	 * @flowerModelElementId _u80kwFOOEeCqc81Ch_V53g
	 */
	public class BringForwardAction extends RelativeArrangeAction {
		
		[Embed(source = "/icons/bring_forward.gif")]
		private var IconClass:Class;
		
		public function BringForwardAction() {
			label = "Bring Forward";
			image = IconClass;
		}	
		
		/**
		 * Retrieves the first intersecting EditPart, not included in the <code>ignoreList</code> and positioned over the given 
		 * <code>editPart</code>.
		 * This will be used as reference to find the position where the selected <code>editPart</code> should move.
		 * @flowerModelElementId _u81y41OOEeCqc81Ch_V53g
		 */ 
		override protected function getIntersectingEditPart(editPart:EditPart, ignoreList:Array):EditPart {
			var parent:EditPart = editPart.getParent();
			
			// start searching from this editPart forward					
			for (var i:int = parent.getChildren().getItemIndex(editPart) + 1; i < parent.getChildren().length; i++) {
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