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