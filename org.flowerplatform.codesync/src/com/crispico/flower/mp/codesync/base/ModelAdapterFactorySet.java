package com.crispico.flower.mp.codesync.base;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.resource.Resource;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

public /* abstract */ class ModelAdapterFactorySet {

	protected ModelAdapterFactory ancestorFactory;

	protected ModelAdapterFactory leftFactory;
	
	protected ModelAdapterFactory rightFactory;

	protected Map<Object, IFeatureProvider> featureProviders = new HashMap<Object, IFeatureProvider>();
	
	public ModelAdapterFactory getAncestorFactory() {
		return ancestorFactory;
	}

	public ModelAdapterFactory getLeftFactory() {
		return leftFactory;
	}

	public ModelAdapterFactory getRightFactory() {
		return rightFactory;
	}

	public IFeatureProvider getFeatureProvider(Object element) {
		if (element instanceof CodeSyncElement) {
			IFeatureProvider featureProvider = featureProviders.get(((CodeSyncElement) element).getType());
			if (featureProvider != null) {
				return featureProvider;
			}
		}
		for (Object key : featureProviders.keySet()) {
			if (key instanceof Class) {
				if (((Class) key).isAssignableFrom(element.getClass())) {
					IFeatureProvider featureProvider = featureProviders.get(key);
					if (featureProvider != null) {
						return featureProvider;
					}
				}
			}
		}
		throw new IllegalArgumentException("Cannot find feature provider for " + element);
	}
	
	public ModelAdapterFactorySet addFeatureProvider(Object key, IFeatureProvider provider) {
		featureProviders.put(key, provider);
		return this;
	}
	
	/* abstract */ public void initialize(Resource cache) {
		throw new UnsupportedOperationException("Must be implemented!");
	}
	
	
	public ModelAdapterFactorySet() {
		
	}
	
	public ModelAdapterFactorySet(ModelAdapterFactory ancestorModelAdapterFactory, ModelAdapterFactory leftModelAdapterFactory, ModelAdapterFactory rightModelAdapterFactory) {
		super();
		this.ancestorFactory = ancestorModelAdapterFactory;
		this.leftFactory = leftModelAdapterFactory;
		this.rightFactory = rightModelAdapterFactory;
	}
	
}
