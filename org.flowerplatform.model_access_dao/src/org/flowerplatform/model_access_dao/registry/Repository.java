package org.flowerplatform.model_access_dao.registry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;

public class Repository {

	public static String MAPPING = "mapping";
	
	public static String APP_WIZARD = "app-wizard";
	
	private UUID id;
	
	private UUID masterId;
	
	private File dir;
	
	private Map<UUID, URI> resources = new HashMap<UUID, URI>();
	
	private Map<String, UUID> resourcesAlias = new HashMap<String, UUID>();

	public Repository(UUID id, File dir) {
		setId(id);
		setDir(dir);
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getMasterId() {
		return masterId;
	}

	public void setMasterId(UUID masterId) {
		this.masterId = masterId;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public Map<UUID, URI> getResources() {
		return resources;
	}

	public void setResources(Map<UUID, URI> resources) {
		this.resources = resources;
	}
	
	public Map<String, UUID> getResourcesAlias() {
		return resourcesAlias;
	}
	
	public void setResourcesAlias(Map<String, UUID> resourcesAlias) {
		this.resourcesAlias = resourcesAlias;
	}

	@Override
	public String toString() {
		return String.format("%s [dir = %s]", this.getClass().getSimpleName(), dir);
	}
	
}
