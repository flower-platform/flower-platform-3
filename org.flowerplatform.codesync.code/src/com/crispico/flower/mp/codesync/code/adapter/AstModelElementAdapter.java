package com.crispico.flower.mp.codesync.code.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com.crispico.flower.mp.codesync.base.IModelAdapter;
import com.crispico.flower.mp.codesync.base.action.ActionResult;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @flowerModelElementId _j9MV0LLCEeC7r409C3qpqw
 */
public abstract class AstModelElementAdapter implements IModelAdapter {

	public static final String FOLDER = "Folder";
	
	public static final String FILE = "File";
	
	@Override
	public String getFeatureName(Object feature) {
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		return structuralFeature.getName();
	}

	@Override
	public List<?> getFeatures(Object element) {
		List<EStructuralFeature> features = new ArrayList<EStructuralFeature>();
		features.add(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name());
		features.add(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Type());
		features.add(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children());
		return features;
	}

	@Override
	public int getFeatureType(Object feature) {
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		if (structuralFeature.isMany())
			return IModelAdapter.FEATURE_TYPE_CONTAINMENT;
		else 
			return IModelAdapter.FEATURE_TYPE_VALUE;
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
