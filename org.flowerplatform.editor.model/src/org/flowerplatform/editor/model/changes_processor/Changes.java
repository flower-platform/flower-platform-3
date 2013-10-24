package org.flowerplatform.editor.model.changes_processor;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.common.util.Pair;

public class Changes {
	private List<FeatureChange> featureChanges;
	private List<Pair<EObject, EReference>> addedTo;
	private List<Pair<EObject, EReference>> removedFrom;
	
	public List<FeatureChange> getFeatureChanges() {
		return featureChanges;
	}
	
	public void setFeatureChanges(List<FeatureChange> featureChanges) {
		this.featureChanges = featureChanges;
	}
	
	public List<Pair<EObject, EReference>> getAddedTo() {
		return addedTo;
	}
	
	public void setAddedTo(List<Pair<EObject, EReference>> addedTo) {
		this.addedTo = addedTo;
	}
	
	public List<Pair<EObject, EReference>> getRemovedFrom() {
		return removedFrom;
	}
	
	public void setRemovedFrom(List<Pair<EObject, EReference>> removedFrom) {
		this.removedFrom = removedFrom;
	}
	
	public static String printFeatureChanges(List<FeatureChange> list) {
		StringBuffer sb = new StringBuffer("[");
		for (FeatureChange fc : list) {
			sb.append("(");
			sb.append(fc.getFeatureName()); 
			sb.append("),");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public String toString() {
		return "{featureChanges:" + printFeatureChanges(featureChanges) +
				", addedTo:" + addedTo + ", removedFrom:" + removedFrom + "}";
	}
	
}
