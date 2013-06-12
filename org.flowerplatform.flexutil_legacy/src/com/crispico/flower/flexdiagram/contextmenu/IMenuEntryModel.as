package com.crispico.flower.flexdiagram.contextmenu {
	
	/**
	 * All the model classes that lay behind a graphical menu entry inside a FlowerContextMenu 
	 * have to implement this class.
	 * 
	 * @see IAction
	 * @see BaseAction
	 * @see SubMenuEntryModel
	 * @author Dana
	 */ 
	[RemoteClass(alias="com.crispico.flower.mp.contextmenu.IMenuEntryModel")]
	public interface IMenuEntryModel {
		/**
		 * The label of the menu entry. 
		 */
		function get label():String;
		
		/**
		 * The image of the menu entry (a <code>Class</code> of an embeded image).
		 */
		function get image():Object;
		
		/**
		 * Entries can specify a sort index at which should be added in the context menu. If
		 * they don't specify anything, they are added at the end of the context menu.
		 *  
		 * @internal
		 * By default, the sort index is ContextMenu.DEFAULT_SORT_INDEX, meaning they will be added at the bottom of the context menu.
		 */
		function get sortIndex():int; 
	}
}