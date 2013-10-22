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
package org.flowerplatform.properties.remote {
	[Bindable]
	[RemoteClass]
	[SecureSWF(rename="off")]
	/**
	 * @author Razvan Tache
	 */
	public class Property {
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var name:String;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var value:Object;
		
		/**
		 * 
		 */
		[SecureSWF(rename="off")]
		public var readOnly:Boolean;
		
		public function Property() {
			
		}
	}
}