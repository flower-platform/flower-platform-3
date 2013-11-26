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
package org.flowerplatform.codesync.regex.ide {
	import flash.events.Event;
	
	import mx.collections.ArrayCollection;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public class RegexDataEvent extends Event {
		
		public static const SELECTED_CONFIG_CHANGED:String = "selected_config_changed";
		
		public static const CONFIGS_REQUEST_REFRESH:String = "configs_request_refresh";
		public static const MACROS_REQUEST_REFRESH:String = "macros_request_refresh";
		public static const PARSERS_REQUEST_REFRESH:String = "parsers_request_refresh";
		
		public static const REGEX_ACTIONS_CHANGED:String = "regex_actions_changed";
		
		public static const REGEX_MATCHES_CHANGED:String = "regex_matches_changed";
		
		public static const REGEX_MATCHES_SELECTED_CHANGED:String = "regex_matches_selected_changed";
		
		public static const REGEX_ACTIONS_SELECTED_CHANGED:String = "regex_actions_selected_changed";
		
		public var newData:ArrayCollection;
				
		public var newSelectedMatches:Array;
		
		public function RegexDataEvent(type:String) {
			super(type, false, false);			
		}
		
	}
}