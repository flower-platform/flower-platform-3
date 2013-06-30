package org.flowerplatform.common.plugin {
	import mx.resources.ResourceManager;
	
	import org.flowerplatform.flexutil.plugin.AbstractFlexPlugin;
	import org.flowerplatform.flexutil.plugin.FlexPluginLoadingSession;
	import org.flowerplatform.flexutil.resources.ResourcesUtils;
	
	/**
	 * @author Cristi
	 */
	public class AbstractFlowerFlexPlugin extends AbstractFlexPlugin {
		
		public static const MESSAGES_FILE:String = "messages.properties";
		
		protected var _resourcesUrl:String;

		public function get resourcesUrl():String {
			if (_resourcesUrl == null) {
				const regex:RegExp = new RegExp("(.*?/)swc/");
				var groups:Array = regex.exec(flexPluginDescriptor.url);
				if (groups == null || groups.length != 2) {
					throw new Error("Error getting the bundle name from the url: " + flexPluginDescriptor.url + "; tried to apply regex: " + regex);
				} 
				var package_:String = groups[1];
				_resourcesUrl = groups[1];
			}
			return _resourcesUrl;
		}

		override public function start():void {
			super.start();
			registerMessageBundle();
			registerClassAliases();
		}
		
		protected function registerMessageBundle():void {
			ResourcesUtils.registerMessageBundle("en_US", resourcesUrl, getResourceUrl(MESSAGES_FILE));
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
			return resourcesUrl + resource;
		}

	}
}