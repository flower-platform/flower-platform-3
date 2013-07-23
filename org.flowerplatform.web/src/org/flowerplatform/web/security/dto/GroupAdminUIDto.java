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


import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.dto.NamedDto;

/**
 * @see Group
 * 
 * @author Cristi
 * @author Cristina
 * @flowerModelElementId _bWxJ8FcCEeGL3vi-zPhopA
 */
public class GroupAdminUIDto extends NamedDto {
	
	/**
	 * @flowerModelElementId _xwN4MFcCEeGL3vi-zPhopA
	 */
	private NamedDto organization;

	private String organizationLabel;
	
	public NamedDto getOrganization() {
		return organization;
	}

	public void setOrganization(NamedDto organization) {
		this.organization = organization;
	}
	
	public String getOrganizationLabel() {
		return organizationLabel;
	}

	public void setOrganizationLabel(String organizationLabel) {
		this.organizationLabel = organizationLabel;
	}

	public GroupAdminUIDto() {
		super();	
	}

	public GroupAdminUIDto(long id, String name, NamedDto organization) {
		super(id, name);
		this.organization = organization;
	}

}