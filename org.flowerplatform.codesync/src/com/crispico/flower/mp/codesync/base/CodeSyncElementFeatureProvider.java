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
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana
 */
public class CodeSyncElementFeatureProvider implements IFeatureProvider {

	@Override
	public List<?> getFeatures(Object element) {
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		features.add(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		features.add(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type());
		features.add(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children());
		return features;
	}

	@Override
	public int getFeatureType(Object feature) {
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		if (structuralFeature.isMany())
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		else 
			return IModelAdapter.FEATURE_TYPE_VALUE;
	}

	@Override
	public String getFeatureName(Object feature) {
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		return structuralFeature.getName();
	}

}