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
	import flash.events.EventDispatcher;
	
	import mx.core.FlexGlobals;
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.common.CommonPlugin;
	import org.flowerplatform.flexutil.FlexUtilGlobals;
	
	/**
	 * @author Cristina Constantinescu
	 */	
	public class RegexUtils extends EventDispatcher {
		
		public static const REGEX_CONFIG_TYPE:String = "regex_config";	
		public static const REGEX_PARSER_TYPE:String = "regex_parser";	
		public static const REGEX_MACRO_TYPE:String = "regex_macro";	
		public static const REGEX_MATCH_TYPE:String = "regex_match";	
		
		private var _selectedConfig:String;
				
		private var _selectedConfigMessage:String;
		
		[Bindable]
		public function get selectedConfig():String	{
			return _selectedConfig;
		}

		public function set selectedConfig(value:String):void {
			_selectedConfig = value;
			if (selectedConfig != null) {
				selectedConfigMessage = CodeSyncPlugin.getInstance().getMessage('regex.selectedConfig', [selectedConfig]);
			} else {
				selectedConfigMessage = CodeSyncPlugin.getInstance().getMessage('regex.selectedConfig.none');
			}
			
			FlexGlobals.topLevelApplication.dispatchEvent(new RegexEvent(RegexEvent.SELECTED_CONFIG_CHANGED));
		}
		
		[Bindable(event="selectionConfigMessageChanged")]
		public function get selectedConfigMessage():String {
			return _selectedConfigMessage;
		}

		public function set selectedConfigMessage(value:String):void {
			_selectedConfigMessage = value;			
			
			dispatchEvent(new Event("selectionConfigMessageChanged"));
		}
		
		public function isSelectedConfigValid():Boolean {
			if (selectedConfig == null) {
				FlexUtilGlobals.getInstance().messageBoxFactory.createMessageBox()
					.setText(CodeSyncPlugin.getInstance().getMessage('regex.config.noConfigSelected.info'))
					.setTitle(CommonPlugin.getInstance().getMessage('info'))
					.setWidth(300)
					.setHeight(150)
					.showMessageBox();
				return false;		
			}
			return true;
		}
	}
}