package org.flowerplatform.web.git {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.git.layout.GitPerspective;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GitPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:GitPlugin;
			
		public static function getInstance():GitPlugin {
			return INSTANCE;
		}
		
		protected var gitCommonPlugin:GitCommonPlugin = new GitCommonPlugin();
		
		override public function start():void {
			super.start();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;	
			
			gitCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			gitCommonPlugin.start();
			
			WebPlugin.getInstance().perspectives.push(new GitPerspective());
		}
		
		override public function setupExtensionPointsAndExtensions():void {
			super.setupExtensionPointsAndExtensions();
			gitCommonPlugin.setupExtensionPointsAndExtensions();		
		}
		
		override protected function registerMessageBundle():void {
			// do nothing; this plugin doesn't have a .resources (yet)
		}
	}
}