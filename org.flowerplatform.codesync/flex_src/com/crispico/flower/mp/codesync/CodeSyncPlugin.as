package com.crispico.flower.mp.codesync {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class CodeSyncPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CodeSyncPlugin;
		
		public static function getInstance():CodeSyncPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
		}
		
	}
}