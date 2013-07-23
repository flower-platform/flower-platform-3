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
package org.flowerplatform.web.common.security.dto
{
	import org.flowerplatform.web.common.entity.dto.NamedDto;

	/**
	 * @see Corresponding Java class.
	 * 
	 * @author Cristi
	 * @author Cristina
	 * @flowerModelElementId _Q9C3UF34EeGwLIVyv_iqEg
	 */ 
	[RemoteClass(alias="org.flowerplatform.web.security.dto.GroupAdminUIDto")]
	public class GroupAdminUIDto extends NamedDto {
		
		/**
		 * @flowerModelElementId _Q9DeZF34EeGwLIVyv_iqEg
		 */
		public var organization:NamedDto;
		
		/**
		 * This is needed because the organization is only NamedDto, so we can't access its label.
		 */ 
		public var organizationLabel:String;
		
	}
}