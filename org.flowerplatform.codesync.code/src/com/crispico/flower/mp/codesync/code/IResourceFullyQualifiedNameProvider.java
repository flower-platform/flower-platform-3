package com.crispico.flower.mp.codesync.code;

import org.eclipse.core.resources.IResource;

import com.crispico.flower.mp.codesync.base.IFullyQualifiedNameProvider;

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
