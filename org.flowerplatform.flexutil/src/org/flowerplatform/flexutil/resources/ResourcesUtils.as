package org.flowerplatform.flexutil.resources {
	
	import flash.events.Event;
	import flash.events.IEventDispatcher;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.events.TextEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	import mx.controls.Alert;
	import mx.resources.IResourceManager;
	import mx.resources.ResourceBundle;
	import mx.resources.ResourceManager;
	
	import org.flowerplatform.flexutil.FlowerLoaderUtil;

	/**
	 * @author Cristi
	 */
	public class ResourcesUtils	{
		
		public static function registerMessageBundle(locale:String, messageBundle:String, url:String, object:Object = null):void {
			var loadedHandler:Function = function(event:Event):void {
				var urlLoader:URLLoader = URLLoader(event.target);
				var source:String = urlLoader.data as String;
				
				var resourceManager:IResourceManager = ResourceManager.getInstance();
				var bundle:ResourceBundle = new ResourceBundle(locale, messageBundle); 
				new PropertiesParser().parseProperties(source, bundle.content);
				resourceManager.addResourceBundle(bundle, false); 
				resourceManager.update();
				
				if (object != null && object is IEventDispatcher) {
					IEventDispatcher(object).dispatchEvent(new ResourceUpdatedEvent());
				}
			}
			
			var errorHandler:Function = function (event:TextEvent):void {
				Alert.show("Error loading message bundle: " + messageBundle + ", url: " + url + ", message: " + event.text);
			}
			
			var urlLoader:URLLoader = new URLLoader(new URLRequest(FlowerLoaderUtil.createAbsoluteURL(url)));
			urlLoader.addEventListener(Event.COMPLETE, loadedHandler);
			urlLoader.addEventListener(IOErrorEvent.IO_ERROR, errorHandler);
			urlLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, errorHandler);
		}
	}
}