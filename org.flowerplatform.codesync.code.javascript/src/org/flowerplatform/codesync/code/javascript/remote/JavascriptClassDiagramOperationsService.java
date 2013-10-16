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
package org.flowerplatform.codesync.code.javascript.remote;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.ExpandableNode;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana Gheorghe
 */
public class JavascriptClassDiagramOperationsService {

	public static final String SERVICE_ID = "jsClassDiagramOperationsDispatcher";
	
	public void expandCollapseCompartment(ServiceInvocationContext context, String viewId, boolean expand) {
		View view = getViewById(context, viewId);
		if (view instanceof ExpandableNode) {
			((ExpandableNode) view).setExpanded(expand);
		}
	}
	
	public void addElement(
			ServiceInvocationContext context, 
			String type, 
			String keyParameter, 
			boolean isCategory, 
			Map<String, String> parameters, 
			String template, 
			String childType,
			String nextSiblingSeparator,
			String parentViewId,
			String parentCategory) {

		// step 1: create the element
		RegExAstCodeSyncElement cse = (RegExAstCodeSyncElement) createElement(context, type, keyParameter, isCategory, parameters, template, childType, nextSiblingSeparator);
		
		// step 2: add the element to the model
		if (parentViewId != null) {
			// in an existing node
			CodeSyncElement parent = (CodeSyncElement) getViewById(context, parentViewId).getDiagrammableElement();
			if (parentCategory != null) {
				parent = getOrCreateCategory(context, parent, parentCategory);
			}
			parent.getChildren().add(cse);
			((ExpandableNode) getViewById(context, parentViewId)).setHasChildren(true);
		} else {
			// on the diagram
			Diagram diagram = getDiagram(context);
			
			CodeSyncElement file = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			file.setType("File");
			file.setAdded(true);
			file.setName(cse.getName());
			file.getChildren().add(cse);
			
			DiagramEditableResource der = getEditableResource(context);
			ResourceSet resourceSet = der.getResourceSet();
			File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile((File) der.getFile());
			CodeSyncElement srcDir = (CodeSyncElement) CodeSyncCodePlugin.getInstance().getCodeSyncMapping(project, resourceSet)
					.getContents().get(0);
			srcDir.getChildren().add(file);
			
			// copied from dnd handler
			Node node = NotationFactory.eINSTANCE.createNode();
			node.setViewType("file");
			node.setDiagrammableElement(file);
			
			Bounds bounds = NotationFactory.eINSTANCE.createBounds();
			bounds.setX(200);
			bounds.setY(200);
			bounds.setHeight(100);
			bounds.setWidth(100);
			node.setLayoutConstraint(bounds);
			diagram.getPersistentChildren().add(node);
		}
	}
	
	public void deleteElement(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context, viewId);
		CodeSyncElement element = (CodeSyncElement) view.getDiagrammableElement();
		CodeSyncElement parent = (CodeSyncElement) element.eContainer();
		parent.getChildren().remove(element);
		if (parent instanceof CodeSyncRoot) {
			((View) view.eContainer()).getPersistentChildren().remove(view);
		}
	}
	
	protected CodeSyncElement getOrCreateCategory(ServiceInvocationContext context, CodeSyncElement parent, String parentCategory) {
		for (CodeSyncElement child : parent.getChildren()) {
			if (child.getType().equals(parentCategory)) {
				return child;
			}
		}
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("name", parentCategory);
		CodeSyncElement cse = createElement(context, parentCategory, "name", true, parameters, null, null, null);
		parent.getChildren().add(cse);
		return cse;
	}
	
	protected RegExAstCodeSyncElement createElement(
			ServiceInvocationContext context, 
			String type, 
			String keyParameter, 
			boolean isCategory, 
			Map<String, String> parameters, 
			String template, 
			String childType,
			String nextSiblingSeparator) {
		RegExAstCodeSyncElement cse = RegExAstFactory.eINSTANCE.createRegExAstCodeSyncElement();
		cse.setType(type);
		RegExAstCacheElement ast = RegExAstFactory.eINSTANCE.createRegExAstCacheElement();
		ast.setCategoryNode(isCategory);
		ast.setKeyParameter(keyParameter);
		if (parameters != null) {
			for (Entry<String, String> entry : parameters.entrySet()) {
				RegExAstNodeParameter parameter = RegExAstFactory.eINSTANCE.createRegExAstNodeParameter();
				parameter.setName(entry.getKey());
				parameter.setValue(entry.getValue());
				ast.getParameters().add(parameter);
			}
		}
		cse.setName(getParameterValue(ast, keyParameter));
		cse.setAstCacheElement(ast);
		DiagramEditableResource der = getEditableResource(context);
		ResourceSet resourceSet = der.getResourceSet();
		File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile((File) der.getFile());
		Resource astCache = CodeSyncCodePlugin.getInstance().getAstCache(project, resourceSet);
		astCache.getContents().add(ast);
		cse.setTemplate(template);
		cse.setChildType(childType);
		cse.setNextSiblingSeparator(nextSiblingSeparator);
		cse.setAdded(true);
		return cse;
	}
	
	protected String getParameterValue(RegExAstCacheElement ast, String key) {
		for (RegExAstNodeParameter parameter : ast.getParameters()) {
			if (parameter.getName().equals(key)) {
				return parameter.getValue();
			}
		}
		return null;
	}
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected View getViewById(ServiceInvocationContext context, String viewId) {
		return (View) getEditableResource(context).getEObjectById(viewId);
	}
	
	protected Diagram getDiagram(ServiceInvocationContext context) {
		DiagramEditableResource der = getEditableResource(context);
		for (EObject eObject : der.getMainResource().getContents()) {
			if (eObject instanceof Diagram) {
				return (Diagram) eObject;
			}
		}
		throw new RuntimeException("No diagram view for " + der.getEditableResourcePath());
	}
}
