package com.crispico.flower.mp.codesync.code;

import java.util.List;
import java.util.Map;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.action.ActionResult;

/**
 * @flowerModelElementId _j9MV0LLCEeC7r409C3qpqw
 */
public abstract class AstModelElementAdapter implements IModelAdapter {

	public static final String FOLDER = "Folder";
	
	public static final String FILE = "File";
	
	protected CodeSyncElementFeatureProvider common;
	
	public void setFeatureProvider(CodeSyncElementFeatureProvider common) {
		this.common = common;
	}

	@Override
	public String getFeatureName(Object feature) {
		return common.getFeatureName(feature);
	}

	@Override
	public List<?> getFeatures(Object element) {
		return common.getFeatures(element);
	}

	@Override
	public int getFeatureType(Object feature) {
		return common.getFeatureType(feature);
	}

	@Override
	public void addToMap(Object element, Map<Object, Object> map) {
		map.put(getMatchKey(element), element);
	}

	@Override
	public Object removeFromMap(Object element, Map<Object, Object> leftOrRightMap, boolean isRight) {
		throw new UnsupportedOperationException("AstModelElementAdapter.removeFromMap() attempted.");
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue) {
		throw new IllegalArgumentException("Attempted to acces value feature " + getFeatureName(feature) + " for element " + element);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable) {
		throw new IllegalArgumentException("Attempted to acces containment feature " + getFeatureName(feature) + " for element " + element);
	}
	
	@Override
	public void beforeFeaturesProcessed(Object element, Object correspondingElement) {
		// nothing to do
	}
	
	@Override
	public void featuresProcessed(Object element) {
		// nothing to do
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
		for (Object feature : getFeatures(element)) {
			if (getFeatureType(feature) == FEATURE_TYPE_CONTAINMENT) {
				allActionsPerformedForFeature(element, correspondingElement, feature);
			}
		}
	}
	
}
