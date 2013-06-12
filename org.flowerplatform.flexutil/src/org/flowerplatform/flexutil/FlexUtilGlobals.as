package org.flowerplatform.flexutil {
	import org.flowerplatform.flexutil.layout.ComposedViewProvider;
	import org.flowerplatform.flexutil.layout.IWorkbench;
	import org.flowerplatform.flexutil.popup.IMessageBoxFactory;
	import org.flowerplatform.flexutil.popup.IPopupHandlerFactory;

	public class FlexUtilGlobals {

		protected static var INSTANCE:FlexUtilGlobals = new FlexUtilGlobals();
		
		public static function getInstance():FlexUtilGlobals {
			return INSTANCE;
		}
		
		public var messageBoxFactory:IMessageBoxFactory;
		
		public var popupHandlerFactory:IPopupHandlerFactory;
		
		public var composedViewProvider:ComposedViewProvider = new ComposedViewProvider();
		
		public var isMobile:Boolean;
		
	}
}