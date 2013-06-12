package org.flowerplatform.flexutil {
	import mx.resources.ResourceManager;
	
	[ResourceBundle("org_flowerplatform_flexutil")]
	/**
	 * @author Cristi
	 */
	public class FlexUtilAssets {
		
		[Bindable]
		public static var INSTANCE:FlexUtilAssets = new FlexUtilAssets(); 
		
		/**
		 * Retrieves a message from the properties files. Parameters can be passed
		 * and the {?} place holders will be replaced with them.
		 * 
		 * Copied from MP.
		 */
		public function getMessage(messageId:String, params:Array=null):String {				
			return ResourceManager.getInstance().getString("org_flowerplatform_flexutil", messageId, params);
		}
		
	}
}