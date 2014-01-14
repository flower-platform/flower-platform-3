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
package org.flowerplatform.codesync.regex.remote {
	
	import org.flowerplatform.codesync.CodeSyncPlugin;
	import org.flowerplatform.properties.remote.SelectedItem;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass]
	public class RegexSelectedItem extends SelectedItem {
		
		public var match:RegexMatchDto;
	
		public var regex:MacroRegexDto;
		
		public var config:String;
		
		public function RegexSelectedItem(itemType:String, match:RegexMatchDto = null, regex:MacroRegexDto = null) {
			this.itemType = itemType;
			this.match = match;
			this.regex = regex;
			this.config = CodeSyncPlugin.getInstance().regexUtils.selectedConfig;
		}
		
	}
}