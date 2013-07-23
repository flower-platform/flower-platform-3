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
package org.flowerplatform.web.git.remote.dto;

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