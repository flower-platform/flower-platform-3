package com.crispico.flower.flexdiagram {
	
	import flash.display.DisplayObjectContainer;
	
	import mx.core.IVisualElementContainer;
		
	/**
	 * Implemented by composite figures, that divide their content in one container component that holds child-figures 
	 * and other components.
	 * 
	 * <p>Figures supervised by AbsoluteLayoutEditPart, SequentialLayoutEditPart and their descendents may implement
	 * this interface.
	 * 
	 * <p>Note that when calling addChild(), removeChild(), getChildIndex(), removeChildIndex() to add, remove, update
	 * children figures, #getVisualChildrenContainer() must be called to obtain the actual container of the contained 
	 * figures.
	 * <br>Users may use functions provided by AbsolutePositionEditPartUtils.
	 * 
	 * @see AbsolutePositionEditPartUtils#addChildFigure()
	 * @see AbsolutePositionEditPartUtils#removeChildFigure()
	 * @see AbsolutePositionEditPartUtils#removeChildFigureAtIndex()
	 * @see AbsolutePositionEditPartUtils#getChildFigureIndex()
	 * @see AbsolutePositionEditPartUtils#setChildFigureIndex()
	 * 
	 * @author Luiza
	 */ 	
	public interface IComposedFigure {	
		/**
		 * Returns the container component that holds the child figures.
		 */ 
		function getVisualChildrenContainer():IVisualElementContainer;
		
	}
}