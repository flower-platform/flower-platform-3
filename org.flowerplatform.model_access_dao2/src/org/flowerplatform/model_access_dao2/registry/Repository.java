package org.flowerplatform.model_access_dao2.registry;

import java.util.UUID;

public class Repository {

	private UUID repoId;
	
	private String path;
	
	private Design defaultDesign;
	
	public Repository(String path, UUID repoId) {
		this.path =  path;
		this.repoId = repoId;
	}

	public UUID getRepoId() {
		return repoId;
	}

	public void setRepoId(UUID repoId) {
		this.repoId = repoId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Design getDefaultDesign() {
		return defaultDesign;
	}

	public void setDefaultDesign(Design defaultDesign) {
		this.defaultDesign = defaultDesign;
	}

}
