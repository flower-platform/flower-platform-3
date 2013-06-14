package org.flowerplatform.editor {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class EditorPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorPlugin;
		
		public static function getInstance():EditorPlugin {
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