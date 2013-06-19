package org.flowerplatform.flexutil.context_menu {
	import flash.display.DisplayObject;
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayList;
	import mx.collections.IList;
	import mx.core.FlexGlobals;
	import mx.managers.PopUpManager;
	
	import org.flowerplatform.flexutil.popup.ActionUtil;
	import org.flowerplatform.flexutil.popup.IAction;

	public class ContextMenuManager	{
		
		public var contextMenuStack:Vector.<ContextMenu> = new Vector.<ContextMenu>();
		
		public var allActions:Vector.<IAction>;
		
		public var selection:IList;
		
		public function ContextMenuManager() {
			FlexGlobals.topLevelApplication.stage.addEventListener(MouseEvent.RIGHT_CLICK, rightClickHandler);
		}
		
		public function dispose():void {
			FlexGlobals.topLevelApplication.stage.removeEventListener(MouseEvent.RIGHT_CLICK, rightClickHandler);
		}
		
		protected function rightClickHandler(event:MouseEvent):void {
			if (contextMenuStack.length > 0) {
				contextMenuStack[0].closeContextMenuStack(0);
			}
			var currentElementUnderMouse:DisplayObject = DisplayObject(event.target);
			while (currentElementUnderMouse != null) {
				if (currentElementUnderMouse.hasEventListener(FillContextMenuEvent.FILL_CONTEXT_MENU)) {
					dispatchSimulatedMouseDownEvent(DisplayObject(event.target), currentElementUnderMouse, event);
					var cmEvent:FillContextMenuEvent = new FillContextMenuEvent();
					currentElementUnderMouse.dispatchEvent(cmEvent);

					if (cmEvent.allActions != null && cmEvent.allActions.length > 0) {
						var cm:org.flowerplatform.flexutil.context_menu.ContextMenu = new org.flowerplatform.flexutil.context_menu.ContextMenu();
						allActions = cmEvent.allActions;
						selection = cmEvent.selection;
						cm.openContextMenu(this, event.stageX, event.stageY, cmEvent.rootActionsAlreadyCalculated, null);		
					}
					return;
				}
				currentElementUnderMouse = currentElementUnderMouse.parent;
			}
		}
		
		protected function dispatchSimulatedMouseDownEvent(startingWith:DisplayObject, endingWidth:DisplayObject, event:MouseEvent):void {
			var simulatedLeftClickEvent:MouseEvent = new MouseEvent(
				MouseEvent.MOUSE_DOWN, event.bubbles, event.cancelable, event.localX, event.localY,
				event.relatedObject, event.ctrlKey, event.altKey, event.shiftKey, 
				event.buttonDown, event.delta);
			var currentElementUnderMouse:DisplayObject = DisplayObject(event.target);
			while (currentElementUnderMouse != null && currentElementUnderMouse != endingWidth) {
				currentElementUnderMouse.dispatchEvent(simulatedLeftClickEvent);
				currentElementUnderMouse = currentElementUnderMouse.parent;
			}

		}
		
		public function openContextMenu(x:Number, y:Number, allActions:Vector.<IAction>, actionsForCurrentLevelAreadyCalculated:IList, rootActionId:String, selection:IList):void {
			if (contextMenuStack.length > 0) {
				contextMenuStack[0].closeContextMenuStack(0);
			}
			this.allActions = allActions;
			this.selection = selection;
			new ContextMenu().openContextMenu(this, x, y, actionsForCurrentLevelAreadyCalculated, rootActionId);
		}
	}
}