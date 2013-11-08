package org.flowerplatform.emf_model.dual_resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.BasicInternalEList;
import org.eclipse.emf.ecore.xmi.XMLHelper;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class DualResource extends XMIResourceImpl {
	
	protected Resource globalResource;
	
	public DualResource(URI uri) {
		super(uri);
	}

	@Override
	protected boolean useUUIDs() {
		return true;
	}

	@Override
	public EList<EObject> getContents() {
		BasicInternalEList<EObject> contents = new BasicInternalEList<EObject>(EObject.class);
		contents.addAll(super.getContents()); 
		
		if (globalResource == null) {
			IResource resource = DualResourceURIHandler.getPlatformDependentResource(getURI());
			IProject project = resource.getProject();
			String path = project.getName() + "/" + DualResourceURIHandler.GLOBAL_MAPPING_FILE_PATH;
			globalResource = getResourceSet().getResource(URI.createPlatformResourceURI(path, true), true);
			contents.addAll(globalResource.getContents());
		}
		
		return contents;
	}
	
	@Override
	public void doLoad(InputStream inputStream, Map<?, ?> options) throws IOException {
		super.doLoad(inputStream, options);
		loadGlobalResource();
	}

	@Override
	public void doLoad(Node node, Map<?, ?> options) throws IOException {
		super.doLoad(node, options);
		loadGlobalResource();
	}

	@Override
	public void doLoad(InputSource inputSource, Map<?, ?> options) throws IOException {
		super.doLoad(inputSource, options);
		loadGlobalResource();
	}
	
	protected void loadGlobalResource() {
		if (DualResourceURIHandler.isDualResourceURI(getURI())) {
			IResource resource = DualResourceURIHandler.getPlatformDependentResource(getURI());
			IProject project = resource.getProject();
			String path = project.getName() + "/" + DualResourceURIHandler.GLOBAL_MAPPING_FILE_PATH;
			globalResource = getResourceSet().getResource(URI.createPlatformResourceURI(path, true), true);
		}
	}

	@Override
	protected XMLHelper createXMLHelper() {
		return new DualResourceXMIHelper(this);
	}

	@Override
	public EObject getEObject(String uriFragment) {
		EObject eObject = super.getEObject(uriFragment);
		if (eObject == null && globalResource != null) {
			// TODO this is where the object should be copied to this resource
			return globalResource.getEObject(uriFragment);
		}
		return eObject;
	}

	@Override
	public String getURIFragment(EObject eObject) {
		String uriFragment = super.getURIFragment(eObject);
		if (uriFragment == null && globalResource != null) {
			return globalResource.getURIFragment(eObject);
		}
		return uriFragment;
	}

}
