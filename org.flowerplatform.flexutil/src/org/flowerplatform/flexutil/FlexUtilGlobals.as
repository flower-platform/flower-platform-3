package org.flowerplatform.flexutil {
	import mx.utils.LoaderUtil;
	
	import org.flowerplatform.flexutil.context_menu.ContextMenuManager;
	import org.flowerplatform.flexutil.layout.ComposedViewProvider;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.popup.IMessageBoxFactory;
	import org.flowerplatform.flexutil.popup.IPopupHandlerFactory;

	public class FlexUtilGlobals {

		protected static var INSTANCE:FlexUtilGlobals = new FlexUtilGlobals();
		
		public static function getInstance():FlexUtilGlobals {
			return INSTANCE;
		}
		
		public var workbench:IWorkbench;
		
		public var composedViewProvider:ComposedViewProvider = new ComposedViewProvider();
		
		public var messageBoxFactory:IMessageBoxFactory;
		
		public var popupHandlerFactory:IPopupHandlerFactory;
		
		public var isMobile:Boolean;
		
		public var contextMenuManager:ContextMenuManager;
		
		/**
		 * The normal app, shouldn't do anything.
		 * The embeded app should initialize using LoaderUtil.normalizeURL(info);
		 * The mobile app should provide the explicit URL.
		 */
		public var rootUrl:String = "";
		
		public function createAbsoluteUrl(url:String):String {
			if (rootUrl.length > 0) {
				return LoaderUtil.createAbsoluteURL(rootUrl, url);
			}
			return url;
		}
		
		public function adjustImageBeforeDisplaying(image:Object):Object {
			if (!(image is String)) {
				// probably an embeded image
				return image;
			} else {
				// an URL
				return createAbsoluteUrl(String(image));
			}
		}

	}
}