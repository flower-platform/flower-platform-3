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
package org.flowerplatform.editor.mindmap.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.codesync.remote.CodeSyncElementFeatureChangesProcessor;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.MindMapNode;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Cristina Constantinescu
 */
public class MindMapDiagramOperationsService {

	private static final String SERVICE_ID = "mindMapDiagramOperationsService";
	
	public static MindMapDiagramOperationsService getInstance() {
		return (MindMapDiagramOperationsService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
		
	public void setExpanded(ServiceInvocationContext context, String viewId, boolean expanded) {
		MindMapNode node = getMindMapNodeById(context, viewId);		
		if (!expanded) {			
			node.getPersistentChildren().clear();				
		}
		node.setExpanded(expanded);
	}

	public void setText(ServiceInvocationContext context, String viewId, String text) {
		MindMapNode node = getMindMapNodeById(context, viewId);		
		CodeSyncElement cse = (CodeSyncElement) node.getDiagrammableElement();
		CodeSyncPlugin.getInstance().setFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name(), text);
		
	}
	
	public Object changeParent(ServiceInvocationContext context, String viewId, String parentViewId, int index, int side) {
		MindMapNode node = getMindMapNodeById(context, viewId);	
		MindMapNode newParentNode = getMindMapNodeById(context, parentViewId);
		
		CodeSyncElement cse = (CodeSyncElement) node.getDiagrammableElement();
		CodeSyncElement oldParentCSE = (CodeSyncElement) cse.eContainer();
		CodeSyncElement newParentCSE = (CodeSyncElement) newParentNode.getDiagrammableElement();
		
		if (oldParentCSE.equals(newParentCSE)) {
		} else {			
			((MindMapNode) node.eContainer()).getPersistentChildren().remove(node);
			oldParentCSE.getChildren().remove(cse);			
			newParentCSE.getChildren().add(index, cse);
	
			newParentNode.setHasChildren(true);
		}
	
		setSide(node, side);
		
		return node;
	}
	
	public Object moveUp(ServiceInvocationContext context, String viewId) {
		MindMapNode node = getMindMapNodeById(context, viewId);		
		CodeSyncElement cse = (CodeSyncElement) node.getDiagrammableElement();
		
		if (cse instanceof CodeSyncRoot) {
			return node;
		}
		CodeSyncElement parentCSE = (CodeSyncElement) cse.eContainer();
		MindMapNode parentNode = (MindMapNode) node.eContainer();
		if (parentNode.getPersistentChildren().indexOf(node) != 0) {
			parentNode.getPersistentChildren().move(parentNode.getPersistentChildren().indexOf(node), parentNode.getPersistentChildren().indexOf(node) - 1);
		}
		if (parentCSE.getChildren().indexOf(cse) != 0) {
			parentCSE.getChildren().move(parentCSE.getChildren().indexOf(cse), parentCSE.getChildren().indexOf(cse) - 1);
		}
		return node;
	}
	
	public Object moveDown(ServiceInvocationContext context, String viewId) {
		MindMapNode node = getMindMapNodeById(context, viewId);		
		CodeSyncElement cse = (CodeSyncElement) node.getDiagrammableElement();
		
		if (cse instanceof CodeSyncRoot) {
			return node;
		}
		CodeSyncElement parentCSE = (CodeSyncElement) cse.eContainer();
		MindMapNode parentNode = (MindMapNode) node.eContainer();
		if (parentNode.getPersistentChildren().indexOf(node) != parentNode.getPersistentChildren().size() - 1) {
			parentNode.getPersistentChildren().move(parentNode.getPersistentChildren().indexOf(node), parentNode.getPersistentChildren().indexOf(node) + 1);
		}
		if (parentCSE.getChildren().indexOf(cse) != parentCSE.getChildren().size() - 1) {
			parentCSE.getChildren().move(parentCSE.getChildren().indexOf(cse), parentCSE.getChildren().indexOf(cse) + 1);
		}
		return node;
	}
	
	public Object createNew(ServiceInvocationContext context, String viewId, String viewType) {
		MindMapNode node = getMindMapNodeById(context, viewId);	
		
		CodeSyncElement parentCSE = (CodeSyncElement) node.getDiagrammableElement();	
		
		CodeSyncElement mindmapCse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();				
		mindmapCse.setName("New " + viewType);
		mindmapCse.setType(viewType);
		
		parentCSE.getChildren().add(parentCSE.getChildren().size(), mindmapCse);
		node.setHasChildren(true);
		return node;
	}
	
	public void delete(ServiceInvocationContext context, String viewId) {
		MindMapNode node = getMindMapNodeById(context, viewId);		
		CodeSyncElement cse = (CodeSyncElement) node.getDiagrammableElement();	
		if (cse instanceof CodeSyncRoot) {
			return;
		}
		MindMapNode parentNode = (MindMapNode) node.eContainer();
		CodeSyncElement parentCSE = (CodeSyncElement) cse.eContainer();	
		parentCSE.getChildren().remove(cse);	
		parentNode.setHasChildren(parentCSE.getChildren().size() > 0);	
	}
	
	private void setSide(MindMapNode node, int side) {
		node.setSide(side);
		for (EObject child : node.getPersistentChildren()) {
			setSide((MindMapNode) child, side);
		}
	}
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected MindMapNode getMindMapNodeById(ServiceInvocationContext context, String viewId) {
		return (MindMapNode) getEditableResource(context).getEObjectById(viewId);
	}	
}