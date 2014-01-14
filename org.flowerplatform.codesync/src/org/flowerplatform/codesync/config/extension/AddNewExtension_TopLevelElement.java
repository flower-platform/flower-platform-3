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
package org.flowerplatform.codesync.config.extension;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.emf_model.notation.Bounds;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncRoot;

/**
 * @author Mariana Gheorghe
 */
public class AddNewExtension_TopLevelElement implements AddNewExtension {

	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String LOCATION = "location";
	
	@Override
	public boolean addNewView(CodeSyncElement codeSyncElement, View parentView, Resource codeSyncMappingResource, Map<String, Object> parameters) {
		Node node = NotationFactory.eINSTANCE.createNode();
		parameters.put(CodeSyncPlugin.VIEW, node);
		
		// check if top-level element
		if (parentView != null) {
			return true;
		}	
		
		// set layout constraints
		Bounds bounds = NotationFactory.eINSTANCE.createBounds();
		bounds.setX(getParameterValue(parameters, X, 100));
		bounds.setY(getParameterValue(parameters, Y, 100));
		bounds.setWidth(getParameterValue(parameters, WIDTH, 100));
		bounds.setHeight(getParameterValue(parameters, HEIGHT, 100));
		node.setLayoutConstraint(bounds);
		
		Node title = NotationFactory.eINSTANCE.createNode();
		title.setDiagrammableElement(codeSyncElement);
		title.setViewType("topLevelBoxTitle");
		node.getPersistentChildren().add(title);
		
		for (CodeSyncElementDescriptor childDescriptor : CodeSyncDiagramOperationsService.getInstance().getChildrenCategories(codeSyncElement.getType())) {
			String category = childDescriptor.getCategory();
			if (category != null) {
				CodeSyncDiagramOperationsService.getInstance().addCategorySeparator(node, codeSyncElement, childDescriptor);
			}
		}	
			
		// populate PARENT_CODE_SYNC_ELEMENT
		if (codeSyncElement.eContainer() == null) {
			String location = (String) parameters.get(LOCATION);
			if (location == null) {
				throw new RuntimeException("No location specified for new element");
			}
			CodeSyncElement parent = getOrCreateCodeSyncElementForLocation(codeSyncMappingResource, location.split("/"));
			CodeSyncElement file = CodeSyncOperationsService.getInstance().create(CodeSyncPlugin.FILE);
			CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
			CodeSyncOperationsService.getInstance().setKeyFeatureValue(file,  
					descriptor.getDefaultName() + "." + descriptor.getExtension()); // TODO numbering logic
			CodeSyncOperationsService.getInstance().add(parent, file);
			parameters.put(CodeSyncPlugin.PARENT_CODE_SYNC_ELEMENT, file);
		}
				
		return true;
	}

	protected int getParameterValue(Map<String, Object> parameters, String name, int defaultValue) {
		if (parameters.containsKey(name)) {
			return ((Number) parameters.get(name)).intValue();
		} else {
			return defaultValue;
		}
	}
	
	/**
	 * Finds the {@link CodeSyncElement} corresponding to {@code path}; if it does not exist, create it and
	 * any necessary parents.
	 */
	public static CodeSyncElement getOrCreateCodeSyncElementForLocation(Resource codeSyncMappingResource, String[] path) {
		CodeSyncRoot root = null;
		for (EObject eObject : codeSyncMappingResource.getContents()) {
			if ((CodeSyncOperationsService.getInstance().getKeyFeatureValue((CodeSyncRoot) eObject)).equals(path[0])) {
				root = (CodeSyncRoot) eObject;
				break;
			}
		}
		// no SrcDir => create it
		if (root == null) {
			root = CodeSyncFactory.eINSTANCE.createCodeSyncRoot();
			root.setType(CodeSyncPlugin.FOLDER);
			CodeSyncOperationsService.getInstance().setKeyFeatureValue(root, path[0]);
			root.setAdded(true);
			root.setType(CodeSyncPlugin.FOLDER);
			codeSyncMappingResource.getContents().add(root);
		}
		
		CodeSyncElement codeSyncElement = root;
		for (int i = 1; i < path.length; i++) {
			boolean foundElement = false;
			for (CodeSyncElement child : codeSyncElement.getChildren()) {
				if (CodeSyncOperationsService.getInstance().getKeyFeatureValue(child).equals(path[i])) {
					codeSyncElement = child;
					foundElement = true;
					break;
				}
			}
			if (!foundElement) {
				CodeSyncElement child = CodeSyncOperationsService.getInstance().create(CodeSyncPlugin.FOLDER);
				CodeSyncOperationsService.getInstance().setKeyFeatureValue(child, path[i]);
				CodeSyncOperationsService.getInstance().add(codeSyncElement, child);
				codeSyncElement = child;
			}
		}
		return codeSyncElement;
	}

	@Override
	public boolean configNew(CodeSyncElement codeSyncElement, Resource codeSyncMappingResource, Map<String, Object> parameters) {		
		return true;
	}
	
}
