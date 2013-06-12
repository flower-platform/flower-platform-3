package com.crispico.flower.flexdiagram.action
{
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	/**
	 * @flowerModelElementId _E2_xkMesEd-nq8-kOf0AJQ
	 */
	public interface IActionProvider2 {
		
		function getContext():ActionContext;
		
		function fillContextMenu(contextMenu:FlowerContextMenu):void;
	}
}