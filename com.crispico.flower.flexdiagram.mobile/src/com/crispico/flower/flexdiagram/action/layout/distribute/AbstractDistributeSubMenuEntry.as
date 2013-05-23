package com.crispico.flower.flexdiagram.action.layout.distribute {
	
	import com.crispico.flower.flexdiagram.FlexDiagramAssets;
	import com.crispico.flower.flexdiagram.action.layout.LayoutSubMenuEntry;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	/**
	 * SubmenuEntry that contains instances of AbstractDistributeAction. 
	 * Users must override <code>createActions()</code> in order to fill this submenu.
	 * 
	 * @see com.crispico.flower.flexdiagram.action.layout.distribute.AbstractDistributeAction
	 * 
	 * @author Luiza
	 */ 
	public class AbstractDistributeSubMenuEntry extends LayoutSubMenuEntry {
		
		/**
		 * Creates a new AbstractDistributeSubMenuEntry instance. Adds default label and icon if no others provided.
		 */ 	
		public function AbstractDistributeSubMenuEntry(parentContextMenu:FlowerContextMenu, sortIndex:int=int.MAX_VALUE, icon:Object = null, label:String = null) {
			var defaultLabel:String = FlexDiagramAssets.INSTANCE.getMessage("UI_SubMenuEntry_Distribute");
			var defaultIcon:Object = FlexDiagramAssets.INSTANCE.distribute_horizontal_icon;
			super((icon == null ? defaultIcon : icon), (label == null ? defaultLabel : label) , parentContextMenu, sortIndex);
		}		
	}
}