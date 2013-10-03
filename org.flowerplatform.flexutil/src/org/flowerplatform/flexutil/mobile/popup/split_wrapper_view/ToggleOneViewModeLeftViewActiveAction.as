package org.flowerplatform.flexutil.mobile.popup.split_wrapper_view {
	import org.flowerplatform.flexutil.FlexUtilAssets;
	import org.flowerplatform.flexutil.popup.ActionBase;
	
	/**
	 * @author Cristian Spiescu
	 */
	public class ToggleOneViewModeLeftViewActiveAction extends ActionBase {
		
		[Embed(source="switch_view.png")]
		public static var ICON:Class;
		
		public var splitWrapperView:SplitWrapperView;
		
		public function ToggleOneViewModeLeftViewActiveAction() {
			super();
			icon = ICON;
		}
		
		override public function get label():String {
			if (splitWrapperView.oneViewModeLeftViewActive) {
				return FlexUtilAssets.INSTANCE.getMessage("SplitWrapperView.ToggleOneViewModeActionLeftViewActive.right");
			} else {
				return FlexUtilAssets.INSTANCE.getMessage("SplitWrapperView.ToggleOneViewModeActionLeftViewActive.left");
			}
		}
		
		override public function run():void {
			splitWrapperView.oneViewModeLeftViewActive = !splitWrapperView.oneViewModeLeftViewActive;
			// force refresh so that the new label/enablement is taken into account
			splitWrapperView.refreshActions(splitWrapperView.activePopupContent);

		}
		
		override public function get visible():Boolean {
			return splitWrapperView.oneViewMode && (selection.length == 0 || !splitWrapperView.visibleOnlyOnEmptySelection);
		}		
		
	}
}