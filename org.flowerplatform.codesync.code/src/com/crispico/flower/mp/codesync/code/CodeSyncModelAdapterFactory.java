/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
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
			for (ModelAdapterEntry entry : modelAdapters) {
				String type = ((CodeSyncElement) modelElement).getType();
				if (type != null && type.equals(entry.type)) {
					return entry.modelAdapter;
				}
			}
		}
		return super.getModelAdapter(modelElement);
	}

	public ModelAdapterEntry addModelAdapter(String type, SyncElementModelAdapter modelAdapter) {
		ModelAdapterEntry e = new ModelAdapterEntry();
		e.type = type;
		e.modelAdapter = isLeft 
							? new CodeSyncElementModelAdapterLeft(createModelAdapter(modelAdapter)) 
							: new CodeSyncElementModelAdapterAncestor(createModelAdapter(modelAdapter));
		modelAdapters.add(e);
		return e;
	}
	
	public ModelAdapterEntry addModelAdapter(Class<?> clazz, SyncElementModelAdapter modelAdapter) {
		return super.addModelAdapter(clazz,
				isLeft 
					? createModelAdapter(modelAdapter)
					: createModelAdapter(modelAdapter));
	}
	
}