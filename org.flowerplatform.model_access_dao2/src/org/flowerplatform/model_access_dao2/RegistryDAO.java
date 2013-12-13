package org.flowerplatform.model_access_dao2;

import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.model_access_dao2.registry.Design;
import org.flowerplatform.model_access_dao2.registry.Repository;

public interface RegistryDAO {

	////////////////////////
	// Repositories
	////////////////////////

	UUID createRepository(String path, UUID repoId);
	Repository getRepository(UUID repoId);
	void updateRepository(Repository repo);
	void saveRepository(Repository repo);
	
	////////////////////////
	// Designs
	////////////////////////
		
	UUID createDesign(String path, UUID repoId, UUID designId);
	Design getDesign(UUID designId);
	void updateDesign(Design design);
	void saveDesign(Design design);
	
	////////////////////////
	// Resources
	////////////////////////
	
	UUID createResource(String path, UUID designId, UUID resourceId);
	URI getResource(UUID designId, UUID resourceId);
	void saveResource(UUID designId, UUID resourceId);
	
	Resource loadResource(UUID designId, UUID resourceId);
	
}
