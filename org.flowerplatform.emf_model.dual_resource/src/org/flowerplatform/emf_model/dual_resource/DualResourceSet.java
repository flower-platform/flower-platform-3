package org.flowerplatform.emf_model.dual_resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.PlatformResourceURIHandlerImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceFactoryImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class DualResourceSet extends ResourceSetImpl {	

	public DualResourceSet() {
		super();
		
		getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl() {
			
			@Override
			public Resource createResource(URI uri) {
				System.out.println("Create dual resource");
				return new DualResource(uri);
			}
		});
		
		// add it in front of the list, to make sure the URI won't be handled by a default implementation
		getURIConverter().getURIHandlers().add(0, new DualResourceURIHandler(getPlatformDependentHandler()));
	}

	private URIHandler getPlatformDependentHandler() {
		return new PlatformResourceURIHandlerImpl();
	}
	
}
