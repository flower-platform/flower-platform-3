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
package org.flowerplatform.codesync.regex.ide.remote {
	
	import org.flowerplatform.properties.remote.SelectedItem;
	
	/**
	 * @author Cristina Constantinescu
	 */
	[RemoteClass]
	public class RegexMatchSelectedItem extends SelectedItem {
		
		public var match:RegexMatchDto;
		
		public function RegexMatchSelectedItem(match:RegexMatchDto) {
			this.match = match;	
			this.itemType = "regex_match";
		}	
		
	}
}