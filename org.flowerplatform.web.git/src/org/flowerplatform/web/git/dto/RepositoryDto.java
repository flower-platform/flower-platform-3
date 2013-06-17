package org.flowerplatform.web.git.dto;

/**
 *	@author Cristina Constantinescu
 */
public class RepositoryDto {

	private String label;
	
	private String location;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public RepositoryDto() {	
	}

	public RepositoryDto(String label, String location) {	
		this.label = label;
		this.location = location;
	}
		
}
