package com.crispico.flower.mp.codesync.base;

public class ModelAdapterFactorySet {

	private ModelAdapterFactory ancestorFactory;

	private ModelAdapterFactory leftFactory;
	
	private ModelAdapterFactory rightFactory;

	public static ModelAdapterFactorySet tempInstance;
	
	public ModelAdapterFactory getAncestorFactory() {
		return ancestorFactory;
	}

	public ModelAdapterFactory getLeftFactory() {
		return leftFactory;
	}

	public ModelAdapterFactory getRightFactory() {
		return rightFactory;
	}

	public ModelAdapterFactorySet(ModelAdapterFactory ancestorModelAdapterFactory, ModelAdapterFactory leftModelAdapterFactory, ModelAdapterFactory rightModelAdapterFactory) {
		super();
		this.ancestorFactory = ancestorModelAdapterFactory;
		this.leftFactory = leftModelAdapterFactory;
		this.rightFactory = rightModelAdapterFactory;
	}
	
}
