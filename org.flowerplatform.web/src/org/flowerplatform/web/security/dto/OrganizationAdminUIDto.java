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


import java.util.List;

import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.OrganizationMembershipStatus;
import org.flowerplatform.web.entity.dto.NamedDto;

/**
 * @see Organization
 * 
 * @author Cristi
 * @author Cristina
 * 
 */
public class OrganizationAdminUIDto extends NamedDto {
	
	private String label;
	
	private String URL;
	
	private boolean activated;
	
	private String logoURL;
	
	private String iconURL;
	
	private int projectsCount;

	private int filesCount;

	private int modelsCount;

	private int diagramsCount;
	
	/**
	 * Not persisted in BD; computed from FavoriteItem.
	 * 
	 * @author Mariana
	 */
	private boolean pinned[] = new boolean[3];
	
	/**
	 * Status of current member in organization.
	 */
	private OrganizationMembershipStatus status;
	
	/**
	 * 
	 */
	private List<NamedDto> groups;

	// !!! getter&setter have upper case SVNRepo...
	private List<NamedDto> svnRepositoryURLs;
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}
	
	public String getLogoURL() {
		return logoURL;
	}

	public void setLogoURL(String logoURL) {
		this.logoURL = logoURL;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public OrganizationMembershipStatus getStatus() {
		return status;
	}

	public void setStatus(OrganizationMembershipStatus status) {
		this.status = status;
	}

	public List<NamedDto> getGroups() {
		return groups;
	}

	public int getProjectsCount() {
		return projectsCount;
	}

	public void setProjectsCount(int projectsCount) {
		this.projectsCount = projectsCount;
	}

	public int getFilesCount() {
		return filesCount;
	}

	public void setFilesCount(int filesCount) {
		this.filesCount = filesCount;
	}

	public int getModelsCount() {
		return modelsCount;
	}

	public void setModelsCount(int modelsCount) {
		this.modelsCount = modelsCount;
	}

	public int getDiagramsCount() {
		return diagramsCount;
	}

	public void setDiagramsCount(int diagramsCount) {
		this.diagramsCount = diagramsCount;
	}

	public void setGroups(List<NamedDto> groups) {
		this.groups = groups;
	}
		
	public List<NamedDto> getSVNRepositoryURLs() {
		return svnRepositoryURLs;
	}

	public void setSVNRepositoryURLs(List<NamedDto> svnRepositoryURLs) {
		this.svnRepositoryURLs = svnRepositoryURLs;
	}

	public OrganizationAdminUIDto() {		
	}

	public boolean[] getPinned() {
		return pinned;
	}

	public void setPinned(boolean pinned[]) {
		this.pinned = pinned;
	}
}