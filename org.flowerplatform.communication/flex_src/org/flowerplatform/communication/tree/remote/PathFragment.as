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
package org.flowerplatform.communication.tree.remote {
	
	/**
	 * @see Corresponding Java doc.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * 
	 * @flowerModelElementId _cLb0sKP8EeGeHqktJlHXmA
	 */
	[RemoteClass]
	[Bindable]
	[SecureSWF(rename="off")]
	public class PathFragment {
		
		/**
		 * @flowerModelElementId _cLcbw6P8EeGeHqktJlHXmA
		 */
		[SecureSWF(rename="off")]
		public var name:String;
		
		/**
		 * @flowerModelElementId _cLdC0KP8EeGeHqktJlHXmA
		 */
		[SecureSWF(rename="off")]
		public var type:String;
		
		public function PathFragment(name:String = null, type:String = null) {
			this.name = name;
			this.type = type;
		}
	}
	
}