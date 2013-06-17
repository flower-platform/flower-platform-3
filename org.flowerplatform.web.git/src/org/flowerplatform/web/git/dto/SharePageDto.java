package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class SharePageDto extends GitActionDto {

	private String projectPath;
	
	private List<RepositoryDto> repositories;
	
	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}

	public List<RepositoryDto> getRepositories() {
		return repositories;
	}

	public void setRepositories(List<RepositoryDto> repositories) {
		this.repositories = repositories;
	}

}
