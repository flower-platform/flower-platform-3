package org.flowerplatform.web.common {
	
	import mx.core.FlexGlobals;
	import mx.core.IVisualElementContainer;
	
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.common.communication.AuthenticationManager;
	import org.flowerplatform.web.common.explorer.ExplorerViewProvider;
	
	/**
	 * @author Cristi
	 */
	public class WebCommonPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:WebCommonPlugin;
		
		public static function getInstance():WebCommonPlugin {
			return INSTANCE;
		}
		
		public var authenticationManager:AuthenticationManager;
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;
			authenticationManager = new AuthenticationManager();
		}
		
		override public function setupExtensionPointsAndExtensions():void {
			super.setupExtensionPointsAndExtensions();
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new ExplorerViewProvider());
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
	}
}