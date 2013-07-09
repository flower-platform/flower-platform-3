package org.flowerplatform.web.git.remote.dto;

/**
 *	@author Cristina Constantinescu
 */
public class GitActionDto {

	private String repository;

	private String branch;
	
	public String getRepository() {
		return repository;
	}

	public void setRepository(String repository) {
		this.repository = repository;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}	
		
}
