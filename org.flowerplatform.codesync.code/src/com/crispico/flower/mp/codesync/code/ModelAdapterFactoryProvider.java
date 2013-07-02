package com.crispico.flower.mp.codesync.code;

import java.util.HashMap;
import java.util.Map;

import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.merge.CodeSyncElementFeatureProvider;

/**
 * @author Mariana
 */
public class ModelAdapterFactoryProvider {

	private Map<String, ModelAdapterFactory> factories = new HashMap<String, ModelAdapterFactory>();
	
	private Map<String, CodeSyncElementFeatureProvider> featureProviders = new HashMap<String, CodeSyncElementFeatureProvider>();

	public Map<String, ModelAdapterFactory> getFactories() {
		return factories;
	}
	
	public Map<String, CodeSyncElementFeatureProvider> getFeatureProviders() {
		return featureProviders;
	}
	
}
