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
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService1;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.emf_model.notation.CategorySeparator;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class ChildrenUpdaterDiagramProcessor extends AbstractChildrenUpdaterDiagramProcessor {

	@Override
	protected void processChildren(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram,
			List<Node> childViews, Map<String, Object> viewDetails) {
		CodeSyncElement codeSyncElement = getCodeSyncElement(object);
		for (CodeSyncElementDescriptor childDescriptor : CodeSyncDiagramOperationsService1.getInstance().getChildrenCategories(codeSyncElement.getType())) {
			String category = childDescriptor.getCategory();
			super.processChildren(
					object, filterChildModelElements(childModelElements, category), 
					associatedViewOnOpenDiagram, filterChildViews(childViews, category), 
					viewDetails);
		}
	}

	protected List<EObject> filterChildModelElements(List<EObject> list, String category) {
		List<EObject> result = new ArrayList<EObject>();
		for (EObject child : list) {
			CodeSyncElementDescriptor descriptor = 
					CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(getCodeSyncElement(child).getType());
			if (category.equals(descriptor.getCategory())) {
				result.add(child);
			}
		}
		return result;
	}
	
	protected List<Node> filterChildViews(List<Node> list, String category) {
		List<Node> result = new ArrayList<Node>();
		for (Node child : list) {
			CodeSyncElement diagrammableElement = (CodeSyncElement) child.getDiagrammableElement();
			if (diagrammableElement == null) {
				continue;
			}
			CodeSyncElementDescriptor descriptor = 
					CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(diagrammableElement.getType());
			if (category.equals(descriptor.getCategory())) {
				result.add(child);
			}
		}
		return result;
	}
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	@Override
	protected int getNewViewsIndex(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram) {
		if (childModelElements.size() == 0) {
			// no children on model => no views to add
			return -1;
		}
		CodeSyncElement codeSyncElement = getCodeSyncElement(childModelElements.get(0));
		CodeSyncElementDescriptor descriptor = 
				CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
		String category = descriptor.getCategory();
		int index = 0;
		
		if (category != null) {
			index = getCategorySeparatorIndex(associatedViewOnOpenDiagram, category);
		}
		// add the number of previous added children having the same type as codeSyncElement
		for (EObject child : object.eContents()) {
			CodeSyncElement childElement = getCodeSyncElement(child);
			if (childElement.equals(codeSyncElement)) {
				break;
			}
			if (childElement.getType().equals(codeSyncElement.getType())) {
				index++;
			}						
		}
		return index;
	}
	
	private int getCategorySeparatorIndex(View view, String category) {
		for (Node node : view.getPersistentChildren()) {
			String nodeCategory = getCategoryForSeparator(node);
			if (category.equals(nodeCategory)) {
				return view.getPersistentChildren().indexOf(node) + 1;
			}
		}
		return -1;
	}

	private String getCategoryForSeparator(Node node) {
		if (!(node instanceof CategorySeparator)) {
			return null;
		}
		CategorySeparator separator = (CategorySeparator) node;
		return separator.getCategory();
	}
	
	@Override
	protected Node createChildView(View associatedViewOnOpenDiagram, EObject child, Map<String, Object> context) {
		return NotationFactory.eINSTANCE.createNode();
	}

	@Override
	protected CodeSyncElement createModelElementChild(EObject object, View child) {
		// TODO Auto-generated method stub
		return null;
	}

}
