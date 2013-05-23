package  com.crispico.flower.flexdiagram.util.tabNavigator {
	
	import com.crispico.flower.flexdiagram.util.common.ButtonUtils;
	
	import flash.display.DisplayObject;
	
	import flexlib.controls.tabBarClasses.SuperTab;
	
	import mx.controls.Button;
	import mx.core.IFlexDisplayObject;
	import mx.core.mx_internal;
	
	use namespace mx_internal;
	
	/**
	 * Besides knowing how to retrive an image from a Class, this graphical component has the
	 * following functionality : setting the "iconURL" style, knows how to show the image
	 * based on its URL.
	 * 
	 * @author Cristina
	 * @flowerModelElementId _wozM0PDSEd-yKrmCiYZiog
	 */
	public class FlowerSuperTab extends SuperTab {		
		
		[Embed(source="/icons/closeContextMenu.png")]      
		private var closeButtonIcon:Class;
		
		public function FlowerSuperTab() {
				
		}
		
		/**
		 * @flowerModelElementId _6zPAgPDSEd-yKrmCiYZiog
		 */
		override mx_internal function viewIconForPhase(tempIconName:String):IFlexDisplayObject {
			return ButtonUtils.viewIconForPhase(this, tempIconName);
		}
		
		override public function addChild(child:DisplayObject):DisplayObject {			
			if (child is Button) {
				var closeButton:Button = child as Button;			
				closeButton.setStyle("icon", closeButtonIcon);
			}
			return super.addChild(child);
		}
	}
	
}