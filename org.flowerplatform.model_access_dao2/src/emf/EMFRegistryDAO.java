package emf;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.flowerplatform.model_access_dao2.FlowerResourceURIHandler;
import org.flowerplatform.model_access_dao2.RegistryDAO;
import org.flowerplatform.model_access_dao2.UUIDGenerator;
import org.flowerplatform.model_access_dao2.model.ModelFactory;
import org.flowerplatform.model_access_dao2.model.ResourceInfo;
import org.flowerplatform.model_access_dao2.registry.Design;
import org.flowerplatform.model_access_dao2.registry.Repository;
import org.flowerplatform.model_access_dao2.service.RegistryService;

public class EMFRegistryDAO implements RegistryDAO {

	////////////////////////
	// Repositories
	////////////////////////
	
	private Map<UUID, Repository> repos = new HashMap<UUID, Repository>();
	
	@Override
	public UUID createRepository(String path, UUID repoId) {
		// create repo
		if (repoId == null) {
			repoId = UUIDGenerator.newUUID();
		}
		
		Repository repo = new Repository(path, repoId);
		repos.put(repoId, repo);
		
		// create default design
		createDesign("../..", repoId, repoId);
		repo.setDefaultDesign(getDesign(repoId));
		
		return repoId;
	}

	@Override
	public Repository getRepository(UUID repoId) {
		return repos.get(repoId);
	}

	@Override
	public void updateRepository(Repository repo) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void saveRepository(Repository repo) {
		// TODO Auto-generated method stub
		
	}
	
	////////////////////////
	// Designs
	////////////////////////
	
	private Map<UUID, Design> designs = new HashMap<UUID, Design>();
	
	@Override
	public UUID createDesign(String path, UUID repoId, UUID designId) {
		if (designId == null) {
			designId = UUIDGenerator.newUUID();
		}
		Design design = new Design(path, repoId, designId);
		designs.put(designId, design);
		return designId;
	}

	@Override
	public Design getDesign(UUID designId) {
		return designs.get(designId);
	}

	@Override
	public void updateDesign(Design design) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveDesign(Design design) {
		// TODO Auto-generated method stub
		
	}
	
	////////////////////////
	// Resources
	////////////////////////
	
	@Override
	public UUID createResource(String path, UUID designId, UUID resourceId) {
		if (resourceId == null) {
			resourceId = UUIDGenerator.newUUID();
		}
		
		// find the design
		Design design = getDesign(designId);
		design.getResources().put(resourceId, path);
		
		ResourceSet resourceSet = getResourceSet(designId);
		Resource resource = null;
		ResourceInfo info = null;
		File file = RegistryService.INSTANCE.getResourceFile(design, path);
		if (file.exists()) {
			// load from existing file
			resource = loadResource(designId, resourceId);
			info = (ResourceInfo) resource.getContents().get(0);
		} else {
			// create new resource
			resource = resourceSet.createResource(
					FlowerResourceURIHandler.createFlowerResourceURI(resourceId));
			info = ModelFactory.eINSTANCE.createResourceInfo();
			resource.getContents().add(0, info);
		}
		
		// set resource info
		info.setDesignId(designId.toString());
		info.setResourceId(resourceId.toString());
		
		return resourceId;
	}
	
	private Map<UUID, ResourceSet> resourceSets = new HashMap<UUID, ResourceSet>();
	
	private ResourceSet getResourceSet(UUID designId) {
		ResourceSet resourceSet = resourceSets.get(designId);
		if (resourceSet != null) {
			return resourceSet;
		}
		
		resourceSet = RegistryService.INSTANCE.createResourceSet();
		// add it in front of the list, to make sure the URI won't be handled by a default implementation
        resourceSet.getURIConverter().getURIHandlers().add(0, 
        		new FlowerResourceURIHandler(RegistryService.INSTANCE.getPlatformDependentHandler()));
		resourceSets.put(designId, resourceSet);
		
		return resourceSet;
	}
	
	private Map<Object, Object> getOptions() {
		Map<Object, Object> options = new HashMap<Object, Object>();
		options.put(XMLResource.OPTION_ENCODING, "UTF-8");
		options.put(XMLResource.OPTION_XML_VERSION, "1.1");
		options.put(XMLResource.OPTION_SCHEMA_LOCATION_IMPLEMENTATION, Boolean.TRUE);
		return options;
	}
	
	@Override
	public URI getResource(UUID designId, UUID resourceId) {
		Design design = getDesign(designId);
		String path = design.getResources().get(resourceId);
		if (path == null) {
			// find the resource in the repo
			UUID repoId = design.getRepoId();
			if (repoId != null) {
				return getResource(repoId, resourceId);
			}
		}
		if (path == null) {
			throw new RuntimeException(String.format("Resource with id %s not registered in design with id %s", 
					resourceId.toString(), designId.toString()));
		}
		path = RegistryService.INSTANCE.getResourceFile(design, path).getPath();
		return RegistryService.INSTANCE.getPlatformDependentURI(path);
	}

	@Override
	public Resource loadResource(UUID designId, UUID resourceId) {
		Resource resource = getLoadedResource(designId, resourceId);
		if (resource != null) {
			return resource;
		}
		
		ResourceSet resourceSet = getResourceSet(designId);
		URI uri = FlowerResourceURIHandler.createFlowerResourceURI(resourceId);
		resource = resourceSet.createResource(uri);
		Map<Object, Object> options = getOptions();
		options.put(FlowerResourceURIHandler.OPTION_DESIGN_ID, designId);
		try {
			resource.load(options);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load resource " + uri, e);
		}
		
		
		Design design = getDesign(designId);
		if (!design.getResources().containsKey(resourceId) && design.getRepoId() != null) {
			// set the resource info in case the resource was loaded from the repo
			ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
			info.setDesignId(designId.toString());
			info.setResourceId(resourceId.toString());
			
			Design defaultDesign = getDesign(design.getRepoId());
			String path = defaultDesign.getResources().get(resourceId);
			design.getResources().put(resourceId, path);
		}
	
		return resource;
	}

	/**
	 * Find the reource with the same resourceId and designId in the resourceSet.
	 */
	private Resource getLoadedResource(UUID designId, UUID resourceId) {
		ResourceSet resourceSet = getResourceSet(designId);
		URI uri = FlowerResourceURIHandler.createFlowerResourceURI(resourceId);
		for (Resource resource : resourceSet.getResources()) {
			if (resource.getURI().equals(uri)) {
				ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
				if (safeEquals(info.getDesignId(), designId.toString())) {
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
	public void saveResource(UUID designId, UUID resourceId) {
		Resource resourceToSave = getLoadedResource(designId, resourceId);
		if (resourceToSave == null) {
			throw new RuntimeException(String.format(
					"No resource loaded for repo = %s, id = %s", designId, resourceId));
		}
		saveResource(resourceToSave);
	}
	
	private void saveResource(Resource resourceToSave) {
		for (Resource resource : resourceToSave.getResourceSet().getResources()) {
			Map<Object, Object> options = getOptions();
			ResourceInfo info = (ResourceInfo) resource.getContents().get(0);
			UUID designId = UUID.fromString(info.getDesignId());
			// set the DESIGN_ID in the options map so the URI handler will be able 
			// to convert from the resource UUID to the actual location on the disk
			options.put(FlowerResourceURIHandler.OPTION_DESIGN_ID, designId);
			UUID resourceId = UUID.fromString(info.getResourceId());
			URI uri = getResource(designId, resourceId);
			try {
				resource.save(options);
			} catch (IOException e) {
				throw new RuntimeException("Cannot save resource " + uri, e);
			}
		}
	}

}
