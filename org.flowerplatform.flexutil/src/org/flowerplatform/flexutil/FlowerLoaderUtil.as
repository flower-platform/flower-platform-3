package org.flowerplatform.flexutil {
	
	import flash.display.LoaderInfo;
	
	import mx.utils.LoaderUtil;
	
	/**
	 * @author Mariana
	 */
	public class FlowerLoaderUtil {
		
		public static var _rootUrl:String = "";
		
		public static function createAbsoluteURL(url:String):String {
			if (_rootUrl.length > 0) {
				return LoaderUtil.createAbsoluteURL(_rootUrl, url);
			}
			return url;
		}
		
		public static function computeRootUrl(info:LoaderInfo):void {
			_rootUrl = LoaderUtil.normalizeURL(info);
		}
		
	}
}