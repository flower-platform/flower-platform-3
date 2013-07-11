package com.crispico.flower.mp.codesync.wiki.featureprovider;

import java.util.List;

import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.codesync.base.CodeSyncElementFeatureProvider;

/**
 * @author Mariana
 */
public class WikiPageFeatureProvider extends CodeSyncElementFeatureProvider {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> getFeatures(Object element) {
		List features = super.getFeatures(element);
		features.add(WikiPackage.eINSTANCE.getPage_InitialContent());
		return features;
	}
	
}
