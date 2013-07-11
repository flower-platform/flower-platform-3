package com.crispico.flower.mp.codesync.code.java.featureprovider;

import java.util.Arrays;
import java.util.List;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;

public class JavaAnnotationFeatureProvider extends CodeSyncElementFeatureProvider {
	
	@Override
	public List<?> getFeatures(Object element) {
		return Arrays.asList(
			AstCacheCodePackage.eINSTANCE.getAnnotation_Name(),
			AstCacheCodePackage.eINSTANCE.getAnnotation_Values()
		);
	}
}