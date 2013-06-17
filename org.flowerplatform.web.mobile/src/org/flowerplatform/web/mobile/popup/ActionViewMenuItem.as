package org.flowerplatform.web.mobile.popup {
	import flash.events.MouseEvent;
	
	import org.flowerplatform.flexutil.popup.IAction;
	
	import spark.components.View;
	import spark.components.ViewMenuItem;
	
	public class ActionViewMenuItem extends ViewMenuItem {
		
		public var action:IAction;
		
		public var view:WrapperViewBase;
		
		override protected function clickHandler(event:MouseEvent):void {
			super.clickHandler(event);
			view.actionClickHandler(action);
		}
		
	}
}