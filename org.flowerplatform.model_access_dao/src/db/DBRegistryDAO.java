package db;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.model_access_dao.RegistryDAO;
import org.flowerplatform.model_access_dao.UUID;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.ResourceInfo;
import org.flowerplatform.model_access_dao.registry.Repository;

import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;

import emf.FlowerResourceURIHandler;

public class DBRegistryDAO implements RegistryDAO {

	@Override
	public String createRepository(String path, String masterRepoId) {
		// TODO create files too
		
		String id = UUID.newUUID();
		PreparedStatement stmt = CassandraData.getSession().prepare(
				"INSERT INTO repository (repoId, path, masterRepoId) VALUES (?, ?, ?)");
		CassandraData.getSession().execute(stmt.bind(id, path, masterRepoId));
		
		return id;
	}

	@Override
	public Repository getRepository(String id) {
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
		Repository repo = new Repository(row.getString("repoId"), new File(row.getString("path")));
		repo.setMasterId(row.getString("masterRepoId"));
		return repo;
	}

	@Override
	public String getMasterRepositoryId(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createResource(String path, String repoId, String id) {
		if (id != null) {
			return id;
		}
		return UUID.newUUID();
	}

	@Override
	public URI getResource(String repoId, String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Map<String, ResourceSet> resourceSets = new HashMap<String, ResourceSet>();
	
	private ResourceSet getResourceSet(String repoId) {
		// delegate to master repository
		String masterRepositoryId = getMasterRepositoryId(repoId);
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
	public Resource loadResource(String repoId, String id) {
		Resource resource = getLoadedResource(repoId, id);
		if (resource != null) {
			return resource;
		}
		
		ResourceSet resourceSet = getResourceSet(repoId);
		URI uri = FlowerResourceURIHandler.createFlowerResourceURI(id);
		resource = resourceSet.createResource(uri);
		// no loading from disk, contents will be retrieved from DB
		
		ResourceInfo resourceInfo = ModelFactory.eINSTANCE.createResourceInfo();
		resourceInfo.setRepoId(repoId);
		resourceInfo.setResourceId(id);
		resource.getContents().add(resourceInfo);
		return resource;
	}

	@Override
	public void saveResource(String repoId, String id) {
		// nothing to do
	}
	
	private Resource getLoadedResource(String repoId, String id) {
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
	public String moveResource(String sourceRepoId, String sourceId,
			String targetPath, String targetRepoId, String targetId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteResource(String repoId, String id) {
		// TODO Auto-generated method stub

	}

}
