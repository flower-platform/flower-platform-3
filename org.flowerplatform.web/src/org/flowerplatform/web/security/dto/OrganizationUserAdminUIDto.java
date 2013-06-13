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
