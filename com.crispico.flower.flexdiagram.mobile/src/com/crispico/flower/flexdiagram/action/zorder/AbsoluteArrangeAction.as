package com.crispico.flower.flexdiagram.action.zorder {
	
	import com.crispico.flower.flexdiagram.EditPart;
	import com.crispico.flower.flexdiagram.action.Action2;
	
	import mx.collections.ArrayCollection;
	import mx.events.ChildExistenceChangedEvent;

	/**
	 * Action that arrange the selected EditParts absolutely: brings them on top of all the others or 
	 * in the background.
	 * 
	 * @author Luiza
	 * 
	 * @flowerModelElementId _u8WqsFOOEeCqc81Ch_V53g
	 */
	public class AbsoluteArrangeAction extends Action2 {	
		
		/**
		 * Compares two EditParts on their indexInParentList. Returns 0 if the index is equal,
		 * -1 if <code>ep1</code> has smaller index then <code>ep2</code> and 1 if <code>ep1</code>
		 * has bigger index then <code>ep2</code>.
		 * This is equivalent to ordering EditParts based on the Z-index of theirs figures.
		 * Figures with smaller index are placed under figures with bigger index.
		 * 
		 * @flowerModelElementId _u8XRwFOOEeCqc81Ch_V53g
		 */ 
		protected function compareEditParts(ep1:EditPart, ep2:EditPart):int {
			var childrenCollection:ArrayCollection = ep1.getParent().getChildren();
			if (childrenCollection.getItemIndex(ep1) > childrenCollection.getItemIndex(ep2)) {
				return 1;
			} else if (childrenCollection.getItemIndex(ep1) < childrenCollection.getItemIndex(ep2)) {
				return -1;
			} else {
				return 0;
			}
		}
	}
	
}