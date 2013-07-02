package com.crispico.flower.mp.codesync.code;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.ModelAdapterFactory;
import com.crispico.flower.mp.codesync.base.action.ActionResult;

import static com.crispico.flower.mp.codesync.base.CodeSyncAlgorithm.UNDEFINED;

/**
 * @author Mariana
 */
public class StringModelAdapter implements IModelAdapter {
	
	private ModelAdapterFactory factory;

	public StringModelAdapter(ModelAdapterFactory factory) {
		this.factory = factory;
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
	public List<?> getFeatures(Object element) {
		return Collections.emptyList();
	}

	@Override
	public int getFeatureType(Object feature) {
		throw new UnsupportedOperationException("String does not have feature " + feature);
	}

	@Override
	public String getFeatureName(Object feature) {
		throw new UnsupportedOperationException("String does not have feature " + feature);
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
		factory.getModelAdapter(parent).removeChildrenOnContainmentFeature(parent, feature, child);
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
