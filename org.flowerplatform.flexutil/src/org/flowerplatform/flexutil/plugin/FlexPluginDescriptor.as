package org.flowerplatform.flexutil.plugin {
	import flash.display.Loader;
	import flash.events.Event;
	import flash.net.URLLoader;
	
	/**
	 * @author Cristi
	 */
	public class FlexPluginDescriptor {
		public var url:String;
		public var urlLoader:URLLoader;
		public var downloadFinished:Boolean;
		public var loader:Loader;
		public var errorObject:Object;
		public var flexPlugin:AbstractFlexPlugin;
	}
}