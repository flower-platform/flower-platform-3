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

package  org.flowerplatform.web.svn.common.history {
	
	import mx.collections.ArrayCollection;
	
	/**  
	 * @author Victor Badila
	 */
	[RemoteClass]
	[SecureSWF(rename="off")]
	public class HistoryEntry {
		
		[SecureSWF(rename="off")]
		public var revision:String;
		
		[SecureSWF(rename="off")]
		public var date:Date;
		
		[SecureSWF(rename="off")]
		public var author:String;
		
		[SecureSWF(rename="off")]
		public var comment:String;
		
		[SecureSWF(rename="off")]
		public var affectedPathEntries:ArrayCollection;		
		
	}
	
}