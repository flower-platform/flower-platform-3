package com.crispico.flower.util.layout.view {
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	/**
	 * Interface that needs to be implemented in order to provide additional support for a view tab. <br>
	 * Objects that implements this interface must provide functionality for the following methods:
	 * <ul>
	 * 	<li> fillContextMenu() - adds to context menu additional actions. 	
	 * </ul>
	 * 
	 * @author Cristina	 * 
	 * @flowerModelElementId _be4soOCVEeGdYcOEhSk3ug
	 */
	public interface ITabCustomizer {
		
		/**
		 * @flowerModelElementId _h_mAkOCVEeGdYcOEhSk3ug
		 */
		 function fillContextMenu(contextMenu:FlowerContextMenu):void;
	}
}