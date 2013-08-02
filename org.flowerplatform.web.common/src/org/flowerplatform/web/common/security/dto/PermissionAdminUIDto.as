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
	 * 
	 */ 
	[RemoteClass(alias="org.flowerplatform.web.security.dto.PermissionAdminUIDto")]
	public class PermissionAdminUIDto extends NamedDto {
	
		/**
		 * 
		 */
		public var type:String;
		
		/**
		 * 
		 */
		public var actions:String;
		
		/**
		 * 
		 */
		public var assignedTo:String;

		public var isEditable:Boolean;
		
		public function get simpleType():String { 
			return type.substring(type.lastIndexOf(".") + 1, type.length);
		}
	}
}