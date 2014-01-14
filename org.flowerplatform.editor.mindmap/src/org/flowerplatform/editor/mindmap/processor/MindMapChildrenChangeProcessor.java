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
package org.flowerplatform.editor.mindmap.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.flowerplatform.codesync.processor.AbstractChildrenUpdaterDiagramProcessor;
import org.flowerplatform.emf_model.notation.MindMapNode;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.MindMapElement;

/**
 * @author Cristina Constantinescu
 */
public class MindMapChildrenChangeProcessor extends AbstractChildrenUpdaterDiagramProcessor {

	@Override
	protected void processFeatureChange(EObject object, FeatureChange featureChange, View associatedViewOnOpenDiagram, Map<String, Object> context) {
		super.processFeatureChange(object, featureChange, associatedViewOnOpenDiagram, context);
		if (featureChange != null && CodeSyncPackage.eINSTANCE.getMindMapElement_Expanded().equals(featureChange.getFeature())) {
			processChildren(object, getChildrenForCodeSyncElement(object), associatedViewOnOpenDiagram, getChildrenForView(associatedViewOnOpenDiagram), context);
		}
	}
	
	@Override
	protected int getNewViewsIndex(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram) {	
		if (!((MindMapElement) object).isExpanded()) {
			return -1;
		}
		return 0;
	}

	@Override
	protected boolean canAddChildView(View view, EObject candidate, Map<String, Object> context) {
		boolean canAdd = super.canAddChildView(view, candidate, context);
		if (canAdd) {
			return ((MindMapElement) view.getDiagrammableElement()).isExpanded();
		}
		return canAdd;
	}
	
	@Override
	protected Node createChildView(View associatedViewOnOpenDiagram, EObject child, Map<String, Object> context) {		
		MindMapNode node = NotationFactory.eINSTANCE.createMindMapNode();
		node.setViewType(getCodeSyncElement(child).getType());
		
		return node;
	}	
	
	@Override
	protected Node addChildView(View associatedViewOnOpenDiagram, EObject child, int newViewsIndex, Map<String, Object> context) {		
		Node node = super.addChildView(associatedViewOnOpenDiagram, child, newViewsIndex, context);
		if (((MindMapElement) child).isExpanded()) { // process children recursive if expanded
			processChildren(child, getChildrenForCodeSyncElement(child), node, new ArrayList<Node>(), context);			
		}
		return node;
	}

	@Override
	protected void removeChildView(View associatedViewOnOpenDiagram, EObject child, Map<String, Object> context) {		
		super.removeChildView(associatedViewOnOpenDiagram, child, context);
		 // remove children recursive
		for (Iterator<Node> it = getChildrenForView(associatedViewOnOpenDiagram).iterator(); it.hasNext();) {
			View view = it.next();
			removeChildView(view, view.getDiagrammableElement(), context);			
		}	
	}

	@Override
	protected CodeSyncElement createModelElementChild(EObject object, View child) {		
		return null;
	}
	
}
