package com.crispico.flower.flexdiagram.contextmenu
{
	import com.crispico.flower.flexdiagram.action.Action2;

	/**
	 * This menu entry is intended to be use to show messages in the
	 * context menu.
	 * @author Sorin
	 */ 
	public class InfoMenuEntry extends ActionEntry {
		
		public function InfoMenuEntry(label:String) {
			super(new Action2());
			this.enabled = false;
			this.label = label;
			setStyle("textIndent", 16);
		}
	}
}