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

import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.codesync.code.javascript.regex_ast.Parameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.ExpandableNode;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;

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
	
	public void addElement(ServiceInvocationContext context, String type, String keyParameter, boolean isCategory, Map<String, String> parameters, String template, String parentViewId) {

		// step 1: create the element
		RegExAstCodeSyncElement cse = RegExAstFactory.eINSTANCE.createRegExAstCodeSyncElement();
		cse.setType(type);
		RegExAstCacheElement ast = RegExAstFactory.eINSTANCE.createRegExAstCacheElement();
		ast.setCategoryNode(isCategory);
		ast.setKeyParameter(keyParameter);
		if (parameters != null) {
			for (Entry<String, String> entry : parameters.entrySet()) {
				Parameter parameter = RegExAstFactory.eINSTANCE.createParameter();
				parameter.setName(entry.getKey());
				parameter.setValue(entry.getValue());
				ast.getParameters().add(parameter);
			}
		}
		cse.setName(getParameterValue(ast, keyParameter));
		cse.setAstCacheElement(ast);
		DiagramEditableResource der = getEditableResource(context);
		ResourceSet resourceSet = der.getResourceSet();
		IProject project = ProjectsService.getInstance().getProjectWrapperResourceFromFile(der.getFile()).getProject();
		Resource astCache = CodeSyncCodePlugin.getInstance().getAstCache(project, resourceSet);
		astCache.getContents().add(ast);
		cse.setTemplate(template);
		
		// step 2: add the element to the model
		if (parentViewId != null) {
			// in an existing node
			View parent = getViewById(context, parentViewId);
			((CodeSyncElement) parent.getDiagrammableElement()).getChildren().add(cse);
			((ExpandableNode) parent).setHasChildren(true);
		} else {
			// on the diagram
			Diagram diagram = getDiagram(context);
			
			CodeSyncElement file = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			file.setType("File");
			file.setName(cse.getName());
			file.getChildren().add(cse);
			
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
	}
	
	protected String getParameterValue(RegExAstCacheElement ast, String key) {
		for (Parameter parameter : ast.getParameters()) {
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
