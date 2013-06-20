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
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
		}
		
	}
}