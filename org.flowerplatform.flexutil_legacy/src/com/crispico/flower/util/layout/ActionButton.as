package com.crispico.flower.util.layout {
	import flash.events.MouseEvent;
	
	import mx.collections.IList;
	
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.context_menu.ContextMenu;
	import org.flowerplatform.flexutil.popup.IAction;
	import org.flowerplatform.flexutil.popup.IComposedAction;
	
	import spark.components.Button;
	
	public class ActionButton extends Button {
		
		public var viewWrapper:PopupHostViewWrapper;
		
		public var action:IAction;
		
		protected override function clickHandler(event:MouseEvent):void {
			super.clickHandler(event);
			if (action is IComposedAction) {
				FlexUtilGlobals.getInstance().contextMenuManager.openContextMenu(event.stageX, event.stageY, viewWrapper.allActions, null, IComposedAction(action).id, viewWrapper.selection);
			} else {
				try {
					action.selection = viewWrapper.selection;
					action.run();
				} finally {
					action.selection = null;
				}
			}
		}
	}
}