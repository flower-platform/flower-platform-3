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
 * @author Mariana Gheorghe
 */
public abstract class CodeSyncElementFeatureConverter {

	Map<String, EStructuralFeature> features;
	
	public CodeSyncElementFeatureConverter() {
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
	
	/**
	 * Finds the corresponding {@link EStructuralFeature} and delegates to
	 * {@link CodeSyncOperationsService}.
	 */
	public Object getValue(CodeSyncElement codeSyncElement, String name) {
		EStructuralFeature feature = getFeature(name);
		if (feature != null) {
			return CodeSyncPlugin.getInstance()
					.getCodeSyncOperationsService().getFeatureValue(codeSyncElement, feature);
		}
		return null;
	}
	
}
