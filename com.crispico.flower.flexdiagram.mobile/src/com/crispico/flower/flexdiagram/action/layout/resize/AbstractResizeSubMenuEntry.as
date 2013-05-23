package com.crispico.flower.flexdiagram.action.layout.resize {
	
	import com.crispico.flower.flexdiagram.FlexDiagramAssets;
	import com.crispico.flower.flexdiagram.action.layout.LayoutSubMenuEntry;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;

	/**
	 * SubmenuEntry that contains instances of AbstractResizeAction. 
	 * Users must override <code>createActions()</code> in order to fill this submenu.
	 * 
	 * @see com.crispico.flower.flexdiagram.action.layout.resize.AbstractResizeAction
	 * 
	 * @author Luiza
	 */ 
	public class AbstractResizeSubMenuEntry extends LayoutSubMenuEntry {
			
		/**
		 * Creates a new AbstractResizeSubMenuEntry instance. Adds default label and icon if no others provided.
		 */ 	
		public function AbstractResizeSubMenuEntry(parentContextMenu:FlowerContextMenu, sortIndex:int=int.MAX_VALUE, icon:Object = null, label:String = null){
			var defaultLabel:String = FlexDiagramAssets.INSTANCE.getMessage("UI_SubMenuEntry_Resize");
			var defaultIcon:Object = FlexDiagramAssets.INSTANCE.resize_width_icon;
			super((icon == null ? defaultIcon : icon), (label == null ? defaultLabel : label) , parentContextMenu, sortIndex);
		}
		
	}
}