package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class CleanPageDto {

	private List<CommitResourceDto> resources;

	public List<CommitResourceDto> getResources() {
		return resources;
	}

	public void setResources(List<CommitResourceDto> resources) {
		this.resources = resources;
	}
		
}
