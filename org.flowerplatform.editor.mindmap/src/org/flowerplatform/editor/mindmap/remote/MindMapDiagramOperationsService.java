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

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.MindMapNode;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;
import com.crispico.flower.mp.model.codesync.MindMapElement;

/**
 * @author Cristina Constantinescu
 */
public class MindMapDiagramOperationsService {

	private static final String SERVICE_ID = "mindMapDiagramOperationsService";
	
	public static MindMapDiagramOperationsService getInstance() {
		return (MindMapDiagramOperationsService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
		
	public void setExpanded(ServiceInvocationContext context, String viewId, boolean expanded) {		
		MindMapElement model = (MindMapElement) getMindMapNodeById(context, viewId).getDiagrammableElement();
		model.setExpanded(expanded);
	}

	public void setText(ServiceInvocationContext context, String viewId, String text) {	
		CodeSyncElement model = (CodeSyncElement) getMindMapNodeById(context, viewId).getDiagrammableElement();
		model.setName(text);				
	}
	
	public void changeParent(ServiceInvocationContext context, String viewId, String parentViewId, int index, int side) {		
		MindMapElement model = (MindMapElement) getMindMapNodeById(context, viewId).getDiagrammableElement();
		MindMapElement oldParentModel = (MindMapElement) model.eContainer();
		MindMapElement newParentModel = (MindMapElement) getMindMapNodeById(context, parentViewId).getDiagrammableElement();
				
		if (!newParentModel.isExpanded()) {
			newParentModel.setExpanded(true);
		}
		
		setSide(model, side);

		if (index != -1) {
			if (!oldParentModel.equals(newParentModel)) {
				oldParentModel.getChildren().remove(model);
				newParentModel.getChildren().add(index, model);
			} else {
				newParentModel.getChildren().move(index, model);
			}
		}
	}
	
	private void setSide(MindMapElement node, int side) {
		node.setSide(side);		
		for (EObject child : node.eContents()) {
			setSide((MindMapElement) child, side);
		}
	}
		
	public Object moveUp(ServiceInvocationContext context, String viewId) {
		MindMapNode node = getMindMapNodeById(context, viewId);		
		CodeSyncElement model = (CodeSyncElement) node.getDiagrammableElement();
		
		if (model instanceof CodeSyncRoot) {
			return node;
		}
		CodeSyncElement parentModel = (CodeSyncElement) model.eContainer();
		int index = parentModel.getChildren().indexOf(model);
		if (index != 0) {
			parentModel.getChildren().move(index - 1, index);
		}
		return node;
	}
	
	public Object moveDown(ServiceInvocationContext context, String viewId) {
		MindMapNode node = getMindMapNodeById(context, viewId);		
		CodeSyncElement model = (CodeSyncElement) node.getDiagrammableElement();
		
		if (model instanceof CodeSyncRoot) {
			return node;
		}
		CodeSyncElement parentModel = (CodeSyncElement) model.eContainer();
		int index = parentModel.getChildren().indexOf(model);
		if (index != parentModel.getChildren().size() - 1) {
			parentModel.getChildren().move(index + 1, index);
		}
		return node;
	}
	
	public Object createNode(ServiceInvocationContext context, String viewId, String viewType) {
		MindMapNode node = getMindMapNodeById(context, viewId);	
		
		CodeSyncElement parentModel = (CodeSyncElement) node.getDiagrammableElement();	
		
		MindMapElement newModel = CodeSyncFactory.eINSTANCE.createMindMapElement();				
		newModel.setName("New " + viewType);
		newModel.setType(viewType);
		
		parentModel.getChildren().add(newModel);
		return node;
	}
	
	public void delete(ServiceInvocationContext context, String viewId) {		
		CodeSyncElement model = (CodeSyncElement) getMindMapNodeById(context, viewId).getDiagrammableElement();	
		if (model instanceof CodeSyncRoot) {
			return;
		}		
		CodeSyncElement parentModel = (CodeSyncElement) model.eContainer();	
		parentModel.getChildren().remove(model);
	}
		
	public void removeAllIcons(ServiceInvocationContext context, List<String> viewIds) {
		for (String viewId : viewIds) {
			MindMapNode node = getMindMapNodeById(context, viewId);		
			MindMapElement cse = (MindMapElement) node.getDiagrammableElement();	
			
			cse.getIcons().clear();
		}		
	}
	
	public void removeFirstIcon(ServiceInvocationContext context, List<String> viewIds) {
		for (String viewId : viewIds) {
			MindMapNode node = getMindMapNodeById(context, viewId);		
			MindMapElement model = (MindMapElement) node.getDiagrammableElement();	
			
			if (model.getIcons().size() > 0) {
				model.getIcons().remove(0);
			}
		}		
	}
	
	public void removeLastIcon(ServiceInvocationContext context, List<String> viewIds) {
		for (String viewId : viewIds) {
			MindMapNode node = getMindMapNodeById(context, viewId);		
			MindMapElement model = (MindMapElement) node.getDiagrammableElement();	
			
			if (model.getIcons().size() > 0) {
				model.getIcons().remove(model.getIcons().size() - 1);
			}
		}		
	}
	
	public void addIcon(ServiceInvocationContext context, List<String> viewIds, String icon) {
		for (String viewId : viewIds) {
			MindMapNode node = getMindMapNodeById(context, viewId);		
			MindMapElement model = (MindMapElement) node.getDiagrammableElement();	
			
			model.getIcons().add(icon);
		}		
	}
	
	public boolean setMinMaxWidth(ServiceInvocationContext context, List<String> viewIds, long minWidth, long maxWidth) {
		for (String viewId : viewIds) {
			MindMapNode node = getMindMapNodeById(context, viewId);		
			MindMapElement model = (MindMapElement) node.getDiagrammableElement();	
			
			model.setMinWidth(minWidth);
			model.setMaxWidth(maxWidth);
		}	
		return true;
	}
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected MindMapNode getMindMapNodeById(ServiceInvocationContext context, String viewId) {
		return (MindMapNode) getEditableResource(context).getEObjectById(viewId);
	}	
	
}