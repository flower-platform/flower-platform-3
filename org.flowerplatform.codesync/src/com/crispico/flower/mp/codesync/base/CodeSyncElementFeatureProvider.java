package com.crispico.flower.mp.codesync.base;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EStructuralFeature;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana
 */
public class CodeSyncElementFeatureProvider implements IFeatureProvider {

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
	public String getFeatureName(Object feature) {
		EStructuralFeature structuralFeature = (EStructuralFeature) feature;
		return structuralFeature.getName();
	}

}
