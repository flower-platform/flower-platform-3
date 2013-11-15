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
import org.flowerplatform.model_access_dao.registry.DirWithResources;
import org.flowerplatform.model_access_dao.registry.DiscussableDesign;
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
			String id = FileUtils.readFileToString(file);
			Repository repo = new Repository(id, file.getParentFile().getParentFile());
			repos.put(id, repo);
		} else if (file.getName().equals(DISCUSSABLE_DESIGN_CONFIG)) {
			String id = FileUtils.readFileToString(file);
			Repository repo = (Repository) repos.values().toArray()[0];
			DiscussableDesign discussableDesign = new DiscussableDesign(id, file.getParentFile());
			repo.getDiscussableDesigns().put(id, discussableDesign);
		} else if (file.getName().endsWith(".notation")) {
			ResourceSet resourceSet = getResourceSet(null);
			URI uri = URI.createFileURI(file.getPath());
			Resource resource = resourceSet.getResource(uri, true);
			ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
			Repository repo = getRepository(info.getRepoId());
			DiscussableDesign discussableDesign = getDiscussableDesign(repo, info.getDiscussableDesignId());
			Map<String, URI> resources = getResources(repo, discussableDesign);
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
	public String createRepository(String path) {
		// create repo dir
		File dir = new File(path);
		dir.mkdirs();
		
		// create FP data dir
		File fpd = new File(dir, FLOWER_PLATFORM_DATA);
		fpd.mkdir();
		
		// create discussable designs dir
		File dd = new File(fpd, DISCUSSABLE_DESIGNS);
		dd.mkdir();
		
		// generate UUID for resource
		String uuid = org.flowerplatform.model_access_dao.UUID.newUUID();
		Repository repo = new Repository(uuid, dir);
		repos.put(uuid, repo);
		
		// create config file
		File config = new File(fpd, CONFIG);
		try {
			config.createNewFile();
			FileUtils.writeStringToFile(config, uuid);
		} catch (IOException e) {
			throw new RuntimeException("Cannot create config file " + config, e);
		}
		
		// create global mapping resource
		createResource(GLOBAL_MAPPING_LOCATION, uuid, null, null);
		
		// create global app wizard resource
		createResource(GLOBAL_APP_WIZARD_LOCATION, uuid, null, null);

		System.out.println("> created repo " + dir);
		
		resourceSets.clear();
		
		return uuid;
	}
	
	@Override
	public Repository getRepository(String id) {
		return repos.get(id);
	}
	
	@Override
	public List<Repository> getRepositories() {
		return Arrays.asList(repos.values().toArray(new Repository[0]));
	}
	
	////////////////////////
	// Discussable Designs
	////////////////////////
	
	/**
	 * 
	 * @param repoId
	 * @param path relative to the discussable designs directory
	 * @return
	 */
	@Override
	public String createDiscussableDesign(String repoId, String path) {
		// create discussable design dir
		File dir = getDiscussableDesignDir(getRepository(repoId), path);
		dir.mkdirs();
		
		// create diagrams dir
		File dgrs = new File(dir, FLOWER_PLATFORM_DIAGRAMS);
		dgrs.mkdirs();
		
		// generate UUID for discussable design
		String uuid = org.flowerplatform.model_access_dao.UUID.newUUID();
		Repository repo = getRepository(repoId);
		DiscussableDesign discussableDesign = new DiscussableDesign(uuid, dir);
		repo.getDiscussableDesigns().put(uuid, discussableDesign);
		
		// create config file
		File config = new File(dir, DISCUSSABLE_DESIGN_CONFIG);
		try {
			config.createNewFile();
			FileUtils.writeStringToFile(config, uuid);
		} catch (IOException e) {
			throw new RuntimeException("Cannot create config file " + config, e);
		}
		
		// create local mapping resource
		createResource(MAPPING_LOCATION, repoId, uuid, getResourceId(repo, MAPPING_LOCATION));
		
		// create local app wizard resource
		createResource(APP_WIZARD_LOCATION, repoId, uuid, getResourceId(repo, APP_WIZARD_LOCATION));
		
		System.out.println("> created discussable design " + dir);
		
		resourceSets.clear();
		
		return uuid;
	}
	
	public String getResourceId(DirWithResources dirWithResources, String suffix) {
		for (Entry<String, URI> entry : dirWithResources.getResources().entrySet()) {
			if (entry.getValue().toFileString().endsWith(suffix)) {
				return entry.getKey();
			}
		}
		throw new RuntimeException(String.format("No resource with suffix %s for %s", suffix, dirWithResources.getDir()));
	}
	
	private File getDiscussableDesignDir(Repository repository, String dd) {
		if (dd == null) {
			return repository.getDir();
		}
		return new File(repository.getDir(), FLOWER_PLATFORM_DATA + "/" + DISCUSSABLE_DESIGNS + "/" + dd);
	}
	
	@Override
	public DiscussableDesign getDiscussableDesign(String repoId, String id) {
		return getDiscussableDesign(getRepository(repoId), id);
	}
	
	private DiscussableDesign getDiscussableDesign(Repository repo, String id) {
		return repo.getDiscussableDesigns().get(id);
	}
	
	public List<DiscussableDesign> getDiscussableDesigns(String repoId) {
		Repository repo = getRepository(repoId);
		return Arrays.asList(repo.getDiscussableDesigns().values().toArray(new DiscussableDesign[0]));
	}
	
	@Override
	public void deleteDiscussableDesign(String repoId, String id, boolean moveDiagrams) {
		// TODO
	}
	
	////////////////////////
	// Resources
	////////////////////////
	
	/**
	 * 
	 * @param resource
	 * @param path relative to the discussable design directory or repo
	 * @param repoId
	 * @param discussableDesignId
	 * @param uuid force using this uuid for this new resource
	 * @return
	 */
	@Override
	public String createResource(String path, String repoId, String discussableDesignId, String uuid) {
		Repository repo = getRepository(repoId);
		DiscussableDesign discussableDesign = getDiscussableDesign(repo, discussableDesignId);
		Map<String, URI> resources = getResources(repo, discussableDesign);
		
		File file = getFile(repo, discussableDesign, path);
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
			resource = loadResource(repoId, discussableDesignId, uuid);
			info = (ResourceInfo) resource.getContents().get(0);
		} else {
			resource = resourceSet.createResource(uri);
			info = ModelFactory.eINSTANCE.createResourceInfo();
			resource.getContents().add(0, info);
		}
		
		info.setRepoId(repoId);
		info.setDiscussableDesignId(discussableDesignId);
		info.setResourceId(uuid);
		saveResource(resource);
		
		System.out.println("> created resource " + path + " " + uuid);
		
		return uuid;
	}
	
	private File getFile(Repository repo, DiscussableDesign discussableDesign, String path) {
		if (discussableDesign != null) {
			return new File(discussableDesign.getDir(), path);
		} else {
			return new File(repo.getDir(), path);
		}
	}

	@Override
	public URI getResource(String repoId, String discussableDesignId, String id) {
		Repository repo = getRepository(repoId);
		DiscussableDesign discussableDesign = repo.getDiscussableDesigns().get(discussableDesignId);
		return getResource(repo, discussableDesign, id);
	}
	
	private URI getResource(Repository repo, DiscussableDesign discussableDesign, String id) {
		return getResources(repo, discussableDesign).get(id);
	}
	
	private Map<String, ResourceSet> resourceSets = new HashMap<String, ResourceSet>();
	
	private ResourceSet getResourceSet(String repoId) {
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
	public Resource loadResource(String repoId, String discussableDesignId, String id) {
		Resource resource = getLoadedResource(repoId, discussableDesignId, id);
		if (resource != null) {
			return resource;
		}
		
		ResourceSet resourceSet = getResourceSet(repoId);
		URI uri = FlowerResourceURIHandler.createFlowerResourceURI(id);
		resource = resourceSet.createResource(uri);
		Map<Object, Object> options = getOptions();
		options.put(FlowerResourceURIHandler.OPTION_REPO, repoId);
		options.put(FlowerResourceURIHandler.OPTION_DISCUSSABLE_DESIGN, discussableDesignId);
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
	public void saveResource(String repoId, String discussableDesignId, String id) {
		Resource resourceToSave = getLoadedResource(repoId, discussableDesignId, id);
		if (resourceToSave == null) {
			throw new RuntimeException(String.format("No resource loaded for repo = %s, dd = %s, id = %s", 
					repoId, discussableDesignId, id));
		}
		saveResource(resourceToSave);
	}
	
	private void saveResource(Resource resourceToSave) {
		for (Resource resource : resourceToSave.getResourceSet().getResources()) {
			Map<Object, Object> options = getOptions();
			ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
			String repoId = info.getRepoId();
			String discussableDesignId = info.getDiscussableDesignId();
			options.put(FlowerResourceURIHandler.OPTION_REPO, repoId);
			options.put(FlowerResourceURIHandler.OPTION_DISCUSSABLE_DESIGN, discussableDesignId);
			String resourceId = info.getResourceId();
			URI uri = getResourceURI(repoId, discussableDesignId, resourceId);
			try {
				resource.save(options);
			} catch (IOException e) {
				throw new RuntimeException("Cannot save resource " + uri, e);
			}
			
			System.out.println("> saved resource " + info.getResourceId() + " " + uri);
		}
	}
	
	private URI getResourceURI(String repoId, String discussableDesignId, String resourceId) {
		Map<String, URI> resources = getResources(repoId, discussableDesignId);
		return resources.get(resourceId);
	}
	
	private Resource getLoadedResource(String repoId, String discussableDesignId, String id) {
		ResourceSet resourceSet = getResourceSet(repoId);
		URI uri = FlowerResourceURIHandler.createFlowerResourceURI(id);
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI().equals(uri)) {
				ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
				if (safeEquals(info.getRepoId(), repoId) && safeEquals(info.getDiscussableDesignId(), discussableDesignId)) {
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
	
	private Map<String, URI> getResources(Repository repo, DiscussableDesign discussableDesign) {
		return discussableDesign == null ? repo.getResources() : discussableDesign.getResources();
	}
	
	private Map<String, URI> getResources(String repoId, String discussableDesignId) {
		Repository repo = getRepository(repoId);
		DiscussableDesign discussableDesign = getDiscussableDesign(repo, discussableDesignId);
		return getResources(repo, discussableDesign);
	}
	
	@Override
	public String moveResource(String sourceRepoId, String sourceDiscussableDesignId, String sourceId, 
			String targetPath, String targetRepoId, String targetDiscussableDesignId, String targetId) {
		URI sourceUri = getResource(sourceRepoId, sourceDiscussableDesignId, sourceId);
		File sourceFile = new File(sourceUri.toFileString());
		File targetFile = getFile(targetRepoId, targetDiscussableDesignId, targetPath);
		try {
			FileUtils.copyFile(sourceFile, targetFile);
		} catch (IOException e) {
			throw new RuntimeException("Cannot copy " + sourceFile + " -> " + targetFile, e);
		}
		targetId = createResource(targetPath, targetRepoId, targetDiscussableDesignId, targetId);
		deleteResource(sourceRepoId, sourceDiscussableDesignId, sourceId);
		return targetId;
	}
	
	private File getFile(String repoId, String discussableDesignId, String path) {
		return getFile(getRepository(repoId), getDiscussableDesign(repoId, discussableDesignId), path);
	}
	
	@Override
	public void deleteResource(String repoId, String discussableDesignId, String id) {
		Map<String, URI> resources = getResources(repoId, discussableDesignId);
		URI uri = resources.get(id);
		File file = new File(uri.toFileString());
		file.delete();
		resources.remove(id);
	}
	
}
