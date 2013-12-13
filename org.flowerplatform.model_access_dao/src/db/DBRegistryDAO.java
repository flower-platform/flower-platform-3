package db;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.model_access_dao.RegistryDAO;
import org.flowerplatform.model_access_dao.UUIDGenerator;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.ResourceInfo;
import org.flowerplatform.model_access_dao.registry.Repository;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import emf.FlowerResourceURIHandler;

public class DBRegistryDAO implements RegistryDAO {

	@Override
	public UUID createRepository(String path, UUID masterRepoId) {
		// TODO create files too
		
		UUID id = UUIDGenerator.newUUID();
		Map<String, UUID> resourcesAlias = new HashMap<String, UUID>();
		resourcesAlias.put(Repository.MAPPING, org.flowerplatform.model_access_dao.UUIDGenerator.newUUID());
		resourcesAlias.put(Repository.APP_WIZARD, org.flowerplatform.model_access_dao.UUIDGenerator.newUUID());
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO repository (repoId, path, masterRepoId, resourcesAlias) VALUES (?, ?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(id, path, masterRepoId, resourcesAlias));
		
		return id;
	}

	@Override
	public Repository getRepository(UUID id) {
		String query = "SELECT * FROM repository WHERE repoId = ?";
		PreparedStatement stmt = CassandraData.getSession().prepare(query);
		List<Row> results = CassandraData.getSession().execute(stmt.bind(id)).all();
		
		if (results.size() == 0) {
			return null;
		}
		
		if (results.size() > 1) {
			throw new RuntimeException("Multiple rows returned for query = " + query);
		}
		
		return toRepository(results.get(0));
	}

	@Override
	public List<Repository> getRepositories() {
		ResultSet results = CassandraData.getSession().execute("SELECT * FROM repository");
		List<Repository> repositories = new ArrayList<Repository>();
		for (Row row : results) {
			repositories.add(toRepository(row));
		}
		return repositories;
	}
	
	private Repository toRepository(Row row) {
		Repository repo = new Repository(row.getUUID("repoId"), new File(row.getString("path")));
		repo.setMasterId(row.getUUID("masterRepoId"));
		repo.setResourcesAlias(row.getMap("resourcesAlias", String.class, UUID.class));
		return repo;
	}

	@Override
	public UUID getMasterRepositoryId(UUID id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UUID createResource(String path, UUID repoId, UUID id) {
		if (id == null) {
			id = UUIDGenerator.newUUID();
		}
		
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO resource (repoId, resourceId, path) VALUES (?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(repoId, id, path));
		
		return org.flowerplatform.model_access_dao.UUIDGenerator.newUUID();
	}

	@Override
	public URI getResource(UUID repoId, UUID id) {
		String query = "SELECT * FROM resource WHERE repoId = ? AND resourceId = ?";
		PreparedStatement stmt = CassandraData.getSession().prepare(query);
		List<Row> results = CassandraData.getSession().execute(stmt.bind(repoId, id)).all();
		
		if (results.size() == 0) {
			return null;
		}
		
		if (results.size() > 1) {
			throw new RuntimeException("Multiple rows returned for query = " + query);
		}
		
		Row row = results.get(0);
		URI uri = URI.createFileURI(row.getString("path"));
		
		return uri;
	}
	
	private Map<UUID, ResourceSet> resourceSets = new HashMap<UUID, ResourceSet>();
	
	private ResourceSet getResourceSet(UUID repoId) {
		// delegate to master repository
		UUID masterRepositoryId = getMasterRepositoryId(repoId);
		if (masterRepositoryId != null) {
			repoId = masterRepositoryId;
		}
		
		ResourceSet resourceSet = resourceSets.get(repoId);
		if (resourceSet != null) {
			return resourceSet;
		}
		
		resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl() {
			
			@Override
			public Resource createResource(URI uri) {
				return new XMIResourceImpl(uri);
			}
		});
		resourceSet.eAdapters().add(new ECrossReferenceAdapter());
		
		System.out.println("> created resource set");
		
		resourceSets.put(repoId, resourceSet);
		
		return resourceSet;
	}
	@Override
	public Resource loadResource(UUID repoId, UUID id) {
		Resource resource = getLoadedResource(repoId, id);
		if (resource != null) {
			return resource;
		}
		
		ResourceSet resourceSet = getResourceSet(repoId);
		URI uri = FlowerResourceURIHandler.createFlowerResourceURI(id);
		resource = resourceSet.createResource(uri);
		// no loading from disk, contents will be retrieved from DB
		
		ResourceInfo resourceInfo = ModelFactory.eINSTANCE.createResourceInfo();
		resourceInfo.setRepoId(repoId.toString());
		resourceInfo.setResourceId(id.toString());
		resource.getContents().add(resourceInfo);
		return resource;
	}

	@Override
	public void saveResource(UUID repoId, UUID id) {
		// nothing to do
	}
	
	private Resource getLoadedResource(UUID repoId, UUID id) {
		ResourceSet resourceSet = getResourceSet(repoId);
		URI uri = FlowerResourceURIHandler.createFlowerResourceURI(id);
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI().equals(uri)) {
				ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
				if (safeEquals(info.getRepoId(), repoId)) {
					return resource;
				}
			}
		}
		return null;
	}
	
	private boolean safeEquals(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		}
		return o1.equals(o2);
	}
	
	@Override
	public UUID moveResource(UUID sourceRepoId, UUID sourceId,
			String targetPath, UUID targetRepoId, UUID targetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteResource(UUID repoId, UUID id) {
		// TODO Auto-generated method stub

	}

}
