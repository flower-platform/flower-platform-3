package org.flowerplatform.editor.model.changes_processor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.common.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristi
 */
public class Changes {

	private static final Logger logger = LoggerFactory.getLogger(Changes.class);
	
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
	
	public EObject getAddedToContainer() {
		if (addedTo == null) {
			return null;
		}
		
		for (Pair<EObject, EReference> pair : addedTo) {
			if (pair.b.isContainment()) {
				return pair.a;
			}
		}
		
		return null;
	}
	
	public void addAndLogAddedTo(EObject who, EObject where, EStructuralFeature reference) {
		if (logger.isTraceEnabled()) {
			logger.trace("Pass1: {} was added to {} on feature {}", new Object[] { who, where, reference} );
		}
		if (addedTo == null) {
			addedTo = new ArrayList<Pair<EObject, EReference>>();
		}
		addedTo.add(new Pair<EObject, EReference>(where, (EReference) reference));
	}
	
	public List<Pair<EObject, EReference>> getRemovedFrom() {
		return removedFrom;
	}
	
	public EObject getRemovedFromContainer() {
		if (removedFrom == null) {
			return null;
		}
		
		for (Pair<EObject, EReference> pair : removedFrom) {
			if (pair.b.isContainment()) {
				return pair.a;
			}
		}
		
		return null;
	}
	
	public void addAndLogRemovedFrom(EObject who, EObject where, EStructuralFeature reference) {
		if (logger.isTraceEnabled()) {
			logger.trace("Pass1: {} was removed from {} on feature {}", new Object[] { who, where, reference} );
		}
		if (removedFrom == null) {
			removedFrom = new ArrayList<Pair<EObject, EReference>>();
		}
		removedFrom.add(new Pair<EObject, EReference>(where, (EReference) reference));
	}
	
	public static String printFeatureChanges(List<FeatureChange> list) {
		if (list == null) {
			return "null";
		}
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
