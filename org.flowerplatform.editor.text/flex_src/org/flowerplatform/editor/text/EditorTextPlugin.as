package org.flowerplatform.editor.text {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class EditorTextPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:EditorTextPlugin;
		
		public static function getInstance():EditorTextPlugin {
			return INSTANCE;
		}
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
		
	}
}