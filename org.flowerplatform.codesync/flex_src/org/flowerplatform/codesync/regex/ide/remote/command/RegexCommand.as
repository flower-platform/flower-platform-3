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
package org.flowerplatform.codesync.regex.ide.remote.command {
	
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.codesync.regex.ide.RegexDataEvent;
	import org.flowerplatform.communication.command.AbstractClientCommand;
	import org.flowerplatform.flexutil.FlexUtilGlobals;

	/**
	 * @author Cristina Constantinescu
	 */ 
	[RemoteClass]
	public class RegexCommand extends AbstractClientCommand {
		
		public static const REFRESH_CONFIGS:String = "refreshConfigs";
		public static const REFRESH_MACROS:String = "refreshMacros";
		public static const REFRESH_PARSERS:String = "refreshParsers";
		
		public var operation:String;
		
		override public function execute():void {
			if (operation == REFRESH_CONFIGS) {					
				FlexGlobals.topLevelApplication.dispatchEvent(new RegexDataEvent(RegexDataEvent.CONFIGS_REQUEST_REFRESH));				
			} else if (operation == REFRESH_MACROS) {
				FlexGlobals.topLevelApplication.dispatchEvent(new RegexDataEvent(RegexDataEvent.MACROS_REQUEST_REFRESH));	
			} else if (operation == REFRESH_PARSERS) {
				FlexGlobals.topLevelApplication.dispatchEvent(new RegexDataEvent(RegexDataEvent.PARSERS_REQUEST_REFRESH));	
			}
		}
		
		
	}
}