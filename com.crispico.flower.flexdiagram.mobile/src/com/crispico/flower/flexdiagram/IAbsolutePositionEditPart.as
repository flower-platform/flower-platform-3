package com.crispico.flower.flexdiagram {
	import com.crispico.flower.flexdiagram.ui.AbsolutePositionSelectionAnchors;
	
	/**
	 * Implemented by EditParts that are positioned absolutely. Their parent is
	 * a subclass of <code>AbsoluteLayoutEditPart</code>.
	 * 
	 * <p>
	 * Classes that implement this interface should:
	 * 
	 * <ul>
	 * 		<li>use <code>AbsoluteLayoutEditPartUtils</code>: for each method 
	 * 			from this class, override the corresponding method from
	 * 			EditPart and delegate to <code>AbsoluteLayoutEditPartUtils</code>,
	 * 		<li>dispatch <code>BoundsRectChangedEvent</code> when the associated
	 * 			model is modified (if there is no figure associated and the bound 
	 * 			rectangle is affected).
	 * 
	 * <p>
	 * We use this mechanism to "simulate" multiple inheritance (e.g. from
	 * <code>SequentialLayoutEditPart</code> and from 
	 * <code>AbsoluteLayoutEditPart</code>.  
	 * 
	 * @author Cristi
	 * @flowerModelElementId _E65HoKnzEd-ENvSoHQK17A
	 */
	public interface IAbsolutePositionEditPart {
		
		/**
		 * Returns an array of 4 ints (x1, y1, width, height) containing the coordinatea
		 * of the model (not the figure). These coordinates come from the model and the 
		 * implementor knows how to get them. In general when this method is called, a
		 * figure is not associated to the EditPart so we need the coordinates directly
		 * from the model rather than the Flex component (the figure).
		 */
		function getBoundsRect():Array;
		function getSelectionComponent():AbsolutePositionSelectionAnchors;
		function setSelectionComponent(selection:AbsolutePositionSelectionAnchors):void;
		function createNewSelectionComponent():AbsolutePositionSelectionAnchors;
	}
}