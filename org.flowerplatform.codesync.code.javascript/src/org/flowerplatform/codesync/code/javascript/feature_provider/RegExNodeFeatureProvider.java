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
package org.flowerplatform.codesync.code.javascript.feature_provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * Feature provider for {@link CodeSyncElement}s and {@link Node}s.
 * 
 * @author Mariana Gheorghe
 */
public class RegExNodeFeatureProvider extends CodeSyncElementFeatureProvider {

	@Override
	public List<?> getFeatures(Object element) {
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		features.add(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type());
		features.add(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children());
		features.add(RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters());
		return features;
	}

	@Override
	public int getFeatureType(Object feature) {
		if (RegExAstPackage.eINSTANCE.getRegExAstCacheElement_Parameters().equals(feature)) {
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		}
		return super.getFeatureType(feature);
	}

	@Override
	public String getFeatureName(Object feature) {
		if (feature instanceof String) {
			return (String) feature;
		}
		return super.getFeatureName(feature);
	}
	
}
