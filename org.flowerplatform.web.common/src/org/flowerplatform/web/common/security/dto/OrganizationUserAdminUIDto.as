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
	import org.flowerplatform.web.common.entity.dto.Dto;
	
	
	[RemoteClass(alias="org.flowerplatform.web.security.dto.OrganizationUserAdminUIDto")]
	public class OrganizationUserAdminUIDto	extends Dto {
		public var organization:OrganizationAdminUIDto;
		public var status:String;
		
		public static const ADMIN:String = "ADMIN";
		public static const MEMBER:String = "MEMBER";
		public static const PENDING_MEMBERSHIP_APPROVAL = "PENDING_MEMBERSHIP_APPROVAL";
	}
}