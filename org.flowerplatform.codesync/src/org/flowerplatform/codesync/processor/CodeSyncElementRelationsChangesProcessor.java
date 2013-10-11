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
package org.flowerplatform.codesync.processor;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeKind;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.ListChange;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * Processes changes in the relations containment feature; adds/removes edge views.
 * 
 * @author Mariana Gheorghe
 */
public class CodeSyncElementRelationsChangesProcessor implements IDiagrammableElementFeatureChangesProcessor {

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		if (featureChanges == null) {
			// full content; do not add new edges for existing relations
		} else {
			for (FeatureChange featureChange : featureChanges) {
				if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Relations().equals(featureChange.getFeature())) {
					processRelationsChanges(object, featureChange, associatedViewOnOpenDiagram, context);
				}
			}
		}
	}

	protected void processRelationsChanges(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		if (featureChange.getListChanges().isEmpty()) {
			getService().addEdgesForRelations(object, ((CodeSyncElement) object).getRelations(), associatedViewOnOpenDiagram, false, context);
		} else {
			for (ListChange listChange : featureChange.getListChanges()) {
				if (listChange.getKind().equals(ChangeKind.ADD_LITERAL)) {
					// removed a relation
					removeEdgesForRelations(object, listChange.getValues(), associatedViewOnOpenDiagram, context);
				} else {
					if (listChange.getKind().equals(ChangeKind.REMOVE_LITERAL)) {
						// send the full list of relations for now, because we've noticed an issue with the indexes from the listChange
						getService().addEdgesForRelations(object, ((CodeSyncElement) object).getRelations(), associatedViewOnOpenDiagram, false, context);
					}
				}
			}
		}
	}
	
	/**
	 * Removes all {@link Edge}s for the deleted {@link Relation}s.
	 */
	protected void removeEdgesForRelations(EObject object, EList<Object> relations, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		Diagram diagram = getService().getDiagram(context);
		for (Object relation : relations) {
			for (View target : getService().getViewsForElement(((Relation) relation).getTarget())) {
				Edge edge = getService().getEdge(associatedViewOnOpenDiagram, target);
				if (edge != null) {
					associatedViewOnOpenDiagram.getSourceEdges().remove(edge);
					target.getTargetEdges().remove(edge);
					diagram.getPersistentEdges().remove(edge);
				}
			}
		}
	}
	
	private CodeSyncDiagramOperationsService getService() {
		return CodeSyncDiagramOperationsService.getInstance();
	}
	
}
