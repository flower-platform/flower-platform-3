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

import com.crispico.flower.mp.codesync.base.IFeatureProvider;
import com.crispico.flower.mp.codesync.base.IModelAdapter;

/**
 * Feature provider for {@link Parameter}s.
 * 
 * @author Mariana Gheorghe
 */
public class RegExParameterFeatureProvider implements IFeatureProvider {

	@Override
	public List<?> getFeatures(Object element) {
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		features.add(RegExAstPackage.eINSTANCE.getRegExAstNodeParameter_Name());
		features.add(RegExAstPackage.eINSTANCE.getRegExAstNodeParameter_Value());
		return features;
	}

	@Override
	public int getFeatureType(Object feature) {
		return IModelAdapter.FEATURE_TYPE_VALUE;
	}

	@Override
	public String getFeatureName(Object feature) {
		return ((EStructuralFeature) feature).getName();
	}

}
