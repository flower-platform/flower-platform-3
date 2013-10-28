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
package org.flowerplatform.codesync.config.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Converts from a {@code name} to the corresponding {@link EStructuralFeature}, 
 * as set in the {@link #features} map, and delegates to {@link CodeSyncOperationsService}.
 * 
 * <p>
 * Extending converters may override if the meaning of {@code name} is not identical
 * with the meaning of the feature (e.g. the value of the feature named {@code visibility}
 * should be computed from the list of modifiers).
 * 
 * @author Mariana Gheorghe
 */
public abstract class FeatureAccessExtension {

	protected List<String> codeSyncTypes;
	
	protected Map<String, EStructuralFeature> features;
	
	public static final String CODE_SYNC_NAME = "codeSyncName";
	
	public FeatureAccessExtension() {
		codeSyncTypes = new ArrayList<String>();
		features = new HashMap<String, EStructuralFeature>();
		
		addFeature(CODE_SYNC_NAME, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
	}
	
	public boolean hasCodeSyncType(String codeSyncType) {
		return codeSyncTypes.contains(codeSyncType);
	}
	
	public Object getValue(CodeSyncElement codeSyncElement, String featureName) {
		EStructuralFeature feature = getFeature(featureName);
		if (feature != null) {
			return CodeSyncOperationsService.getInstance().getFeatureValue(codeSyncElement, feature);
		}
		return null;
	}
	
	public void setValue(CodeSyncElement codeSyncElement, String featureName, Object newValue) {
		EStructuralFeature feature = getFeature(featureName);
		if (feature != null) {
			CodeSyncOperationsService.getInstance().setFeatureValue(codeSyncElement, feature, newValue);
		}
	}
	
	protected void addCodeSyncType(String codeSyncType) {
		codeSyncTypes.add(codeSyncType);
	}
	
	protected EStructuralFeature getFeature(String featureName) {
		return features.get(featureName);
	}
	
	protected void addFeature(String featureName, EStructuralFeature feature) {
		features.put(featureName, feature);
	}
	
}
