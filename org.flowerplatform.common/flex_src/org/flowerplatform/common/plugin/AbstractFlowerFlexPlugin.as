package org.flowerplatform.common.plugin {
	import mx.resources.ResourceManager;
	
	import org.flowerplatform.flexutil.plugin.AbstractFlexPlugin;
	import org.flowerplatform.flexutil.plugin.FlexPluginLoadingSession;
	import org.flowerplatform.flexutil.resources.ResourcesUtils;
	
	/**
	 * @author Cristi
	 */
	public class AbstractFlowerFlexPlugin extends AbstractFlexPlugin {
		
		public static const PUBLIC_RESOURCES_DIR:String = "public-resources";
		
		public static const RESOURCES_PLUGIN_SUFFIX:String = ".resources";
		
		public static const MESSAGES_FILE:String = "messages.properties";
		
		protected var resourcesUrl:String;
		
		override public function start():void {
			super.start();
			registerMessageBundle();
			registerClassAliases();
		}
		
		protected function registerMessageBundle():void {
			ResourcesUtils.registerMessageBundle("en_US", getResourceUrl(""), getResourceUrl(MESSAGES_FILE));
		}
		
		protected function registerClassAliases():void {
			
		}
		
		/**
		 * Retrieves a message from the properties files. Parameters can be passed
		 * and the {?} place holders will be replaced with them.
		 */
		public function getMessage(messageId:String, params:Array=null):String {				
			return ResourceManager.getInstance().getString(resourcesUrl, messageId, params);
		}
		
		public function getResourceUrl(resource:String):String {
			if (resourcesUrl == null) {
				var groups:Array = FlexPluginLoadingSession.classNameRegEx.exec(flexPluginDescriptor.url);
				if (groups == null || groups.length != 2) {
					throw new Error("Error getting the bundle name from the url: " + flexPluginDescriptor + "; tried to apply regex: " + FlexPluginLoadingSession.classNameRegEx);
				} 
				var package_:String = groups[1];
				resourcesUrl = "servlet/" + PUBLIC_RESOURCES_DIR + "/" + package_ + RESOURCES_PLUGIN_SUFFIX + "/";
			}
			return resourcesUrl + resource;
		}

	}
}