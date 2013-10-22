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
package org.flowerplatform.codesync.feature_converter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
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
public abstract class CodeSyncElementFeatureValueConverter {

	Map<String, EStructuralFeature> features;
	
	public CodeSyncElementFeatureValueConverter() {
		features = new HashMap<String, EStructuralFeature>();
		
		addFeature("name", CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		addFeature("isAdded", CodeSyncPackage.eINSTANCE.getCodeSyncElement_Added());
	}
	
	public void addFeature(String name, EStructuralFeature feature) {
		features.put(name, feature);
	}
	
	public EStructuralFeature getFeature(String name) {
		return features.get(name);
	}
	
	public Object getValue(CodeSyncElement codeSyncElement, String name) {
		EStructuralFeature feature = getFeature(name);
		if (feature != null) {
			return CodeSyncPlugin.getInstance()
					.getCodeSyncOperationsService().getFeatureValue(codeSyncElement, feature);
		}
		return null;
	}
	
	public void setValue(CodeSyncElement codeSyncElement, String name, Object newValue) {
		EStructuralFeature feature = getFeature(name);
		if (feature != null) {
			CodeSyncPlugin.getInstance()
					.getCodeSyncOperationsService().setFeatureValue(codeSyncElement, feature, newValue);
		}
	}
	
}
