package org.flowerplatform.flexutil.context_menu {
	
	import flash.events.Event;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.popup.IAction;
	
	public class FillContextMenuEvent extends Event {
		
		public static const FILL_CONTEXT_MENU:String = "fillContextMenu";
		
		public var allActions:Vector.<IAction>;
		
		public var rootActionsAlreadyCalculated:IList;
		
		public var selection:IList;
		
		public function FillContextMenuEvent() {
			super(FILL_CONTEXT_MENU);
		}
	}
}