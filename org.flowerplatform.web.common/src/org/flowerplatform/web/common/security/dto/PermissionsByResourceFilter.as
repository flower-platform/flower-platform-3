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
package org.flowerplatform.web.common.security.dto {
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _Q9I98F34EeGwLIVyv_iqEg
	 */ 
	[RemoteClass(alias="org.flowerplatform.web.security.dto.PermissionsByResourceFilter")]
	public class PermissionsByResourceFilter {
		
		
		/**
		 * @flowerModelElementId _Q9I99F34EeGwLIVyv_iqEg
		 */
		public var resource:String;
		
		/**
		 * @author Mariana
		 * 
		 * @flowerModelElementId _Q9I99V34EeGwLIVyv_iqEg
		 */
		public function PermissionsByResourceFilter(resource:String) {
			this.resource = resource;
		}
	}
	
}