package org.flowerplatform.model_access_dao;

import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao.registry.Repository;

public interface RegistryDAO {
	
	public static final String FLOWER_PLATFORM_DATA = "flower-platform-data";
	public static final String CONFIG = "config";
	
	public static final String DISCUSSABLE_DESIGNS = "discussable-designs";
	
	public static final String FLOWER_PLATFORM_DIAGRAMS = "flower-platform-diagrams";
	
	public static final String MAPPING_LOCATION = "mapping.notation";
	public static final String GLOBAL_MAPPING_LOCATION = FLOWER_PLATFORM_DATA + "/" + MAPPING_LOCATION;
	public static final String APP_WIZARD_LOCATION = "app-wizard.notation";
	public static final String GLOBAL_APP_WIZARD_LOCATION = FLOWER_PLATFORM_DATA + "/" + APP_WIZARD_LOCATION;

	////////////////////////
	// Repositories
	////////////////////////
	
	String createRepository(String path, String masterRepoId);
	
	Repository getRepository(String id);
	List<Repository> getRepositories();
	
	String getMasterRepositoryId(String id);
	
	////////////////////////
	// Resources
	////////////////////////
	
	String createResource(String path, String repoId, String id);
	
	URI getResource(String repoId, String id);
	
	Resource loadResource(String repoId, String id);
	void saveResource(String repoId, String id);
	String moveResource(String sourceRepoId, String sourceId,
			String targetPath, String targetRepoId, String targetId);
	
	void deleteResource(String repoId, String id);
}
