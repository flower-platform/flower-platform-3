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
