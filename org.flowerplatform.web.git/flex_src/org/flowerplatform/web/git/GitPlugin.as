package org.flowerplatform.web.git {
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	import org.flowerplatform.flexutil.Utils;
	import org.flowerplatform.web.WebPlugin;
	import org.flowerplatform.web.git.common.GitCommonPlugin;
	import org.flowerplatform.web.git.history.GitHistoryViewProvider;
	import org.flowerplatform.web.git.layout.GitPerspective;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class GitPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:GitPlugin;
			
		public static function getInstance():GitPlugin {
			return INSTANCE;
		}
		
		protected var gitCommonPlugin:GitCommonPlugin = new GitCommonPlugin();
		
		public var service:GitService = new GitService();
		
		override public function preStart():void {
			super.preStart();
			gitCommonPlugin.preStart();
			if (INSTANCE != null) {
				throw new Error("Plugin " + Utils.getClassNameForObject(this, true) + " has already been started");
			}
			INSTANCE = this;	
						
			FlexUtilGlobals.getInstance().composedViewProvider.addViewProvider(new GitHistoryViewProvider());
			WebPlugin.getInstance().perspectives.push(new GitPerspective());				
		}
		
		override public function start():void {
			super.start();
			gitCommonPlugin.flexPluginDescriptor = flexPluginDescriptor;	
			gitCommonPlugin.start();
		}		
	}
}