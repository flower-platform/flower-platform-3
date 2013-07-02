package com.crispico.flower.mp.codesync.merge;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;

public class MergeModelAdapterFactorySetFactory {

	public static ModelAdapterFactorySet createModelAdapterFactorySet() {
		ModelAdapterFactory factory = new ModelAdapterFactory();
		factory.addModelAdapter(EObject.class, new EObjectModelAdapter());
		factory.addModelAdapter(Resource.class, new FlowerEditingDomainModelAdapter());
		return new ModelAdapterFactorySet(factory, factory, factory);
	}
	
}
