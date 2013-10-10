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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.NotationPackage;
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
			// full content
			addEdgesForRelations(object, ((CodeSyncElement) object).getRelations(), associatedViewOnOpenDiagram, context);
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
			addEdgesForRelations(object, ((CodeSyncElement) object).getRelations(), associatedViewOnOpenDiagram, context);
		}
	}
	
	/**
	 * Adds new {@link Edge}s for each {@link Relation}.
	 */
	protected void addEdgesForRelations(EObject object, List<Relation> relations, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		Diagram diagram = getDiagram(context);
		for (Relation relation : relations) {
			for (EObject eObject : getInverseReferencesForElement(relation.getTarget(), NotationPackage.eINSTANCE.getView_DiagrammableElement())) {
				View target = (View) eObject;
				if (acceptsEdges(target) && !edgeExists(associatedViewOnOpenDiagram, target)) {
					Edge edge = NotationFactory.eINSTANCE.createEdge();
					edge.setDiagrammableElement(relation);
					edge.setSource(associatedViewOnOpenDiagram);
					edge.setTarget(target);
					edge.setViewType("edge");
					diagram.getPersistentEdges().add(edge);
				}
			}
		}
	}
	
	/**
	 * Prevent adding duplicate edges for an existing relation.
	 */
	protected boolean edgeExists(View source, View target) {
		for (Edge edge : source.getSourceEdges()) {
			if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Some views (e.g. class title) cannot have edges even though their diagrammable element can have relations.
	 */
	protected boolean acceptsEdges(View view) {
		return EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor()
				.getDiagrammableElementFeatureChangesProcessors(view.getViewType()).contains(this);
	}
	
	protected List<EObject> getInverseReferencesForElement(CodeSyncElement element, EStructuralFeature feature) {
		List<EObject> result = new ArrayList<EObject>();
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(element);
		for (Setting setting : adapter.getNonNavigableInverseReferences(element)) {
			if (feature.equals(setting.getEStructuralFeature())) {
				result.add(setting.getEObject());
			}
		}
		return result;
	}
	
	protected Diagram getDiagram(Map<String, Object> context) {
		DiagramEditableResource der = (DiagramEditableResource) context.get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
		for (EObject eObject : der.getMainResource().getContents()) {
			if (eObject instanceof Diagram) {
				return (Diagram) eObject;
			}
		}
		throw new RuntimeException("No diagram for " + der.getEditableResourcePath());
	}
	
}
