package org.flowerplatform.web.git.dto;

import java.util.List;

/**
 *	@author Cristina Constantinescu
 */
public class ImportProjectPageDto {

	private List<String> selectedFolders;
	
	private List<ProjectDto> existingProjects;

	public List<ProjectDto> getExistingProjects() {
		return existingProjects;
	}

	public void setExistingProjects(List<ProjectDto> existingProjects) {
		this.existingProjects = existingProjects;
	}
	
	public List<String> getSelectedFolders() {
		return selectedFolders;
	}
	
	public void setSelectedFolders(List<String> selectedFolders) {
		this.selectedFolders = selectedFolders;
	}
}
