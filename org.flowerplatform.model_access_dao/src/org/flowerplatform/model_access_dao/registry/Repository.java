package org.flowerplatform.model_access_dao.registry;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Repository extends DirWithResources {

	private Map<String, DiscussableDesign> discussableDesigns = new HashMap<String, DiscussableDesign>();

	public Repository(String id, File dir) {
		super(id, dir);
	}
	
	public Map<String, DiscussableDesign> getDiscussableDesigns() {
		return discussableDesigns;
	}

	public void setDiscussableDesigns(
			Map<String, DiscussableDesign> discussableDesigns) {
		this.discussableDesigns = discussableDesigns;
	}
	
}
