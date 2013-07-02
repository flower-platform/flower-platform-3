package com.crispico.flower.mp.codesync.code;

import org.eclipse.core.resources.IResource;

/**
 * @author Mariana
 */
public class IResourceFullyQualifiedNameProvider implements IFullyQualifiedNameProvider {

	@Override
	public String getFullyQualifiedName(Object object) {
		if (object instanceof IResource) {
			return ((IResource) object).getFullPath().toString();
		}
		return null;
	}

}
