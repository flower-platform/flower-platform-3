package org.flowerplatform.model_access_dao2.service;

import java.io.File;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.FileURIHandlerImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.flowerplatform.model_access_dao2.DAOFactory;
import org.flowerplatform.model_access_dao2.registry.Design;
import org.flowerplatform.model_access_dao2.registry.Repository;

public class RegistryService {

	public static final String FLOWER_PLATFORM_DATA = "flower-platform-data";
	public static final String DESIGNS = "designs";
	
	public static RegistryService INSTANCE = new RegistryService();
	
	private RegistryService() {
		
	}
	
	public File getRepositoryFile(Repository repo) {
		return new File(repo.getPath(), FLOWER_PLATFORM_DATA);
	}
	
	public File getDesignFile(Design design) {
		// find parent repo
		Repository repo = DAOFactory.getRegistryDAO().getRepository(design.getRepoId());
		File designsDir = new File(new RegistryService().getRepositoryFile(repo), DESIGNS);
		return new File(designsDir, design.getPath());
	}
	
	public File getResourceFile(Design design, String path) {
		File designDir = new RegistryService().getDesignFile(design);
		return new File(designDir, path);
	}
	
	public UUID createRepository(String path) {
		UUID repoId = DAOFactory.getRegistryDAO().createRepository(path, null);
		
		// create dir
		File dir = new File(path);
		dir.mkdirs();
		
		// create FP data dir
		File fpd = new File(dir, FLOWER_PLATFORM_DATA);
		fpd.mkdir();
				
		// create designs dir
		File designs = new File(fpd, DESIGNS);
		designs.mkdir();
		
		return repoId;
	}
	
	public UUID createDesign(String path, UUID repoId) {
		UUID designId = DAOFactory.getRegistryDAO().createDesign(path, repoId, null);
		
		// create design dir
		Design design = DAOFactory.getRegistryDAO().getDesign(designId);
		File dir = RegistryService.INSTANCE.getDesignFile(design);
		dir.mkdirs();
		
		return designId;
	}
	
	public ResourceSet createResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl() {
			
			@Override
			public Resource createResource(URI uri) {
				return new XMIResourceImpl(uri);
			}
		});
		resourceSet.eAdapters().add(new ECrossReferenceAdapter());
		
		return resourceSet;
	}
	
	public URIHandler getPlatformDependentHandler() {
		return new FileURIHandlerImpl();
	}
	
	public URI getPlatformDependentURI(String path) {
		return URI.createFileURI(path);
	}
	
}
