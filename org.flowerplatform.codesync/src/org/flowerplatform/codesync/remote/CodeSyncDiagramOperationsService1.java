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

import java.io.File;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.codesync.operation_extension.AddNewExtension;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncDiagramOperationsService1 {

	public static final String PARENT_CODE_SYNC_ELEMENT = "parentCodeSyncElement";
	public static final String PARENT_VIEW = "parentView";
	
	@RemoteInvocation
	public List<CodeSyncElementDescriptor> getCodeSyncElementDescriptors() {
		return CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors();
	}
	
	@RemoteInvocation
	public List<RelationDescriptor> getRelationDescriptors() {
		return CodeSyncPlugin.getInstance().getRelationDescriptors();
	}
	/**
	 * @return ID of the view pointing to the newly added element.
	 */
	@RemoteInvocation
	public String addNew(ServiceInvocationContext context, String diagramId, String viewIdOfParent, String codeSyncType, Map<String, Object> parameters) {
		// create new CodeSyncElement
		CodeSyncElement codeSyncElement = CodeSyncOperationsService.getInstance().create(codeSyncType);
		return addOnDiagram(context, diagramId, viewIdOfParent, codeSyncElement, parameters);
	}

	public String addOnDiagram(ServiceInvocationContext context, String diagramId, String viewIdOfParent, CodeSyncElement codeSyncElement, Map<String, Object> parameters) {
		// create view
		Node view = NotationFactory.eINSTANCE.createNode();
		view.setDiagrammableElement(codeSyncElement);
		View parentView = viewIdOfParent == null ? null : getViewById(context.getAdditionalData(), viewIdOfParent);
		
		// run all AddNewExtensions
		Resource codeSyncMappingResource = getCodeSyncMappingResource(getEditableResource(context.getAdditionalData()));
		for (AddNewExtension addNewExtension : CodeSyncPlugin.getInstance().getAddNewExtensions()) {
			addNewExtension.addNew(codeSyncElement, view, parentView, codeSyncMappingResource, parameters);
		}
		
		// add to diagram
		if (parameters.containsKey(PARENT_VIEW)) {
			parentView = (View) parameters.get(PARENT_VIEW);
		}
		if (parentView == null && viewIdOfParent == null) {
			parentView = getViewById(context.getAdditionalData(), diagramId);
		}
		parentView.getPersistentChildren().add(view);
		view.setViewType(parentView.getViewType() + "." + codeSyncElement.getType());
		
		// add to parent
		if (codeSyncElement.eContainer() == null) {
			CodeSyncElement parentCodeSyncElement = null;
			if (parameters.containsKey(PARENT_CODE_SYNC_ELEMENT)) {
				parentCodeSyncElement = (CodeSyncElement) parameters.get(PARENT_CODE_SYNC_ELEMENT);
			} else {
				parentCodeSyncElement = (CodeSyncElement) parentView.getDiagrammableElement();
			}
			CodeSyncOperationsService.getInstance().add(parentCodeSyncElement, codeSyncElement);
		}
		
		// return ID of the view
		return view.getIdBeforeRemoval();
	}
	
	@RemoteInvocation
	public String getInplaceEditorText(String viewId) {
		// TODO
		return null;
	}
	
	@RemoteInvocation
	public void setInplaceEditorText(String viewId, String text) {
		// TODO
	}
	
	public void addNewRelation(ServiceInvocationContext context, String type, String sourceViewId, String targetViewId) {
		View sourceView = getViewById(context.getAdditionalData(), sourceViewId);
		View targetView = getViewById(context.getAdditionalData(), targetViewId);
		CodeSyncElement source = (CodeSyncElement) sourceView.getDiagrammableElement();
		CodeSyncElement target = (CodeSyncElement) targetView.getDiagrammableElement();
		Relation relation = CodeSyncFactory.eINSTANCE.createRelation();
		relation.setType(type);
		relation.setSource(source);
		relation.setTarget(target);
		source.getRelations().add(relation);
		
		EObject currentObject = sourceView;
		Diagram diagram = null;
		while (currentObject != null) {
			if (currentObject instanceof Diagram) {
				diagram = (Diagram) currentObject;
				break;
			}
			currentObject = currentObject.eContainer();
		}
		createEdge(relation, sourceView, targetView, diagram);
	}
	
	protected Edge createEdge(Relation relation, View source, View target, Diagram diagram) {
		Edge edge = NotationFactory.eINSTANCE.createEdge();
		edge.setDiagrammableElement(relation);
		edge.setSource(source);
		edge.setTarget(target);
		edge.setViewType("edge");
		diagram.getPersistentEdges().add(edge);
		return edge;
	}
	
	///////////////////////
	// Utils
	///////////////////////
	
	protected View getViewById(Map<String, Object> context, String viewId) {
		return (View) getEditableResource(context).getEObjectById(viewId);
	}
	
	public DiagramEditableResource getEditableResource(Map<String, Object> context) {
		return (DiagramEditableResource) context.get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	public static Resource getCodeSyncMappingResource(DiagramEditableResource diagramEditableResource) {
		ResourceSet resourceSet = diagramEditableResource.getResourceSet();
		File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile((File) diagramEditableResource.getFile());
		return CodeSyncPlugin.getInstance().getCodeSyncMapping(project, resourceSet);
	}
	
}
