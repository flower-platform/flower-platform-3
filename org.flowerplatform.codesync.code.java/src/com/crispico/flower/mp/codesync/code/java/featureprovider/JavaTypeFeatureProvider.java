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
package com.crispico.flower.mp.codesync.code.java.featureprovider;

import java.util.List;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;

public class JavaTypeFeatureProvider extends CodeSyncElementFeatureProvider {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getFeatures(Object element) {
		List features = super.getFeatures(element);
		features.add(AstCacheCodePackage.eINSTANCE.getDocumentableElement_Documentation());
		features.add(AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
		features.add(AstCacheCodePackage.eINSTANCE.getClass_SuperClasses());
		features.add(AstCacheCodePackage.eINSTANCE.getClass_SuperInterfaces());
		return features;
	}

	@Override
	public int getFeatureType(Object feature) {
		if (AstCacheCodePackage.eINSTANCE.getClass_SuperClasses().equals(feature)) {
			return IModelAdapter.FEATURE_TYPE_VALUE;
		}
		return super.getFeatureType(feature);
	}
	
}