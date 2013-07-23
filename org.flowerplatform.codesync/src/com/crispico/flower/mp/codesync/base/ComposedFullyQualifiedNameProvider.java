/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package com.crispico.flower.mp.codesync.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;


/**
 * @author Cristi
 */
public class ComposedFullyQualifiedNameProvider implements IFullyQualifiedNameProvider {

	protected Map<String, IFullyQualifiedNameProvider> fileExtensionBasedDelegateProviders = new HashMap<String, IFullyQualifiedNameProvider>();
	
	protected List<IFullyQualifiedNameProvider> delegateProviders = new ArrayList<IFullyQualifiedNameProvider>();
	
	public void addFileExtensionBasedDelegateProvider(String fileExtension, IFullyQualifiedNameProvider converter) {
		fileExtensionBasedDelegateProviders.put(fileExtension, converter);
	}
			
	public void addDelegateProvider(IFullyQualifiedNameProvider converter) {
		delegateProviders.add(converter);
	}
	
	@Override
	public String getFullyQualifiedName(Object object) {
		IFullyQualifiedNameProvider foundConverter = null;
		if (object instanceof IFile) {
			foundConverter = fileExtensionBasedDelegateProviders.get(((IFile) object).getFileExtension());
		}
		
		if (foundConverter != null) {
			return foundConverter.getFullyQualifiedName(object);
		} else {
			for (IFullyQualifiedNameProvider converter : delegateProviders) {
				String result = converter.getFullyQualifiedName(object);
				if (result != null) {
					return result;
				}
			}
		}
		
		return null;
	}

}