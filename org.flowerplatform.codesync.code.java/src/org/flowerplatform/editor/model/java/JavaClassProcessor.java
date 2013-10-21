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
package org.flowerplatform.editor.model.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.codesync.processor.CodeSyncElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.java.remote.JavaClassDiagramOperationsService;
import org.flowerplatform.editor.model.remote.ViewDetailsUpdate;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAttributeModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaOperationModelAdapter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Mariana Gheorghe
 */
public class JavaClassProcessor extends CodeSyncElementFeatureChangesProcessor {

	@Override
	public void processFeatureChanges(EObject object, List<FeatureChange> featureChanges, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		super.processFeatureChanges(object, featureChanges, associatedViewOnOpenDiagram, context);
		
		// view details needed for the rename operation
		Map<String, Object> viewDetails = new HashMap<String, Object>();
		
		if (featureChanges == null) {
			// i.e. full content update when opening diagram
			processFeatureChangeForViewDetails(object, null, associatedViewOnOpenDiagram, viewDetails);
		} else {
			// i.e. differential, during change description processing
			for (FeatureChange featureChange : featureChanges) {
				processFeatureChangeForViewDetails(object, featureChange, associatedViewOnOpenDiagram, viewDetails);
			}
		}
		
		if (!viewDetails.isEmpty()) {
			ViewDetailsUpdate update = new ViewDetailsUpdate();
			update.setViewId(associatedViewOnOpenDiagram.eResource().getURIFragment(associatedViewOnOpenDiagram));
			update.setViewDetails(viewDetails);
			
			DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(context, true).
				getViewDetailsUpdates().add(update);
		}
	}
	
	protected void processFeatureChangeForViewDetails(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> viewDetails) {
		if (featureChange == null || featureChange.getFeature().equals(CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name())) {
			viewDetails.put("label", CodeSyncPlugin.getInstance().getCodeSyncOperationsService()
					.getFeatureValue((CodeSyncElement) object, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name()));
		}
	}
	
	@Override
	protected void processChildren(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram, List<Node> childViews, Map<String, Object> context) {
		// attributes
		super.processChildren(object, filterChildModelElements(childModelElements, JavaAttributeModelAdapter.ATTRIBUTE), associatedViewOnOpenDiagram, filterChildViews(childViews, "classAttribute"), context);
		
		// operations
		super.processChildren(object, filterChildModelElements(childModelElements, JavaOperationModelAdapter.OPERATION), associatedViewOnOpenDiagram, filterChildViews(childViews, "classOperation"), context);
	}
	
	protected List<EObject> filterChildModelElements(List<EObject> list, String filter) {
		List<EObject> result = new ArrayList<EObject>();
		for (EObject child : list) {
			if (filter.equals(((CodeSyncElement) child).getType())) {
				result.add(child);
			}
		}
		return result;
	}
	
	protected List<Node> filterChildViews(List<Node> list, String filter) {
		List<Node> result = new ArrayList<Node>();
		for (Node child : list) {
			if (filter.equals(child.getViewType())) {
				result.add(child);
			}
		}
		return result;
	}
	
	@Override
	protected int getNewViewsIndex(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram) {
		if (childModelElements.size() == 0) {
			// no children on model => no views to add
			return -1;
		}
		CodeSyncElement child = getCodeSyncElement(childModelElements.get(0));
		String separatorViewType = child.getType().equals(JavaAttributeModelAdapter.ATTRIBUTE) 
				? JavaClassDiagramOperationsService.ATTRIBUTE_SEPARATOR
				: JavaClassDiagramOperationsService.OPERATIONS_SEPARATOR;
		String childViewType = child.getType().equals(JavaAttributeModelAdapter.ATTRIBUTE)
				? "classAttribute"
				: "classOperation";
		int index = getSeparatorIndex(associatedViewOnOpenDiagram, separatorViewType, childViewType);
		if (index == -1) {
			return index;
		}
		return ++index;
	}

	@Override
	protected Node createChildView(View associatedViewOnOpenDiagram, EObject child, Map<String, Object> context) {
		Node node = NotationFactory.eINSTANCE.createNode();
//		node.setViewType(getCodeSyncElement(child).getType());
		CodeSyncElement cse = getCodeSyncElement(child);
		node.setViewType(cse.getType().equals(JavaAttributeModelAdapter.ATTRIBUTE) ? "classAttribute" : "classOperation");
		return node;
	}
	
	@Override
	protected CodeSyncElement createModelElementChild(EObject object, View child) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected int getSeparatorIndex(View view, String separatorViewType, String childViewType) {
		int childrenCount = 0;
		int separatorIndex = -1;
		for (Node node : view.getPersistentChildren()) {
			if (separatorViewType.equals(node.getViewType())) {
				separatorIndex = view.getPersistentChildren().indexOf(node);
			}
			if (childViewType.equals(node.getViewType())) {
				childrenCount++;
			}
		}
		return separatorIndex;
	}
	
}
