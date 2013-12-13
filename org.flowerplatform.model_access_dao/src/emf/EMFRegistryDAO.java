package emf;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

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
import org.flowerplatform.model_access_dao.UUIDGenerator;
import org.flowerplatform.model_access_dao.model.ModelFactory;
import org.flowerplatform.model_access_dao.model.ResourceInfo;
import org.flowerplatform.model_access_dao.registry.Repository;

public class EMFRegistryDAO implements RegistryDAO {
	
	private Map<UUID, Repository> repos = new HashMap<UUID, Repository>();
	
	public EMFRegistryDAO() {
//		try {
//			iterateFiles(new File("repos"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	private void iterateFiles(File file) throws IOException {
		if (file.getName().equals(CONFIG) && file.getParentFile().getName().equals(FLOWER_PLATFORM_DATA)) {
			String config = FileUtils.readFileToString(file);
			UUID id = null, masterRepoId = null;
			if (config.contains(" ")) {
				String[] ids = config.split(" ");
				id = UUID.fromString(ids[0]);
				masterRepoId = UUID.fromString(ids[1]);
			}
			Repository repo = new Repository(id, file.getParentFile().getParentFile());
			repo.setMasterId(masterRepoId);
			repos.put(id, repo);
		} else if (file.getName().endsWith(".notation")) {
			ResourceSet resourceSet = getResourceSet(null);
			URI uri = URI.createFileURI(file.getPath());
			Resource resource = resourceSet.getResource(uri, true);
			ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
			Repository repo = getRepository(UUID.fromString(info.getRepoId()));
			Map<UUID, URI> resources = repo.getResources();
			resources.put(UUID.fromString(info.getResourceId()), uri);
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
	public UUID createRepository(String path, UUID masterRepoId) {
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
		UUID uuid = UUIDGenerator.newUUID();
		Repository repo = new Repository(uuid, dir);
		repo.setMasterId(masterRepoId);
		repos.put(uuid, repo);
		
		// create config file
		File config = new File(fpd, CONFIG);
		try {
			config.createNewFile();
			FileUtils.writeStringToFile(config, 
					masterRepoId == null ? uuid.toString() : uuid.toString() + " " + masterRepoId.toString());
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
		
		Repository masterRepo = null;
		if (masterRepoId != null) {
			masterRepo = getRepository(masterRepoId);
		} else {
			masterRepo = repo;
		}
		
		// create mapping resource
		repo.getResourcesAlias().put("mapping", 
				createResource(mappingLocation, uuid, getResourceId(masterRepo, mappingLocation)));
		
		// create app wizard resource
		repo.getResourcesAlias().put("app-wizard", 
				createResource(appWizardLocation, uuid, getResourceId(masterRepo, appWizardLocation)));

		System.out.println("> created repo " + dir);
		
		resourceSets.clear();
		
		return uuid;
	}
	
	public UUID getResourceId(Repository dirWithResources, String suffix) {
		for (Entry<UUID, URI> entry : dirWithResources.getResources().entrySet()) {
			if (entry.getValue().toFileString().endsWith(suffix)) {
				return entry.getKey();
			}
		}
		return null;
//		throw new RuntimeException(String.format("No resource with suffix %s for %s", suffix, dirWithResources.getDir()));
	}
	
	@Override
	public Repository getRepository(UUID id) {
		return repos.get(id);
	}
	
	@Override
	public List<Repository> getRepositories() {
		return Arrays.asList(repos.values().toArray(new Repository[0]));
	}
	
	public UUID getMasterRepositoryId(UUID id) {
		Repository repo = getRepository(id);
		if (repo != null && repo.getMasterId() != null) {
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
	public UUID createResource(String path, UUID repoId, UUID uuid) {
		Repository repo = getRepository(repoId);
		Map<UUID, URI> resources = repo.getResources();
		
		File file = getFile(repo, path);
		path = file.getPath();
		
		Resource resource = null;
		URI uri = URI.createFileURI(path);
		if (uuid == null) {
			uuid = UUIDGenerator.newUUID();
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
		
		info.setRepoId(repoId.toString());
		info.setResourceId(uuid.toString());
		saveResource(resource);
		
		System.out.println("> created resource " + path + " " + uuid);
		
		return uuid;
	}
	
	private File getFile(Repository repo, String path) {
		return new File(repo.getDir(), path);
	}

	@Override
	public URI getResource(UUID repoId, UUID id) {
		Repository repo = getRepository(repoId);
		URI uri = getResource(repo, id);
		return uri;
	}
	
	private URI getResource(Repository repo, UUID id) {
		return repo.getResources().get(id);
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
		
		// add it in front of the list, to make sure the URI won't be handled by a default implementation
        resourceSet.getURIConverter().getURIHandlers().add(0, new FlowerResourceURIHandler(getPlatformDependentHandler()));
		
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
	public void saveResource(UUID repoId, UUID id) {
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
			UUID repoId = UUID.fromString(info.getRepoId());
			options.put(FlowerResourceURIHandler.OPTION_REPO, repoId);
			UUID resourceId = UUID.fromString(info.getResourceId());
			URI uri = getResourceURI(repoId, resourceId);
			try {
				resource.save(options);
			} catch (IOException e) {
				throw new RuntimeException("Cannot save resource " + uri, e);
			}
			
			System.out.println("> saved resource " + info.getResourceId() + " " + uri);
		}
	}
	
	private URI getResourceURI(UUID repoId, UUID resourceId) {
		Map<UUID, URI> resources = getResources(repoId);
		return resources.get(resourceId);
	}
	
	private Resource getLoadedResource(UUID repoId, UUID id) {
		ResourceSet resourceSet = getResourceSet(repoId);
		URI uri = FlowerResourceURIHandler.createFlowerResourceURI(id);
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI().equals(uri)) {
				ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
				if (safeEquals(info.getRepoId(), repoId.toString())) {
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
	
	private Map<UUID, URI> getResources(UUID repoId) {
		Repository repo = getRepository(repoId);
		return repo.getResources();
	}
	
	@Override
	public UUID moveResource(UUID sourceRepoId, UUID sourceId, 
			String targetPath, UUID targetRepoId, UUID targetId) {
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
	
	private File getFile(UUID repoId, String path) {
		return getFile(getRepository(repoId), path);
	}
	
	@Override
	public void deleteResource(UUID repoId, UUID id) {
		Map<UUID, URI> resources = getResources(repoId);
		URI uri = resources.get(id);
		File file = new File(uri.toFileString());
		file.delete();
		resources.remove(id);
	}
	
}
