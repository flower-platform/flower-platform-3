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

import java.util.ArrayList;
import java.util.Collection;

import org.flowerplatform.web.security.service.GroupService;

import org.flowerplatform.web.entity.OrganizationMembershipStatus;
import org.flowerplatform.web.entity.User;
import org.flowerplatform.web.entity.dto.NamedDto;

/**
 * @see User
 * 
 * @author Cristi
 * @author Cristina
 * @author Mariana
 * 
 * 
 */
public class UserAdminUIDto extends NamedDto {

	/**
	 * 
	 */
	private String login;
	
	/**
	 * 
	 */
	private String password;
	
	/**
	 * 
	 */
	private String email;
	
	private boolean isActivated;
	
	/**
	 * 
	 */
	private Collection<GroupAdminUIDto> groups;
	
	/**
	 * 
	 */
	private Collection<OrganizationUserAdminUIDto> organizationUsers;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public boolean getIsActivated() {
		return isActivated;
	}
	
	public void setIsActivated(boolean isActivated) {
		this.isActivated = isActivated;
	}

	/**
	 * Does not always return Collection<GroupAdminUIDto>. Sometimes returns NamedDto.
	 * 
	 * @see GroupService#findAllAsAdminUIDto()
	 * @see GroupService#findAllAsNamedDto()
	 * 
	 * @return
	 */
	public Collection<? extends NamedDto> getGroups() {
		return groups;
	}

	public void setGroups(Collection<GroupAdminUIDto> groups) {
		this.groups = groups;
	}

	public Collection<OrganizationUserAdminUIDto> getOrganizationUsers() {
		return organizationUsers;
	}

	public void setOrganizationUsers(Collection<OrganizationUserAdminUIDto> organizationUsers) {
		this.organizationUsers = organizationUsers;
	}
	
	/**
	 * Iterates the {@link #organizationUsers} list and returns the organizations where the user
	 * has ADMIN or MEMBER status, and if <code>includePendingStatus</code>, where the user has
	 * requested membership.
 	 *
	 * @author Mariana
	 */
	public Collection<OrganizationAdminUIDto> getOrganizations(boolean includePendingStatus) {
		Collection<OrganizationAdminUIDto> orgs = new ArrayList<OrganizationAdminUIDto>();
		if (organizationUsers != null) {
			for (OrganizationUserAdminUIDto ou : organizationUsers) {
				if (includePendingStatus || !ou.getStatus().equals(OrganizationMembershipStatus.PENDING_MEMBERSHIP_APPROVAL)) {
					orgs.add(ou.getOrganization());
				}
			}
		}
		return orgs;
	}

	public UserAdminUIDto() {
		super();		
	}

}