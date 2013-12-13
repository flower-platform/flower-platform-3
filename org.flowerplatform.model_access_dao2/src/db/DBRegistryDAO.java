package db;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.model_access_dao2.RegistryDAO;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.registry.Design;
import org.flowerplatform.model_access_dao2.registry.Repository;
import org.flowerplatform.model_access_dao2.service.RegistryService;

import com.datastax.driver.core.Row;

public class DBRegistryDAO implements RegistryDAO {

	////////////////////////
	// Repositories
	////////////////////////
	
	@Override
	public UUID createRepository(String path, UUID repoId) {
		if (repoId == null) {
			repoId = UUIDGenerator.newUUID();
		}
		CassandraData.execute("INSERT INTO repository (repoId, path) VALUES (?, ?)",
				repoId, path);
		createDesign("../..", repoId, repoId);
		return repoId;
	}

	@Override
	public Repository getRepository(UUID repoId) {
		List<Row> result = CassandraData.execute("SELECT * FROM repository WHERE repoId = ?", repoId).all();
		if (result.size() == 0) {
			return null;
		}
		Repository repo = toRepository(result.get(0));
		Design design = getDesign(repoId);
		repo.setDefaultDesign(design);
		return repo;
	}

	protected Repository toRepository(Row row) {
		String path = row.getString("path");
		UUID repoId = row.getUUID("repoId");
		Repository repo = new Repository(path, repoId);
		return repo;
	}
	
	@Override
	public void updateRepository(Repository repo) {
		// nothing to do
	}
	
	@Override
	public void saveRepository(Repository repo) {
		createRepository(repo.getPath(), repo.getRepoId());
		updateRepository(repo);
		
		saveDesign(repo.getDefaultDesign());
	}
	
	////////////////////////
	// Designs
	////////////////////////
	
	@Override
	public UUID createDesign(String path, UUID repoId, UUID designId) {
		if (designId == null) {
			designId = UUIDGenerator.newUUID();
		}
		CassandraData.execute("INSERT INTO design (designId, repoId, path) VALUES (?, ?, ?)", 
				designId, repoId, path);
		return designId;
	}

	@Override
	public Design getDesign(UUID designId) {
		List<Row> result = CassandraData.execute("SELECT * FROM design WHERE designId = ?", designId).all();
		if (result.size() == 0) {
			return null;
		}
		return toDesign(result.get(0));
	}

	protected Design toDesign(Row row) {
		String path = row.getString("path");
		UUID repoId = row.getUUID("repoId");
		UUID designId = row.getUUID("designId");
		Design design = new Design(path, repoId, designId);
		Map<UUID, String> resources = row.getMap("resources", UUID.class, String.class);
		Map<String, UUID> resourcesAlias = row.getMap("resourcesAlias", String.class, UUID.class);
		design.setResources(resources);
		return design;
	}

	@Override
	public void updateDesign(Design design) {
		CassandraData.execute("UPDATE design SET resources = ? WHERE designId = ?", 
				design.getResources(), design.getDesignId());
	}

	@Override
	public void saveDesign(Design design) {
		createDesign(design.getPath(), design.getRepoId(), design.getDesignId());
		updateDesign(design);
		
		for (Entry<UUID, String> entry : design.getResources().entrySet()) {
			createResource(entry.getValue(), design.getDesignId(), entry.getKey());
			importResource(design, entry.getKey(), entry.getValue());
		}
	}
	
	////////////////////////
	// Resources
	////////////////////////
	
	@Override
	public UUID createResource(String path, UUID designId, UUID resourceId) {
		if (resourceId == null) {
			resourceId = UUIDGenerator.newUUID();
		}
		
		CassandraData.execute("INSERT INTO resource (resourceId, designId, path) VALUES (?, ?, ?)", 
				resourceId, designId, path);
		CassandraData.execute("UPDATE design SET resources[?] = ? WHERE designId = ?", resourceId, path, designId);
		
		return resourceId;
	}

	@Override
	public URI getResource(UUID designId, UUID resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveResource(UUID designId, UUID resourceId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Resource loadResource(UUID designId, UUID resourceId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void importResource(Design design, UUID resourceId, String path) {
		File file = RegistryService.INSTANCE.getResourceFile(design, path);
		ResourceSet resourceSet = RegistryService.INSTANCE.createResourceSet();
		Resource resource = resourceSet.getResource(
				RegistryService.INSTANCE.getPlatformDependentURI(file.getPath()), true);
		for (EObject eObject : resource.getContents()) {
			importEObject(design, resourceId, eObject);
		}
	}
	
	private void importEObject(Design design, UUID resourceId, EObject eObject) {
		EObjectDAO dao = EObjectDAO.INSTANCE;
		dao.saveEObject(design.getDesignId(), resourceId, eObject);
		EList<? extends EObject> children = dao.getChildren(eObject);
		if (children == null) {
			return;
		}
		for (EObject child : children) {
			importEObject(design, resourceId, child);
		}
	}

}
