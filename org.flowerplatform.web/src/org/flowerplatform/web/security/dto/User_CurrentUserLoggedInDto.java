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

import org.flowerplatform.web.entity.dto.NamedDto;

/**
 * @author Cristi
 */
public class User_CurrentUserLoggedInDto extends NamedDto {

	private String login;
	
	private String email;
	
	private boolean hasAdminSecurityEntitiesPermissions;
	
	private boolean isAdmin;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isHasAdminSecurityEntitiesPermissions() {
		return hasAdminSecurityEntitiesPermissions;
	}

	public void setHasAdminSecurityEntitiesPermissions(
			boolean hasAdminSecurityEntitiesPermissions) {
		this.hasAdminSecurityEntitiesPermissions = hasAdminSecurityEntitiesPermissions;
	}

	public boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

}