package com.crispico.flower.mp.codesync.code;

import java.util.Iterator;

import com.crispico.flower.mp.codesync.base.FilteredIterable;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * @author Mariana
 */
public class CodeSyncElementModelAdapterAncestor extends CodeSyncElementModelAdapter {

	/**
	 * Filters out added {@link CodeSyncElement}s. Returns the new containment list from the {@link FeatureChange}s map for
	 * the <code>feature</code>, if it exists. 
	 */
	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		// first get the children from the FeatureChange, if it exists
		Iterable<?> result;
		FeatureChange change = getFeatureChange(element, feature);
		if (change != null) {
			result = (Iterable<?>) change.getOldValue();
		} else {
			result = super.getContainmentFeatureIterable(element, feature, correspondingIterable);
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
		return super.getValueFeatureValue(element, feature, correspondingValue);
	}
}
