package com.crispico.flower.flexdiagram.action {
	import mx.collections.ArrayCollection;

	/**
	 * Useful base class for actions that are enabled on a multiple selection.
	 * Subclasses should implement <code>isVisibleForSelectedElement()</code>.
	 * 
	 * @see #isVisible()
	 * @see #isVisibleForSelectedElement()
	 * @author Cristi
	 */
	public class BaseMultipleSelectionAction extends BaseAction	{
		
		/**
		 * Iterates over the selected elements and invokes <code>isVisibleForSelectedElement()</code>.
		 * If the selection doesn't contain elements, returns <code>false</code>.
		 */
		override public function isVisible(selectedEditParts:ArrayCollection):Boolean {
			if (selectedEditParts.length == 0) {
				return false;
			}
			
			for each (var object:Object in selectedEditParts) {
				if (!isVisibleForSelectedElement(object)) {
					return false;
				}
			}
			return true;
		}
		
		/**
		 * Should return <code>true</code> if the current element is selected and
		 * <code>false</code> otherwise. It is called once for every element from
		 * the selection. A single <code>false</code> value among these has a veto role, i.e.
		 * the action is not visible.
		 * 
		 * <p>
		 * By default, returns <code>true</code>.
		 */
		protected function isVisibleForSelectedElement(element:Object):Boolean {
			return true;
		}
		
	}
}