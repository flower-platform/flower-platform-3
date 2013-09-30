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
package org.flowerplatform.common {
	import flash.external.ExternalInterface;
	import flash.utils.Dictionary;
	
	import mx.logging.ILogger;
	import mx.logging.Log;
	
	import org.flowerplatform.common.link.ILinkHandler;
	import org.flowerplatform.common.link.LinkProvider;
	import org.flowerplatform.common.log.Logger;
	import org.flowerplatform.common.plugin.AbstractFlowerFlexPlugin;
	import org.flowerplatform.flexutil.Utils;
	
	/**
	 * @author Cristi
	 */
	public class CommonPlugin extends AbstractFlowerFlexPlugin {
		
		protected static var INSTANCE:CommonPlugin;
		
		public static function getInstance():CommonPlugin {
			return INSTANCE;
		}
				
		public static const VERSION:String = "2.0.0.M2_2013-06-04";
		
		/**
		 * @author Cristina Constatinescu
		 */
		public var linkProvider:LinkProvider;		
		public var linkHandlers:Dictionary;
		
		override public function preStart():void {
			super.preStart();
			if (INSTANCE != null) {
				throw new Error("An instance of plugin " + Utils.getClassNameForObject(this, true) + " already exists; it should be a singleton!");
			}
			INSTANCE = this;
			
			linkHandlers = new Dictionary();
			linkProvider = new LinkProvider();
			
			ExternalInterface.addCallback("handleLink", handleLink);
		}
		
		/**
		 * @author Cristina Constatinescu
		 */
		public function handleLink(url:String = null, defaultCommand:String = null):void {
			if (url == null) {
				url = linkProvider.getBrowserURL();
			}
			var commands:Object = linkProvider.parseURLQueryParameters(url); // map command -> parameters
			if (commands.length == 1 && url.indexOf("?") < 0 && defaultCommand) { 
				// one command, url doesn't contain command name -> command can be passed in defaultCommand
				if (linkHandlers[defaultCommand] != null) {	
					ILinkHandler(linkHandlers[defaultCommand]).handleLink(defaultCommand, commands[object]);					
				}
			} else {
				for (var object:String in commands) {				
					if (linkHandlers[object] != null) {	
						ILinkHandler(linkHandlers[object]).handleLink(object, commands[object]);					
					}
				}
			}
		}
	}
}