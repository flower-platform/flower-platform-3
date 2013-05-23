package com.crispico.flower.flexdiagram.action {
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	/**
	 * This interface should be implemented if we need to have a FlowerContextMenu 
	 * 
	 * @see DiagramViewer
	 * @author Ioana Hagiescu
	 * @flowerModelElementId _bwSsIb8REd6XgrpwHbbsYQ
	 */
	public interface IActionProvider {

		/**
		 * This method is called each time a FlowerContextMenu is shown; 
		 * At implementation, each subclass can add the any graphic component in the menu.
		 * It can also specify the position where these elements will be added.
		 * @flowerModelElementId _bwSsJb8REd6XgrpwHbbsYQ
		 */
		 function fillContextMenu(menu:FlowerContextMenu):void;
	}
}