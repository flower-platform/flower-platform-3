package org.flowerplatform.web.git.remote.dto;

/**
 *	@author Cristina Constantinescu
 */
public class ViewInfoDto {

	private String repositoryLocation;
	
	private String repositoryName;
	
	private Object selectedObject;
	
	private boolean isResource;
	
	private String statefulClientId;

	public String getRepositoryLocation() {
		return repositoryLocation;
	}

	public void setRepositoryLocation(String repositoryLocation) {
		this.repositoryLocation = repositoryLocation;
	}

	public String getRepositoryName() {
		return repositoryName;
	}

	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}

	public Object getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(Object selectedObject) {
		this.selectedObject = selectedObject;
	}

	public boolean getIsResource() {
		return isResource;
	}
	
	public void setIsResource(boolean isResource) {
		this.isResource = isResource;
	}

	public String getStatefulClientId() {
		return statefulClientId;
	}

	public void setStatefulClientId(String statefulClientId) {
		this.statefulClientId = statefulClientId;
	}
			
	
	
}
