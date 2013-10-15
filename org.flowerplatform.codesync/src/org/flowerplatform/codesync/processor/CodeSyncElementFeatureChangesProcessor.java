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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public abstract class CodeSyncElementFeatureChangesProcessor implements IDiagrammableElementFeatureChangesProcessor {

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		if (object == null) {
			return;
		}		
		if (featureChanges == null) {
			// full content
			processFeatureChange(object, null, associatedViewOnOpenDiagram, context);
		} else {
			for (FeatureChange featureChange : featureChanges) {
				processFeatureChange(object, featureChange, associatedViewOnOpenDiagram, context);
			}
		}
	}
	
	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		if (featureChange == null) {
			processChildren(object, getChildrenForCodeSyncElement(object), associatedViewOnOpenDiagram, getChildrenForView(associatedViewOnOpenDiagram), context);
		} else {
			if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(featureChange.getFeature())) {
				processChildren(object, getChildrenForCodeSyncElement(object), associatedViewOnOpenDiagram, getChildrenForView(associatedViewOnOpenDiagram), context);
			}
		}
	}

	protected void processChildren(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram, List<Node> childViews, Map<String, Object> context) {
		int newViewsIndex = getNewViewsIndex(object, childModelElements, associatedViewOnOpenDiagram);
		
		if (newViewsIndex >= 0) {
			// add a view for each child of the model element (cases: initial, or adding a new child model element)
			for (EObject child : childModelElements) {
				if (canAddChildView(associatedViewOnOpenDiagram, child)) {
					Node newView = createChildView(associatedViewOnOpenDiagram, child);
					newView.setDiagrammableElement(child);
					associatedViewOnOpenDiagram.getPersistentChildren().add(newViewsIndex, newView);
				}
				newViewsIndex++;
			}
		}
		
		// add a model element for each view (cases: add a new view on client side)
		CopyOnWriteArrayList<Node> copyList = new CopyOnWriteArrayList<Node>(childViews);
		for (Iterator<Node> it = copyList.iterator(); it.hasNext();) {
			View child = it.next();
			if (!containsModelElementForChildView(object, child)) {
				CodeSyncElement newChild = createModelElementChild(object, child);
				if (newChild != null) {
					getChildrenForCodeSyncElement(object).add(associatedViewOnOpenDiagram.getPersistentChildren().indexOf(child), newChild);
				} else {
//					associatedViewOnOpenDiagram.getPersistentChildren().remove(child);
				}
			}
		}
		
		if (newViewsIndex < 0) {
			// remove all views (cases: collapsed view/compartment)
			associatedViewOnOpenDiagram.getPersistentChildren().removeAll(childViews);
		}
	}
	
	protected CodeSyncElement getCodeSyncElement(EObject object) { 
		return (CodeSyncElement) object;
	}
	
	protected List<EObject> getChildrenForCodeSyncElement(EObject object) {
		return (List<EObject>) 
				CodeSyncPlugin.getInstance().getFeatureValue(getCodeSyncElement(object), CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children());
	}
	
	protected List<Node> getChildrenForView(View view) {
		return view.getPersistentChildren();
	}
	
	//////////////////////////////////////
	// Model elements to views methods
	//////////////////////////////////////
	
	abstract protected int getNewViewsIndex(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram);
	
	abstract protected Node createChildView(View associatedViewOnOpenDiagram, EObject child);
	
	protected boolean canAddChildView(View view, EObject candidate) {
		return !containsChildViewForModelElement(view, candidate);
	}
	
	protected boolean containsChildViewForModelElement(View view, EObject candidate) {
		if (candidate.eResource() == null) {
			return false;
		}
		String candidateFragment = candidate.eResource().getURIFragment(candidate);
		for (Node node : view.getPersistentChildren()) {
			EObject child = node.getDiagrammableElement();
			if (child != null && child.eResource() != null && child.eResource().getURIFragment(child).equals(candidateFragment)) {
				return true;
			}
		}
		return false;
	}
	
	//////////////////////////////////////
	// Views to model elements methods
	//////////////////////////////////////
		
	abstract protected CodeSyncElement createModelElementChild(EObject object, View child);
	
	protected boolean containsModelElementForChildView(EObject object, View candidate) {
		EObject diagrammableElement = candidate.getDiagrammableElement();
		if (diagrammableElement == null) {
			return false;
		}
		String candidateFragment = diagrammableElement.eResource() == null ? null : diagrammableElement.eResource().getURIFragment(diagrammableElement);
		if (candidateFragment == null)
			return false;
		
		if (candidateFragment.equals(object.eResource().getURIFragment(object))) {
			return true;
		}
		List<EObject> children = getChildrenForCodeSyncElement(object);		
		for (EObject child : children) {
			if (candidateFragment.equals(child.eResource().getURIFragment(child))) {
				return true;
			}
		}
		return false;
	}
	
}