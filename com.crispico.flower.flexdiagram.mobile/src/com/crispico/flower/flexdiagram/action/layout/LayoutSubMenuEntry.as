package com.crispico.flower.flexdiagram.action.layout {
	
	import com.crispico.flower.flexdiagram.contextmenu.ActionEntry;
	import com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu;
	import com.crispico.flower.flexdiagram.contextmenu.SubMenuEntry;
	import com.crispico.flower.flexdiagram.contextmenu.SubMenuEntryModel;
	
	import mx.collections.ArrayCollection;

	public class LayoutSubMenuEntry extends SubMenuEntry {
		
		/**
		 * The Actions displayed under this SubMenu
		 */ 
		private var _actions:Array;
		
		public function LayoutSubMenuEntry(icon:Object, label:String, parentContextMenu:FlowerContextMenu, sortIndex:int=int.MAX_VALUE) {
			super(new SubMenuEntryModel(icon, label, sortIndex), parentContextMenu);
			_actions = createActions();
		}
		
		/**
		 * Must override in subclasses to create instances of LayoutAction subclasses. 
		 * These actions will be displayed under this menu entry.
		 */ 
		protected function createActions():Array {
			throw new Error("createActions() has no implementation");
		}
		
		override protected function createChildren():void {
			super.createChildren();
			for (var i:int = 0; i < _actions.length; i++) {
				getSubMenu().addChild(new ActionEntry(_actions[i]));		
			}
		}
		
		/**
		 * Checks for vsibility the first action. If visible then all the entries are visible.
		 */ 
		public function isVisible(selection:ArrayCollection):Boolean {
			// actually all Layout Actions will have the same checking on the isVisible
			// if one is visible then all of them are
			return LayoutAction(_actions[0]).isVisible(selection);
		}
		
		
	}
}