package org.flowerplatform.model_access_dao;

import java.util.List;
import java.util.UUID;

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
	
	UUID createRepository(String path, UUID masterRepoId);
	
	Repository getRepository(UUID id);
	List<Repository> getRepositories();
	
	UUID getMasterRepositoryId(UUID id);
	
	////////////////////////
	// Resources
	////////////////////////
	
	UUID createResource(String path, UUID repoId, UUID id);
	
	URI getResource(UUID repoId, UUID id);
	
	Resource loadResource(UUID repoId, UUID id);
	void saveResource(UUID repoId, UUID id);
	UUID moveResource(UUID sourceRepoId, UUID sourceId,
			String targetPath, UUID targetRepoId, UUID targetId);
	
	void deleteResource(UUID repoId, UUID id);
}
