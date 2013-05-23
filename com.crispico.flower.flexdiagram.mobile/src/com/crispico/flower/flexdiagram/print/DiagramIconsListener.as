package com.crispico.flower.flexdiagram.print {
	
	import com.crispico.flower.flexdiagram.util.common.BitmapContainer;
	
	import flash.display.Bitmap;
	import flash.display.DisplayObject;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.controls.Image;
	import mx.core.UIComponent;
	
	/**
	 * This class is used by the printing algorithm to remove all icons from the diagram figures when using vectorial printing.
	 * <p>
	 * Desigend to be used as single instance. User should call the "INSTANCE" static variable to obtain the instance of this class.
	 * User will make sure all the printed figures are visible on the container then call <code>removeIcons()</code> passing the Container; 
	 * then make any other operations like printing.
	 * <p>
	 * In the end user must call <code>restoreIcons()</code> to make sure all the icons inside the container get back to 
	 * their original place.
	 * 
	 * @author Luiza
	 * @flowerModelElementId _LjpogJHbEeC8Zpq2X3Nlbg
	 */ 
	public class DiagramIconsListener {
		
		public static const INSTANCE:DiagramIconsListener = new DiagramIconsListener();
		
		
		private var icons:Dictionary = new Dictionary();
		
		/**
		 * Clears the icons dictionary.
		 */ 	
		private function clearDictionary():void {
			for (var key:Object in icons) {
				delete icons[key];
			}
		}
		
		/**
		 * Retrieves the list of icons mapped at the given key (this is actually the parent Object of the icons).
		 * If there is no element mapped at the given key then maps a new empty collection at <code>key</code>.
		 */ 
		private function getIconsAtKey(key:Object): ArrayCollection {
			if (icons[key] == null) {
				var value:ArrayCollection = new ArrayCollection();
				//trace("Add font key: " + key + " original size: " + originalFontSize);
				icons[key] = value;
				return value;
			}
			return icons[key];
		}
		
		/**
		 * @flowerModelElementId _LjsEwJHbEeC8Zpq2X3Nlbg
		 */
		public function removeIcons(component:UIComponent):void {
			for (var i:int = 0; i < component.numChildren; i++) {
				var child:DisplayObject = component.getChildAt(i);
				
				if (child is Bitmap && component.numChildren == 1) {
					component.visible = false;
					//component.includeInLayout = false;
					getIconsAtKey(component).addItem({image:component, isStyle:false});
						
				} else if (child is Image || child is BitmapContainer) { 
					child.visible = false;
					//UIComponent(child).includeInLayout = false;
					getIconsAtKey(component).addItem({image:child, isStyle:false});
					
				} else if (child is UIComponent) {
					// avem nevoie sa scoatem si iconitele ca stil 
					// gen cele de pe butoane?
					// in cazul FD butoanele sunt de regula ascunse si apar numai 
					// la selectie iar diagrama printata nu are elemente selectata
					// in cazul FDG insa daca s-ar aplica, figura poate sa contina orice
					if (UIComponent(child).getStyle("icon") != null) {
						var icon:Object = UIComponent(child).getStyle("icon");
						getIconsAtKey(child).addItem({image:icon, isStyle:true});
						UIComponent(child).setStyle("icon", null);
					}
					removeIcons(UIComponent(child));
				}
			}
		}
		
		/**
		 * @flowerModelElementId _LjsEw5HbEeC8Zpq2X3Nlbg
		 */
		public function restoreIcons():void {
			for (var key:Object in icons) {
				var value:ArrayCollection = icons[key];
											
				// put back the images;	
		 		for (var i:int = 0; i < value.length; i++) {
		 			var iconPack:Object = value.getItemAt(i);
		 			if (iconPack.isStyle) {
		 				UIComponent(key).setStyle("icon", iconPack.image);
		 			} else {
		 				UIComponent(iconPack.image).visible = true;
		 				
		 			}
		 		}
		 		delete icons[key];
			}
		}
		
	}
}