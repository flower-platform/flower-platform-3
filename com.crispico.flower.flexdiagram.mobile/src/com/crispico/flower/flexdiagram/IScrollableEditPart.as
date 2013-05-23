package com.crispico.flower.flexdiagram {
	/**
	 * 	Implemented by edit parts that can be scrolled. Diagrams and containers
	 * 	with scroll bars should implement this interface.
	 * @flowerModelElementId _4DBjwH9cEd6eOdiqTgIdyg
	 */
	public interface IScrollableEditPart {
		
		/**
		 * Scrolls to the pixels given as parametters.
		 * @flowerModelElementId _jY3FAn9gEd6eOdiqTgIdyg
		 */
		 function handleExternalScroll(deltaX:int, deltaY:int):void;
	}
}