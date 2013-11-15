package org.flowerplatform.model_access_dao;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.registry.DiscussableDesign;
import org.flowerplatform.model_access_dao.registry.Repository;


public interface RegistryDAO {
	
	public static final String FLOWER_PLATFORM_DATA = "flower-platform-data";
	public static final String CONFIG = "config";
	
	public static final String DISCUSSABLE_DESIGNS = "discussable-designs";
	public static final String DISCUSSABLE_DESIGN_CONFIG = "discussable-design-config";
	
	public static final String FLOWER_PLATFORM_DIAGRAMS = "flower-platform-diagrams";
	
	public static final String MAPPING_LOCATION = "mapping.notation";
	public static final String GLOBAL_MAPPING_LOCATION = FLOWER_PLATFORM_DATA + "/mapping.notation";

	////////////////////////
	// Repositories
	////////////////////////
	
	String createRepository(String path);
	
	Repository getRepository(String id);
	List<Repository> getRepositories();
	
	////////////////////////
	// Discussable Designs
	////////////////////////
	
	String createDiscussableDesign(String path, String repoId);
	
	DiscussableDesign getDiscussableDesign(String repoId, String id);
	List<DiscussableDesign> getDiscussableDesigns(String repoId);
	
	void deleteDiscussableDesign(String repoId, String id, boolean moveDiagrams);
	
	////////////////////////
	// Resources
	////////////////////////
	
	String createResource(String path, String repoId, String discussableDesignId, String id);
	
	URI getResource(String repoId, String discussableDesignId, String id);
	
	Resource loadResource(String repoId, String discussableDesignId, String id);
	void saveResource(String repoId, String discussableDesignId, String id);
	String moveResource(String sourceRepoId, String sourceDiscussableDesignId, String sourceId,
			String targetPath, String targetRepoId, String targetDiscussableDesignId, String targetId);
	
	void deleteResource(String repoId, String discussableDesignId, String id);
}
