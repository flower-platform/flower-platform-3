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
package org.flowerplatform.codesync.code.javascript.processor;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement;
import org.flowerplatform.codesync.processor.CodeSyncElementFeatureChangesProcessor;
import org.flowerplatform.emf_model.notation.ExpandableNode;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Mariana Gheorghe
 */
public class JavascriptParentElementProcessor extends CodeSyncElementFeatureChangesProcessor {

	@Override
	protected int getNewViewsIndex(EObject object, List<EObject> childModelElements, View associatedViewOnOpenDiagram) {
		if (associatedViewOnOpenDiagram instanceof ExpandableNode && !((ExpandableNode) associatedViewOnOpenDiagram).isExpanded()) {
			return -1;
		}
		return 0;
	}

	@Override
	protected boolean canAddChildView(View view, EObject candidate) {
		if (view instanceof ExpandableNode && !((ExpandableNode) view).isExpanded()) {
			return false;
		}
		return super.canAddChildView(view, candidate);
	}

	@Override
	protected Node createChildView(View associatedViewOnOpenDiagram, EObject child, Map<String, Object> context) {
		CodeSyncElement cse = getCodeSyncElement(child);
		ExpandableNode container = NotationFactory.eINSTANCE.createExpandableNode();
		container.setExpanded(false);
		container.setHasChildren(cse.getChildren().size() > 0);
		if (cse instanceof RegExAstCodeSyncElement) {
			container.setTemplate(((RegExAstCodeSyncElement) cse).getTemplate());
		}
		container.setViewType("fileElementContainer");
		return container;
	}

	@Override
	protected CodeSyncElement createModelElementChild(EObject object, View child) {
		// TODO Auto-generated method stub
		return null;
	}

}
