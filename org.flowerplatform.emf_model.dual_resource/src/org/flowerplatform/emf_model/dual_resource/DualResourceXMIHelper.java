package org.flowerplatform.emf_model.dual_resource;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIHelperImpl;

public class DualResourceXMIHelper extends XMIHelperImpl {

	public DualResourceXMIHelper(XMLResource resource) {
		super(resource);
	}

	@Override
	protected URI getHREF(Resource otherResource, EObject obj) {
		URI uri = otherResource.getURI();
		if (DualResourceURIHandler.isDualResourceURI(uri)) {
			uri = DualResourceURIHandler.removeBasePathFromURI(uri); 
		}
		String fragment = getURIFragment(otherResource, obj);
		return uri.appendFragment(fragment);
	}

}
