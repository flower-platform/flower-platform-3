package org.flowerplatform.model_access_dao.registry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;

public class Repository {

	private String id;
	
	private String masterId;
	
	private File dir;
	
	private Map<String, URI> resources = new HashMap<String, URI>();

	public Repository(String id, File dir) {
		setId(id);
		setDir(dir);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMasterId() {
		return masterId;
	}

	public void setMasterId(String masterId) {
		this.masterId = masterId;
	}

	public File getDir() {
		return dir;
	}

	public void setDir(File dir) {
		this.dir = dir;
	}

	public Map<String, URI> getResources() {
		return resources;
	}

	public void setResources(Map<String, URI> resources) {
		this.resources = resources;
	}

	@Override
	public String toString() {
		return String.format("%s [dir = %s]", this.getClass().getSimpleName(), dir);
	}
	
}
