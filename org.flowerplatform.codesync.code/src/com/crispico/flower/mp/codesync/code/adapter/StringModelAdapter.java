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
package com.crispico.flower.mp.codesync.code.adapter;

import static com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm.UNDEFINED;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.crispico.flower.mp.codesync.base.AbstractModelAdapter;
import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactorySet;
import com.crispico.flower.mp.codesync.base.action.ActionResult;

/**
 * @author Mariana
 */
public class StringModelAdapter extends AbstractModelAdapter {
	
	private ModelAdapterFactorySet modelAdapterFactorySet;
	
	@Override
	public ModelAdapterFactorySet getModelAdapterFactorySet() {
		return modelAdapterFactorySet;
	}
	
	@Override
	public IModelAdapter setModelAdapterFactorySet(ModelAdapterFactorySet modelAdapterFactorySet) {
		this.modelAdapterFactorySet = modelAdapterFactorySet;
		return this;
	}
	
	@Override
	public boolean hasChildren(Object modelElement) {
		return false;
	}

	@Override
	public List<?> getChildren(Object modelElement) {
		return Collections.emptyList();
	}

	@Override
	public String getLabel(Object modelElement) {
		return (String) modelElement;
	}

	@Override
	public List<String> getIconUrls(Object modelElement) {
		return null;
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		if (UNDEFINED.equals(element)) {
			return Collections.singletonList(UNDEFINED);
		}
		throw new UnsupportedOperationException("String does not have children for feature " + feature);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		if (UNDEFINED.equals(element)) {
			return UNDEFINED;
		}
		throw new UnsupportedOperationException("String does not have feature " + feature);
	}

	@Override
	public Object getMatchKey(Object element) {
		return element;
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map) {
		map.put(getMatchKey(element), element);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		if (UNDEFINED.equals(element)) {
			if (leftOrRightMap.isEmpty() || leftOrRightMap.containsKey(UNDEFINED)) {
				return UNDEFINED;
			} else {
				return leftOrRightMap.remove(leftOrRightMap.keySet().iterator().next());
			}
		}
		return leftOrRightMap.remove(getMatchKey(element));
	}

	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value) {
		throw new UnsupportedOperationException("String does not have feature " + feature);
	}

	@Override
	public Object createChildOnContainmentFeature(Object element, Object feature, Object correspondingChild) {
		throw new UnsupportedOperationException("String does not have children for feature " + feature);
	}

	@Override
	public void removeChildrenOnContainmentFeature(Object parent, Object feature, Object child) {
		throw new UnsupportedOperationException("String does not have children for feature " + feature);
	}

	@Override
	public Object createCorrespondingModelElement(Object element) {
		return element;
	}

	@Override
	public boolean save(Object element) {
		return false;
	}
	
	@Override
	public boolean discard(Object element) {
		return false;
	}

	@Override
	public void actionPerformed(Object element, Object feature, ActionResult result) {
		// nothing to do
	}

	@Override
	public void allActionsPerformedForFeature(Object element, Object correspondingElement, Object feature) {
		// nothing to do
	}

	@Override
	public void allActionsPerformed(Object element, Object correspondingElement) {
		// nothing to do
	}

	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		// nothing to do
	}
	
	@Override
	public void featuresProcessed(Object element) {
		// nothing to do
	}

}