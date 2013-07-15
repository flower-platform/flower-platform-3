package org.flowerplatform.editor.model.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.java.remote.JavaClassDiagramOperationsService;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana
 */
public class JavaClassProcessor implements IDiagrammableElementFeatureChangesProcessor {

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		if (object == null) {
			return;
		}
		
		List<Object> objectsToUpdate = new ArrayList<Object>();
		
		if (featureChanges == null) {
			// full content
			processFeatureChange(object, null, associatedViewOnOpenDiagram, context, objectsToUpdate);
		} else {
			objectsToUpdate.add(associatedViewOnOpenDiagram);
			for (FeatureChange featureChange : featureChanges) {
				processFeatureChange(object, featureChange, associatedViewOnOpenDiagram, context, objectsToUpdate);
			}
		}
	}

	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> context, List<Object> objectsToUpdate) {
		if (featureChange == null) {
			processAttributes(object, associatedViewOnOpenDiagram, context, objectsToUpdate);
		} else {
			if (CodeSyncPackage.eINSTANCE.getCodeSyncElement_Children().equals(featureChange.getFeature())) {
				processAttributes(object, associatedViewOnOpenDiagram, context, objectsToUpdate);
			}
		}
	}
	
	private void processAttributes(EObject object, View associatedViewOnOpenDiagram, Map<String, Object> context, List<Object> objectsToUpdate) {
		int attributeSeparatorIndex = getAttributeSeparatorIndex(associatedViewOnOpenDiagram);
		if (attributeSeparatorIndex >= 0) {
			// if there is an attribute separator => add views for all the attributes under the separator
			for (EObject child : getCodeSyncElement(object).getChildren()) {
				attributeSeparatorIndex++;
				if (!contains(associatedViewOnOpenDiagram, child)) {
					Node node = NotationFactory.eINSTANCE.createNode();
//					node.setViewType(getCodeSyncElement(child).getType());
					node.setViewType("classAttribute");
					node.setDiagrammableElement(child);
					associatedViewOnOpenDiagram.getPersistentChildren().add(attributeSeparatorIndex, node);
				}
			}
		} else {
			// no attribute separator => remove all the view associated to attributes
			for (Iterator iterator = associatedViewOnOpenDiagram.getPersistentChildren().iterator(); iterator.hasNext();) {
				View view = (View) iterator.next();
				if (view.getViewType().equals("classAttribute")) {
					iterator.remove();
				}
			}
		}
	}
	
	protected int getAttributeSeparatorIndex(View view) {
		for (Node node : view.getPersistentChildren()) {
			if (JavaClassDiagramOperationsService.ATTRIBUTE_SEPARATOR.equals(node.getViewType())) {
				return view.getPersistentChildren().indexOf(node);
			}
		}
		return -1;
	}
	
	protected boolean contains(View view, EObject candidate) {
		String candidateFragment = candidate.eResource().getURIFragment(candidate);
		for (Node node : view.getPersistentChildren()) {
			EObject child = node.getDiagrammableElement();
			if (child != null && child.eResource().getURIFragment(child).equals(candidateFragment)) {
				return true;
			}
		}
		return false;
	}
	
	protected CodeSyncElement getCodeSyncElement(EObject object) { 
		return (CodeSyncElement) object;
	}
	
}
