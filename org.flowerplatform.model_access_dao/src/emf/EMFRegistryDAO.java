package emf;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.FileURIHandlerImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.model_access_dao.RegistryDAO;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.ResourceInfo;
import org.flowerplatform.model_access_dao.registry.Repository;

public class EMFRegistryDAO implements RegistryDAO {
	
	private Map<String, Repository> repos = new HashMap<String, Repository>();
	
	public EMFRegistryDAO() {
		try {
			iterateFiles(new File("repos"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void iterateFiles(File file) throws IOException {
		if (file.getName().equals(CONFIG) && file.getParentFile().getName().equals(FLOWER_PLATFORM_DATA)) {
			String config = FileUtils.readFileToString(file);
			String id = null, masterRepoId = null;
			if (config.contains(" ")) {
				String[] ids = config.split(" ");
				id = ids[0];
				masterRepoId = ids[1];
			}
			Repository repo = new Repository(id, file.getParentFile().getParentFile());
			repo.setMasterId(masterRepoId);
			repos.put(id, repo);
		} else if (file.getName().endsWith(".notation")) {
			ResourceSet resourceSet = getResourceSet(null);
			URI uri = URI.createFileURI(file.getPath());
			Resource resource = resourceSet.getResource(uri, true);
			ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
			Repository repo = getRepository(info.getRepoId());
			Map<String, URI> resources = repo.getResources();
			resources.put(info.getResourceId(), uri);
		}
		File[] children = file.listFiles();
		if (children != null) {
			for (File child : children) {
				iterateFiles(child);
			}
		}
	}
	
	////////////////////////
	// Repositories
	////////////////////////
	
	@Override
	public String createRepository(String path, String masterRepoId) {
		// create repo dir
		File dir = new File(path);
		dir.mkdirs();
		
		// create FP data dir
		File fpd = new File(dir, FLOWER_PLATFORM_DATA);
		fpd.mkdir();
		
		// create diagrams dir
		File dgrs = new File(dir, FLOWER_PLATFORM_DIAGRAMS);
		dgrs.mkdirs();
		
		// create discussable designs dir
		if (masterRepoId == null) {
			File dd = new File(fpd, DISCUSSABLE_DESIGNS);
			dd.mkdir();
		}
		
		// generate UUID for resource
		String uuid = org.flowerplatform.model_access_dao.UUID.newUUID();
		Repository repo = new Repository(uuid, dir);
		repo.setMasterId(masterRepoId);
		repos.put(uuid, repo);
		
		// create config file
		File config = new File(fpd, CONFIG);
		try {
			config.createNewFile();
			FileUtils.writeStringToFile(config, masterRepoId == null ? uuid : uuid + " " + masterRepoId);
		} catch (IOException e) {
			throw new RuntimeException("Cannot create config file " + config, e);
		}
		
		String mappingLocation = null, appWizardLocation = null;
		if (masterRepoId == null) {
			mappingLocation = GLOBAL_MAPPING_LOCATION;
			appWizardLocation = GLOBAL_APP_WIZARD_LOCATION;
		} else {
			mappingLocation = MAPPING_LOCATION;
			appWizardLocation = APP_WIZARD_LOCATION;
		}
		
		if (masterRepoId != null) {
			repo = getRepository(masterRepoId);
		}
		
		// create mapping resource
		createResource(mappingLocation, uuid, getResourceId(repo, mappingLocation));
		
		// create app wizard resource
		createResource(appWizardLocation, uuid, getResourceId(repo, appWizardLocation));

		System.out.println("> created repo " + dir);
		
		resourceSets.clear();
		
		return uuid;
	}
	
	public String getResourceId(Repository dirWithResources, String suffix) {
		for (Entry<String, URI> entry : dirWithResources.getResources().entrySet()) {
			if (entry.getValue().toFileString().endsWith(suffix)) {
				return entry.getKey();
			}
		}
		return null;
//		throw new RuntimeException(String.format("No resource with suffix %s for %s", suffix, dirWithResources.getDir()));
	}
	
	@Override
	public Repository getRepository(String id) {
		return repos.get(id);
	}
	
	@Override
	public List<Repository> getRepositories() {
		return Arrays.asList(repos.values().toArray(new Repository[0]));
	}
	
	public String getMasterRepositoryId(String id) {
		Repository repo = getRepository(id);
		if (repo.getMasterId() != null) {
			return repo.getMasterId();
		}
		return null;
	}
	
	////////////////////////
	// Resources
	////////////////////////
	
	/**
	 * 
	 * @param resource
	 * @param path relative to the discussable design directory or repo
	 * @param repoId
	 * @param uuid force using this uuid for this new resource
	 * @return
	 */
	@Override
	public String createResource(String path, String repoId, String uuid) {
		Repository repo = getRepository(repoId);
		Map<String, URI> resources = repo.getResources();
		
		File file = getFile(repo, path);
		path = file.getPath();
		
		Resource resource = null;
		URI uri = URI.createFileURI(path);
		if (uuid == null) {
			uuid = org.flowerplatform.model_access_dao.UUID.newUUID();
		}
		resources.put(uuid, uri);
		uri = FlowerResourceURIHandler.createFlowerResourceURI(uuid);
		ResourceSet resourceSet = getResourceSet(repoId);
		ResourceInfo info = null;
		if (file.exists()) {
			resource = loadResource(repoId, uuid);
			info = (ResourceInfo) resource.getContents().get(0);
		} else {
			resource = resourceSet.createResource(uri);
			info = ModelFactory.eINSTANCE.createResourceInfo();
			resource.getContents().add(0, info);
		}
		
		info.setRepoId(repoId);
		info.setResourceId(uuid);
		saveResource(resource);
		
		System.out.println("> created resource " + path + " " + uuid);
		
		return uuid;
	}
	
	private File getFile(Repository repo, String path) {
		return new File(repo.getDir(), path);
	}

	@Override
	public URI getResource(String repoId, String id) {
		Repository repo = getRepository(repoId);
		return getResource(repo, id);
	}
	
	private URI getResource(Repository repo, String id) {
		return repo.getResources().get(id);
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
		
		// add it in front of the list, to make sure the URI won't be handled by a default implementation
        resourceSet.getURIConverter().getURIHandlers().add(0, new FlowerResourceURIHandler(getPlatformDependentHandler()));
		
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
		Map<Object, Object> options = getOptions();
		options.put(FlowerResourceURIHandler.OPTION_REPO, repoId);
		try {
			resource.load(options);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load resource " + uri, e);
		}
		
		return resource;
	}
	
	private boolean safeEquals(Object o1, Object o2) {
		if (o1 == null) {
			return o2 == null;
		}
		return o1.equals(o2);
	}
	
	@Override
	public void saveResource(String repoId, String id) {
		Resource resourceToSave = getLoadedResource(repoId, id);
		if (resourceToSave == null) {
			throw new RuntimeException(String.format("No resource loaded for repo = %s, id = %s", 
					repoId, id));
		}
		saveResource(resourceToSave);
	}
	
	private void saveResource(Resource resourceToSave) {
		for (Resource resource : resourceToSave.getResourceSet().getResources()) {
			Map<Object, Object> options = getOptions();
			ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
			String repoId = info.getRepoId();
			options.put(FlowerResourceURIHandler.OPTION_REPO, repoId);
			String resourceId = info.getResourceId();
			URI uri = getResourceURI(repoId, resourceId);
			try {
				resource.save(options);
			} catch (IOException e) {
				throw new RuntimeException("Cannot save resource " + uri, e);
			}
			
			System.out.println("> saved resource " + info.getResourceId() + " " + uri);
		}
	}
	
	private URI getResourceURI(String repoId, String resourceId) {
		Map<String, URI> resources = getResources(repoId);
		return resources.get(resourceId);
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
	
	private Map<Object, Object> getOptions() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_XML_VERSION, "1.1");
		options.put(XMLResource.OPTION_SCHEMA_LOCATION_IMPLEMENTATION, Boolean.TRUE);
		return options;
	}
	
	private URIHandler getPlatformDependentHandler() {
		return new FileURIHandlerImpl();
	}
	
	private Map<String, URI> getResources(String repoId) {
		Repository repo = getRepository(repoId);
		return repo.getResources();
	}
	
	@Override
	public String moveResource(String sourceRepoId, String sourceId, 
			String targetPath, String targetRepoId, String targetId) {
		URI sourceUri = getResource(sourceRepoId, sourceId);
		File sourceFile = new File(sourceUri.toFileString());
		File targetFile = getFile(targetRepoId, targetPath);
		try {
			FileUtils.copyFile(sourceFile, targetFile);
		} catch (IOException e) {
			throw new RuntimeException("Cannot copy " + sourceFile + " -> " + targetFile, e);
		}
		targetId = createResource(targetPath, targetRepoId, targetId);
		deleteResource(sourceRepoId, sourceId);
		return targetId;
	}
	
	private File getFile(String repoId, String path) {
		return getFile(getRepository(repoId), path);
	}
	
	@Override
	public void deleteResource(String repoId, String id) {
		Map<String, URI> resources = getResources(repoId);
		URI uri = resources.get(id);
		File file = new File(uri.toFileString());
		file.delete();
		resources.remove(id);
	}
	
}
