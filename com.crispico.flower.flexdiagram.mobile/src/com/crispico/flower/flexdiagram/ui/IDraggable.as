package com.crispico.flower.flexdiagram.ui {
	import com.crispico.flower.flexdiagram.EditPart;
	
	/**
	 * Visual components that can be used as drag "initiators" should implement
	 * this interface (necessary for the system to retrieve an EditPart to delegate
	 * the control to).
	 * 
	 * @flowerModelElementId _3v8DychaEd6f3oj4VmBqug
	 */
	public interface IDraggable {
		 /**
		   * Implementors may need to have a field that stores the EditPart.
		   * 
		   * @return The associated EditPart.
		   */
		   function getEditPart():EditPart;
	}
}