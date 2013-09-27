/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.common.link {
	import flash.external.ExternalInterface;
	
	import mx.core.FlexGlobals;
	import mx.managers.BrowserManager;
	import mx.utils.URLUtil;
	
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;

	/**
	 * @author Sorin
	 * @author Cristina Constatinescu
	 */
	public class LinkProvider {
		
		public static const ORGANIZATION:String = "organization";
		public static const OPEN_RESOURCES:String = "openResources";
		public static const SELECT_RESOURCE_AT_INDEX:String = "selectResourceAtIndex";
		public static const ACTIVATION_CODE:String = "activationCode";
		public static const LOGIN:String = "login";
		public static const SHOW_DEBUG_MENU:String = "showDebugMenu";
		
		public const parameters:Object = new Object();
		
		public function LinkProvider() {
			if (FlexGlobals.topLevelApplication) {
				for (var key:String in FlexGlobals.topLevelApplication.parameters)
					parameters[key] = FlexGlobals.topLevelApplication.parameters[key];
			}
		}
		
		public function getOrganization():String {
			return parameters[ORGANIZATION];
		}		
		
		/**
		 * @author Mariana
		 */ 
		public function getActivationCode():String {
			return parameters[ACTIVATION_CODE];
		}
		
		/**
		 * @author Mariana
		 */ 
		public function getLogin():String {
			return parameters[LOGIN];
		}
		
		public function getShowDebugMenu():String {
			return parameters[SHOW_DEBUG_MENU];
		}
		
		/**
		 * 
		 */
		public function getOpenResources():String {
			return parameters[OPEN_RESOURCES];
		}
		
		/**
		 * 
		 */
		public function clearOpenResources():void {
			delete parameters[OPEN_RESOURCES];
		}
		
		/**
		 * 
		 */
		public function getSelectResourceAtIndex():int {
			return parameters[SELECT_RESOURCE_AT_INDEX];
		}
		
		/**
		 * 
		 */
		public function clearSelectResourceAtIndex():void {
			delete parameters[SELECT_RESOURCE_AT_INDEX];
		}
		
		private function printParameters():void {
			for (var key:String in parameters) 
				trace("[" + key + "] = [" + parameters[key] + "]");
		}
		
		////////////////////////////////////////
		//   URL & Browser Utilities
		////////////////////////////////////////
		
		/**
		 * @author Mariana
		 */
		public function getBrowserURL():String {
			if (ExternalInterface.available) {
				return String(ExternalInterface.call("getURL"));
			}
			return "";
		}
		
		public function getBrowserURLWithoutQuery():String {
			var browserURL:String = getBrowserURL();
			return browserURL.split("?")[0];
		}
		
		public function getBrowserURLQuery():String {
			var browserURL:String = getBrowserURL();
			if (browserURL.indexOf("?") < 0) // no parameters passed in the browser
				return null;
			return browserURL.substr(browserURL.indexOf("?") + 1);
		}
		
		/**
		 * Given an url, if it has query parameters it will place them in the supplied <code>parameters</code> object
		 * or it will return them
		 * @param url the url to parse it's parameters
		 * @param parameters the destination object where the parsed parameters are put.
		 * 		If null then the result must be obtaind from the returned value.
		 */ 
		public function parseURLQueryParameters(url:String, parameters:Object = null):Object {
			if (parameters == null)
				parameters = new Object();
			if (url.indexOf("?") < 0) // no parameters passed in the url
				return parameters;
			
			var query:String = url.substr(url.indexOf("?") + 1); 
			for each (var parameterWithValue:String in query.split("&")) { // spliting by group separator p1=v1&p2=v2
				var parameter:String = null;
				var value:String = null;
				
				var indexOfEqualSign:int = parameterWithValue.indexOf("=");
				if (indexOfEqualSign < 0) { // No value, just key
					parameter = parameterWithValue;
					value = null;
				} else {
					parameter = parameterWithValue.substring(0, indexOfEqualSign);
					value = parameterWithValue.substring(indexOfEqualSign + 1); // the rest represents the value, even though it contains an = character 
				}
				parameters[parameter] = value;
			}
			return parameters;
		}
	}
}