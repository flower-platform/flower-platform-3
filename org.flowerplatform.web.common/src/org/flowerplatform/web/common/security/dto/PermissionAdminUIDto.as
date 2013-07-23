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
	import org.flowerplatform.web.common.entity.dto.NamedDto;
	
	
	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _Q9GhsF34EeGwLIVyv_iqEg
	 */ 
	[RemoteClass(alias="org.flowerplatform.web.security.dto.PermissionAdminUIDto")]
	public class PermissionAdminUIDto extends NamedDto {
	
		/**
		 * @flowerModelElementId _Q9GhtV34EeGwLIVyv_iqEg
		 */
		public var type:String;
		
		/**
		 * @flowerModelElementId _Q9HIwV34EeGwLIVyv_iqEg
		 */
		public var actions:String;
		
		/**
		 * @flowerModelElementId _Q9HIwl34EeGwLIVyv_iqEg
		 */
		public var assignedTo:String;

		public var isEditable:Boolean;
		
		public function get simpleType():String { 
			return type.substring(type.lastIndexOf(".") + 1, type.length);
		}
	}
}