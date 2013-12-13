package org.flowerplatform.model_access_dao2.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

public class Design {

	private UUID designId;
	
	private UUID repoId;
	
	private String path;
	
	private Map<UUID, String> resources = new HashMap<UUID, String>();
	
	public Design(String path, UUID repoId, UUID designId) {
		this.path = path;
		this.repoId = repoId;
		this.designId = designId;
	}

	public UUID getDesignId() {
		return designId;
	}

	public void setDesignId(UUID designId) {
		this.designId = designId;
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
	
	public Map<UUID, String> getResources() {
		return resources;
	}

	public void setResources(Map<UUID, String> resources) {
		this.resources = resources;
	}
	
	public UUID getResourceUUIDForPath(String path) {
		if (resources.containsValue(path)) {
			for (Entry<UUID, String> entry : resources.entrySet()) {
				if (entry.getValue().equals(path)) {
					return entry.getKey();
				}
			}
		}
		return null;
	}

}
