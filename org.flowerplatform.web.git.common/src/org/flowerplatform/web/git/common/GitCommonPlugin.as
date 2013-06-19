package org.flowerplatform.web.git.common {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GitCommonPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:GitCommonPlugin;
						
		public static function getInstance():GitCommonPlugin {
			return INSTANCE;
		}		
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;
		}
		
	}	
}