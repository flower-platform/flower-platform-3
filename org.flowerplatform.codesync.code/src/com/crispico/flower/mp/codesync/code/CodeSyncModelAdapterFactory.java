package com.crispico.flower.mp.codesync.code;

import org.eclipse.emf.ecore.resource.Resource;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.code.adapter.CodeSyncElementModelAdapterAncestor;
import com.crispico.flower.mp.codesync.code.adapter.CodeSyncElementModelAdapterLeft;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana
 */
public class CodeSyncModelAdapterFactory extends ModelAdapterFactory {

	private ModelAdapterFactorySet factorySet;
	private ModelAdapterFactory astModelAdapterFactory;
	private Resource resource;
	private boolean isLeft;
	
	public CodeSyncModelAdapterFactory(ModelAdapterFactorySet factorySet, ModelAdapterFactory astModelAdapterFactory, Resource resource, boolean isLeft) {
		this.factorySet = factorySet;
		this.astModelAdapterFactory = astModelAdapterFactory;
		this.resource = resource;
		this.isLeft = isLeft;
	}
	
	private SyncElementModelAdapter createModelAdapter(SyncElementModelAdapter adapter) {
		return (SyncElementModelAdapter) adapter
				.setModelAdapterFactory(this)
				.setEObjectConverter(astModelAdapterFactory)
				.setResource(resource)
				.setModelAdapterFactorySet(factorySet);
	}
	
	@Override
	public IModelAdapter getModelAdapter(Object modelElement) {
		if (modelElement instanceof CodeSyncElement) {
			ModelAdapterEntry entry = modelAdapters.get(((CodeSyncElement) modelElement).getType());
			if (entry != null) {
				return entry.modelAdapter;
			}
		}
		return super.getModelAdapter(modelElement);
	}

	public ModelAdapterEntry addModelAdapter(String type, SyncElementModelAdapter modelAdapter) {
		ModelAdapterEntry e = new ModelAdapterEntry();
		e.modelAdapter = isLeft 
							? new CodeSyncElementModelAdapterLeft(createModelAdapter(modelAdapter)) 
							: new CodeSyncElementModelAdapterAncestor(createModelAdapter(modelAdapter));
		modelAdapters.put(type, e);
		return e;
	}
	
	public ModelAdapterEntry addModelAdapter(Class<?> clazz, SyncElementModelAdapter modelAdapter) {
		return super.addModelAdapter(clazz,
				isLeft 
					? createModelAdapter(modelAdapter)
					: createModelAdapter(modelAdapter));
	}
	
}
