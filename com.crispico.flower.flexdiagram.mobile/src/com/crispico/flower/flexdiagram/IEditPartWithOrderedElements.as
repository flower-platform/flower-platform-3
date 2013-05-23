package com.crispico.flower.flexdiagram {
	
	import mx.collections.ListCollectionView;
		
	/**
	 * @flowerModelElementId _xlhg8E-fEeC7b-yDeOd1Uw
	 */
	public interface IEditPartWithOrderedElements {
		
		/**
		 * @param children - list of children EditParts for which the assiciated models that must be moved at given position(s)
		 * 
		 * @param newIndexList - list of indexes where each model in childrenList should be moved. If this contains a single
		 * value then all the editParts in childrenList will be moved at the given index. 
		 * @flowerModelElementId _x3YtAE-fEeC7b-yDeOd1Uw
		 */ 
		 function repositionChildren(children:Array, newIndexList:Array):void;

		/**
		 * For the given <code>childEditPart</code> retrieves the model holder list containing its attached model.
		 * @flowerModelElementId _x3ZUEk-fEeC7b-yDeOd1Uw
		 */ 
		 function getModelHolderList(childEditPart:EditPart):ListCollectionView;
		
		/**
		 * Checks if the figures of <code>child1</code> and <code>child2</code> EditParts intersect.
		 */ 
		function intersects(child1:EditPart, child2:EditPart):Boolean;
		
	}
}