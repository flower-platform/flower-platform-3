package com.crispico.flower.flexdiagram.action.layout.align {

	import com.crispico.flower.flexdiagram.FlexDiagramAssets;
	import com.crispico.flower.flexdiagram.action.layout.LayoutSubMenuEntry;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	
	/**
	 * SubmenuEntry that contains instances of AbstractAlignAction. 
	 * Users must override <code>createActions()</code> in order to fill this submenu.
	 * 
	 * @see com.crispico.flower.flexdiagram.action.layout.align.AbstractAlignAction
	 * 
	 * @author Luiza
	 */ 
	public class AbstractAlignSubMenuEntry extends LayoutSubMenuEntry {
		
		/**
		 * Creates AbstractAlignSubMenuEntry instance. Adds default label and icon if no others provided.
		 */ 		
		public function AbstractAlignSubMenuEntry(parentContextMenu:FlowerContextMenu, sortIndex:int=int.MAX_VALUE, icon:Object = null, label:String = null) {
			var defaultLabel:String = FlexDiagramAssets.INSTANCE.getMessage("UI_SubMenuEntry_Align");
			var defaultIcon:Object = FlexDiagramAssets.INSTANCE.align_bottom_icon;
			
			super((icon == null ? defaultIcon : icon), (label == null ? defaultLabel : label), parentContextMenu, sortIndex);
		}
		
	}
}