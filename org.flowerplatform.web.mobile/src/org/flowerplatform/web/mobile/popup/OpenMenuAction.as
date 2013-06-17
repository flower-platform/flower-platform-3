package org.flowerplatform.web.mobile.popup {
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	import spark.components.View;
	import spark.components.ViewMenuItem;
	
	public class OpenMenuAction extends ActionBase {
		
		protected var view:WrapperViewBase;
		public var viewMenuItems:Vector.<ViewMenuItem>;
		
		public function OpenMenuAction(view:WrapperViewBase) {
			this.view = view;
			icon = CommonPlugin.getInstance().getResourceUrl("images/menu.png");
		}
		
		override public function get enabled():Boolean {
			return viewMenuItems != null && viewMenuItems.length > 0;
		}
		
		
		override public function run():void {
			view.viewMenuItems = viewMenuItems; 
			FlexGlobals.topLevelApplication.viewMenuOpen = true;
		}
		
	}
}