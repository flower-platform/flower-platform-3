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
package org.flowerplatform.codesync.regex {
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RegexEvent extends Event {
		
		public static const SELECTED_CONFIG_CHANGED:String = "selected_config_changed";
		public static const SELECTED_MATCH_CHANGED:String = "selected_match_changed";		
		public static const SELECTED_PARSER_CHANGED:String = "selected_parser_changed";
		
		public static const CONFIGS_REQUEST_REFRESH:String = "configs_request_refresh";
		public static const MACROS_REQUEST_REFRESH:String = "macros_request_refresh";
		public static const PARSERS_REQUEST_REFRESH:String = "parsers_request_refresh";
		
		public static const FILL_PARSERS:String = "fill_parsers";		
				
		public var newData:ArrayCollection;
				
		public var newSelectedMatches:Array;
		
		public function RegexEvent(type:String) {
			super(type, false, false);			
		}
		
	}
}