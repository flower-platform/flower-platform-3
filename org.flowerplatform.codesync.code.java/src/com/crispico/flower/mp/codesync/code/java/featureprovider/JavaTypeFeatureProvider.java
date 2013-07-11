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
