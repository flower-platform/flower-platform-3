package org.flowerplatform.common {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class CommonPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CommonPlugin;
		
		public static function getInstance():CommonPlugin {
			return INSTANCE;
		}
		
		public static const VERSION:String = "2.0.0.M2_2013-06-04";
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
	}
}