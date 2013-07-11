package com.crispico.flower.mp.codesync.code.featureprovider;

import java.util.Collections;
import java.util.List;

import com.crispico.flower.mp.codesync.base.IFeatureProvider;

/**
 * @author Mariana
 */
public class StringFeatureProvider implements IFeatureProvider {

	@Override
	public List<?> getFeatures(Object element) {
		return Collections.emptyList();
	}

	@Override
	public int getFeatureType(Object feature) {
		throw new UnsupportedOperationException("String does not support features");
	}

	@Override
	public String getFeatureName(Object feature) {
		throw new UnsupportedOperationException("String does not support features");
	}
	
}
