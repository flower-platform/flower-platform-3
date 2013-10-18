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
package org.flowerplatform.editor.model.change_processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.change.ChangeKind;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.ListChange;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Mariana Gheorghe
 */
public class AbstractFeatureChangesProcessor implements IDiagrammableElementFeatureChangesProcessor {

	protected List<DependentFeature> dependentFeatures = new ArrayList<DependentFeature>();
	
	public List<DependentFeature> getDependentFeatures() {
		return dependentFeatures;
	}

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges,
			View associatedViewOnOpenDiagram, Map<String, Object> context) {
		if (featureChanges == null) {
			// full content
		} else {
			for (FeatureChange featureChange : featureChanges) {
				if (!featureChange.getListChanges().isEmpty()) {
					for (ListChange listChange : featureChange.getListChanges()) {
						if (listChange.getKind().equals(ChangeKind.ADD_LITERAL)) {
							// delete event
							for (Object deletedChild : listChange.getValues()) {
								onDelete(object, featureChange.getFeature(), (EObject) deletedChild);
							}
						}
					}
				}
			}
		}
	}

	protected void onDelete(EObject parent, EStructuralFeature childrenFeature, EObject deletedChild) {
		for (DependentFeature dependentFeature : dependentFeatures) {
			if (dependentFeature.getKey().isAssignableFrom(deletedChild.getClass())) {
				onDeleteElementRemoveInverseReferences((EObject) deletedChild, dependentFeature.getFeature());
			}
		}
		// recurse for contents
		for (Iterator it = deletedChild.eAllContents(); it.hasNext();) {
			EObject object = (EObject) it.next();
			onDelete(deletedChild, object.eContainingFeature(), object);
		}
	}

	protected void onDeleteElementRemoveInverseReferences(EObject element, EStructuralFeature feature) {
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(element);
		for (Setting setting : adapter.getNonNavigableInverseReferences(element)) {
			if (feature.equals(setting.getEStructuralFeature())) {
				EObject reference = setting.getEObject();
				removeFromContainer(reference);
			}
		}
	}

	private void removeFromContainer(EObject object) {
		EStructuralFeature containingFeature = object.eContainingFeature();
		EObject container = object.eContainer();
		if (container != null) {
			if (containingFeature.isMany()) {
				((List<EObject>) container.eGet(containingFeature)).remove(object);
			} else {
				container.eUnset(containingFeature);
			}
			// recurse (because the change description may not be processed for this operation)
			onDelete(container, containingFeature, object);
		}
	}
	
}
