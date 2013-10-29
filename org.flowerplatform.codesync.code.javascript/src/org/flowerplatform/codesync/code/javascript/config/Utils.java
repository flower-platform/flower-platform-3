package org.flowerplatform.codesync.code.javascript.config;

import java.util.List;

import org.flowerplatform.codesync.remote.CodeSyncOperationsService;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

public class Utils {
	
	/**
	 * @param ignoreTypes - creates path without adding the {@link CodeSyncElement}s with type in this list
	 * @param ignoreInitialElement - doesn't add initial element to path
	 * 
	 * @author Cristian Spiescu
	 * @author Cristina Constantinescu
	 */
	public static String getQualifiedPath(CodeSyncElement element, boolean ignoreInitialElement, List<String> ignoreTypes) {
		if (ignoreInitialElement) {
			return getQualifiedPath(element.eContainer() instanceof CodeSyncElement ? (CodeSyncElement) element.eContainer() : null, false, ignoreTypes);
		}
		StringBuffer sb = new StringBuffer();
		while (element != null) {			
			if (element instanceof CodeSyncRoot) {
				break;
			}				
			if (ignoreTypes != null && ignoreTypes.contains(element.getType())) {				
			} else {
				if (sb.length() != 0) {
					sb.insert(0, '/');
				}	
				String currentName = (String) CodeSyncOperationsService.getInstance().getKeyFeatureValue(element);
				sb.insert(0, currentName);
			}
			element = element.eContainer() instanceof CodeSyncElement ? (CodeSyncElement) element.eContainer() : null;
		}
	
		return sb.toString();
	}

}
