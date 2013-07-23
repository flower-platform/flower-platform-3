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

import java.util.Iterator;

import com.crispico.flower.mp.codesync.base.FilteredIterable;
import com.crispico.flower.mp.codesync.merge.SyncElementModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * @author Mariana
 */
public class CodeSyncElementModelAdapterAncestor extends CodeSyncElementModelAdapter {

	public CodeSyncElementModelAdapterAncestor() {
		super();
	}
	
	public CodeSyncElementModelAdapterAncestor(SyncElementModelAdapter modelAdapter) {
		super(modelAdapter);
	}

	/**
	 * Filters out added {@link CodeSyncElement}s. Returns the new containment list from the {@link FeatureChange}s map for
	 * the <code>feature</code>, if it exists. 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		// first get the children from the FeatureChange, if it exists
		Iterable<?> result;
		FeatureChange change = getFeatureChange(element, feature);
		if (change != null) {
			result = (Iterable<?>) change.getOldValue();
		} else {
			result = astCacheElementModelAdapter != null 
					? astCacheElementModelAdapter.getContainmentFeatureIterable(element, feature, correspondingIterable) 
					: super.getContainmentFeatureIterable(element, feature, correspondingIterable);
		}
		return new FilteredIterable<Object, Object>((Iterator<Object>) result.iterator()) {
			protected boolean isAccepted(Object candidate) {
				if (candidate instanceof CodeSyncElement && ((CodeSyncElement) candidate).isAdded())
					return false;
				return true;
			}
		
		};
	}

	/**
	 * Returns the old value from the {@link FeatureChange}s map for the <code>feature</code>, if it exists.
	 */
	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		// first get the value from the FeatureChange, if it exists
		FeatureChange change = getFeatureChange(element, feature);
		if (change != null) {
			return change.getOldValue();
		}
		return astCacheElementModelAdapter != null 
				? astCacheElementModelAdapter.getValueFeatureValue(element, feature, correspondingValue)
				: super.getValueFeatureValue(element, feature, correspondingValue);
	}
}