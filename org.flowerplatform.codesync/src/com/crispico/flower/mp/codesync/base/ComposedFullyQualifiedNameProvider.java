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
