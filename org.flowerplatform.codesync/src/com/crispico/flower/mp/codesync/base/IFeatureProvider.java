package com.crispico.flower.mp.codesync.base;

import java.util.List;

/**
 * @author Mariana
 */
public interface IFeatureProvider {

	public List<?> getFeatures(Object element);

	public int getFeatureType(Object feature);
	
	public String getFeatureName(Object feature);
	
}
