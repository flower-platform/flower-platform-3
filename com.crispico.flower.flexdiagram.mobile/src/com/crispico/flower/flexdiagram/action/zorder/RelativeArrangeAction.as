package com.crispico.flower.flexdiagram.action.zorder {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.IEditPartWithOrderedElements;
	import com.crispico.flower.flexdiagram.action.Action2;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ListCollectionView;
	
	/**
	 * Action that orders EditParts relative to other EditParts. The selected figures will be placed in front of,
	 * or to the back of other intersecting figures. 
	 * 
	 * @author Luiza
	 * @flowerModelElementId _u81L0lOOEeCqc81Ch_V53g
	 */ 
	public class RelativeArrangeAction extends Action2 {
		
		public static var SORT_OPTION_ASCENDING:uint = 0;
		
		public static var SORT_OPTION_DESCENDING:uint = 1;
				
		/**
		 * This is SORT_OPTION_ASCENDING by default. Subclasses may set this on SORT_OPTION_DESCENDING to change the behavior.
		 * @flowerModelElementId _u89HoFOOEeCqc81Ch_V53g
		 */ 
		protected var sortOption:uint = SORT_OPTION_ASCENDING;
		
		public function RelativeArrangeAction() {
			super();
		}	
		
		public override function run(selection:ArrayCollection):void {
			// duplicate the contents of the selection to avoid modifications on the selection list
			var selectedEditParts:Array = selection.source.slice();			
			var parent:IEditPartWithOrderedElements = IEditPartWithOrderedElements(EditPart(selectedEditParts[0]).getParent());
			var indexList:Array = new Array();
			
			// iterate the selected EditParts and find the index where to move if possible
			// this is computed relative to an intersecting EditPart
			for (var i:int = 0; i < selectedEditParts.length; i++) {
				var ep:EditPart = selectedEditParts[i];
				var eligibleEP:EditPart = getIntersectingEditPart(ep, selectedEditParts);
				
				// no eligible EditPart (relative to which the movement should be done) found 
				if (eligibleEP == null) {
					selectedEditParts.splice(i,1); // remove current ep from the selection (it can't be moved)
					i --;
				} else {
					indexList.push(getIndexForEditPart(eligibleEP)); // find the index
				}
			}
			
			var parentChildrenCollection:ArrayCollection = EditPart(parent).getChildren();
			
			// order the selected editparts descending or ascending based on their new index (indexList)
			// so that movements can be executed in correct order without affecting the computed indexes.
			// Example: when moving elements from front to back (from a bigger index to a smaller one) => 
			// elements from the new index to the old one will be shifted to right. => to make sure the computed indexList remains valid
			// from the first to the last move, movements to bigger indexes must be executed first => they need to be sorted descending
			for (i = 0; i < indexList.length - 1; i++) {
				var referenceIndex:int = i; 
				var index1:int = parentChildrenCollection.getItemIndex(selectedEditParts[referenceIndex]);
				
				for (var j:int = i + 1; j < indexList.length; j++) {
					
					if (indexList[referenceIndex] == indexList[j]) {	
						var index2:int = parentChildrenCollection.getItemIndex(selectedEditParts[j]);
						
						// found two candidates for the same index 
						// then give priority to the EP that is on top; this way the order is preserved when movements in list occur
						if ((sortOption == SORT_OPTION_ASCENDING && index2 < index1) 
							|| (sortOption == SORT_OPTION_DESCENDING && index2 > index1)) {
							referenceIndex = j;
						}
					} else if ((sortOption == SORT_OPTION_ASCENDING && indexList[referenceIndex] > indexList[j]) 
							|| (sortOption == SORT_OPTION_DESCENDING && indexList[referenceIndex] < indexList[j])) {
					 	 referenceIndex = j;
					}
				}
				
				// bring the max or the min on current position
				if (referenceIndex != i) {
					var aux:* = indexList[i];
					indexList[i] = indexList[referenceIndex];
					indexList[referenceIndex] = aux;
				
					aux = selectedEditParts[i];
					selectedEditParts[i] = selectedEditParts[referenceIndex];
					selectedEditParts[referenceIndex] = aux;
				}
			}
			
			parent.repositionChildren(selectedEditParts, indexList);		
		}
		
		/**
		 * Finds the index of the model(<code>View</code>) associated to the given <code>editPart</code>.
		 * @flowerModelElementId _u9HfsVOOEeCqc81Ch_V53g
		 */ 
		protected function getIndexForEditPart(editPart:EditPart):int {
			var collection:ListCollectionView = IEditPartWithOrderedElements(editPart.getParent()).getModelHolderList(editPart);
			
			for (var i:int = 0; i < collection.length; i++) {
				if (editPart.getParent().getModelFromModelHolder(collection[i]) == editPart.getModel()) {
					return i;
				}	
			}
			return -1;
		}
		
		protected function getIntersectingEditPart(editPart:EditPart, ingnoreList:Array):EditPart {
			throw "getIntersectingEditPart() has no implementation";
		}
	}
}