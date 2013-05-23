package com.crispico.flower.flexdiagram {
	
	/**
	 * Implemented by figures that position their figure children absolutely.
	 * 
	 * <p>
	 * Figures for descendents of <code>AbsoluteLayoutEditPart</code> should implement
	 * this interface.
	 * 
	 * @author Cristi
	 * @flowerModelElementId _31vw0MhaEd6f3oj4VmBqug
	 */
	public interface IAbsoluteLayoutFigure extends IFigure {
	
		/**
		 * Returns an array of 4 ints (x1, y1, width, height) representing the visible 
		 * area (according to the scroll bar positions). 
		 */ 
		function getVisibleAreaRect():Array;
		
		/**
		 * Corrects the scrollable surface with <code>maxScrollWidth</code> and <code>maxScrollHeight</code> if the measured 
		 * area of the diagram is smaller then the received values. 
		 * 
		 * @flowerModelElementId _ilTOIEstEeCbK9Qdqt9oUA
		 */
		 function setScrollableArea(maxScrollWidth:Number, maxScrollHeight:Number):void;
	}
}