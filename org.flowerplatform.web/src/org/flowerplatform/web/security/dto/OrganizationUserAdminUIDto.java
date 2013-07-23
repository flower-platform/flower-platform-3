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
package org.flowerplatform.web.security.dto;

import org.flowerplatform.web.entity.OrganizationMembershipStatus;
import org.flowerplatform.web.entity.dto.Dto;

public class OrganizationUserAdminUIDto extends Dto {
	private OrganizationAdminUIDto organization;
	private OrganizationMembershipStatus status;
	
	public OrganizationAdminUIDto getOrganization() {
		return organization;
	}
	
	public void setOrganization(OrganizationAdminUIDto organization) {
		this.organization = organization;
	}
	
	public OrganizationMembershipStatus getStatus() {
		return status;
	}
	
	public void setStatus(OrganizationMembershipStatus status) {
		this.status = status;
	}
}