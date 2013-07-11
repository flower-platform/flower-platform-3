package com.crispico.flower.mp.codesync.code.java.featureprovider;

import java.util.List;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;

public class JavaEnumConstantDeclarationFeatureProvider extends CodeSyncElementFeatureProvider {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getFeatures(Object element) {
		List features = super.getFeatures(element);
		features.add(AstCacheCodePackage.eINSTANCE.getEnumConstant_Arguments());
		return features;
	}
}