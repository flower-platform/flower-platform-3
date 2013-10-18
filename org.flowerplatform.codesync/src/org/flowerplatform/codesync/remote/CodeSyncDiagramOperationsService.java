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
package org.flowerplatform.codesync.remote;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.codesync.processor.CodeSyncElementRelationsChangesProcessor;
import org.flowerplatform.communication.service.ServiceInvocationContext;
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
 * @author Mariana Gheorghe
 */
public abstract class CodeSyncDiagramOperationsService {

	protected static CodeSyncDiagramOperationsService INSTANCE;
	
	public static CodeSyncDiagramOperationsService getInstance() {
		return INSTANCE;
	}
	
	public CodeSyncDiagramOperationsService() {
		INSTANCE = this;
	}
	
	public void displayMissingRelations(ServiceInvocationContext context, String viewId, boolean addMissingElements) {
		CodeSyncElement cse = (CodeSyncElement) getViewById(context, viewId).getDiagrammableElement();
		List<View> views = getViewsForElement(cse);
		
		// add edges for relations where this element is the source
		for (View view : views) {
			addEdgesForRelations(cse, cse.getRelations(), view, addMissingElements, context.getAdditionalData());
		}
		
		// add edges for relations where this element is the target
		addEdgesForRelationsWithTarget(cse, views, addMissingElements, context.getAdditionalData());
	}
	
	/**
	 * Adds new {@link Edge}s for each {@link Relation}.
	 */
	public void addEdgesForRelations(EObject object, List<Relation> relations, View associatedViewOnOpenDiagram, boolean addMissingElements, Map<String, Object> context) {
		Diagram diagram = getDiagram(context);
		for (Relation relation : relations) {
			List<View> views = getViewsForElement(relation.getTarget());
			// if there are no views for the target and addMissingElements is true
			if (addMissingElements && views.size() == 0) {
				createViewForElement(relation.getTarget(), diagram);
			} else {
				for (View target : views) {
					if (getEdge(associatedViewOnOpenDiagram, target) == null) {
						createEdge(relation, associatedViewOnOpenDiagram, target, diagram);
					}
				}
			}
		}
	}
	
	public void addEdgesForRelationsWithTarget(EObject object, List<View> targetViews, boolean addMissingElements, Map<String, Object> context) {
		CodeSyncElement cse = (CodeSyncElement) object;
		Diagram diagram = getDiagram(context);
		for (EObject eObject : getInverseReferencesForElement(cse, CodeSyncPackage.eINSTANCE.getRelation_Target())) {
			Relation relation = (Relation) eObject;
			List<View> sourceViews = getViewsForElement(relation.getSource());
			if (addMissingElements && sourceViews.size() == 0) {
				createViewForElement(relation.getSource(), diagram);
			} else {
				for (View sourceView : sourceViews) {
					for (View targetView : targetViews) {
						if (getEdge(sourceView, targetView) == null) {
							createEdge(relation, sourceView, targetView, diagram);
						}
					}
				}
			}
		}
	}
	
	protected abstract void createViewForElement(CodeSyncElement target, Diagram diagram);

	protected Edge createEdge(Relation relation, View source, View target, Diagram diagram) {
		Edge edge = NotationFactory.eINSTANCE.createEdge();
		edge.setDiagrammableElement(relation);
		edge.setSource(source);
		edge.setTarget(target);
		edge.setViewType("edge");
		diagram.getPersistentEdges().add(edge);
		return edge;
	}
	
	/**
	 * Gets the {@link Edge} with the specified <code>source</code> and <code>target</code>,
	 * if it exists.
	 */
	public Edge getEdge(View source, View target) {
		for (Edge edge : source.getSourceEdges()) {
			if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
				return edge;
			}
		}
		return null;
	}
	
	/**
	 * Some views (e.g. class title) cannot have edges even though their diagrammable element can have relations.
	 */
	protected boolean acceptsEdges(View view) {
		List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance()
				.getDiagramUpdaterChangeProcessor()
				.getDiagrammableElementFeatureChangesProcessors(view.getViewType());
		for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
			if (processor instanceof CodeSyncElementRelationsChangesProcessor) {
				return true;
			}
		}
		return false;
	}
	
	public Diagram getDiagram(Map<String, Object> context) {
		DiagramEditableResource der = (DiagramEditableResource) context.get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
		for (EObject eObject : der.getMainResource().getContents()) {
			if (eObject instanceof Diagram) {
				return (Diagram) eObject;
			}
		}
		throw new RuntimeException("No diagram for " + der.getEditableResourcePath());
	}
	
	/**
	 * Finds all the {@link View}s for <code>element</code> and filters out
	 * elements that cannot accept edges.
	 */
	public List<View> getViewsForElement(CodeSyncElement element) {
		List<View> views = (List<View>) getInverseReferencesForElement(element,
				NotationPackage.eINSTANCE.getView_DiagrammableElement());
		// filter out views that can't have edges
		for (Iterator<View> iterator = views.iterator(); iterator.hasNext();) {
			View view = (View) iterator.next();
			if (!acceptsEdges(view)) {
				iterator.remove();
			}
		}
		return views;
	}
	
	protected List<? extends EObject> getInverseReferencesForElement(CodeSyncElement element, EStructuralFeature feature) {
		List<EObject> result = new ArrayList<EObject>();
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(element);
		for (Setting setting : adapter.getNonNavigableInverseReferences(element)) {
			if (feature.equals(setting.getEStructuralFeature())) {
				result.add(setting.getEObject());
			}
		}
		return result;
	}
	
	///////////////////////
	// Utils
	///////////////////////
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
	return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected View getViewById(ServiceInvocationContext context, String viewId) {
	return (View) getEditableResource(context).getEObjectById(viewId);
	}
	
}