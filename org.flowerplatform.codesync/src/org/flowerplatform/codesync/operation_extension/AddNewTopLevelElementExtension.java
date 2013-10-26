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
package org.flowerplatform.codesync.operation_extension;

import static org.flowerplatform.codesync.remote.CodeSyncDiagramOperationsService1.PARENT_CODE_SYNC_ELEMENT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
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
public class AddNewTopLevelElementExtension implements AddNewExtension {

	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String LOCATION = "location";
	
	@Override
	public String addNew(CodeSyncElement codeSyncElement, Node node, View parentView, Resource codeSyncMappingResource, Map<String, Object> parameters) {
		// check if top-level element
		if (parentView != null) {
			return null;
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
		title.setViewType("title");
		node.getPersistentChildren().add(title);
		
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
		for (CodeSyncElementDescriptor childDescriptor : getChildrenForDescriptor(descriptor)) {
			String category = childDescriptor.getCategory();
			if (category != null) {
				Node categorySeparator = NotationFactory.eINSTANCE.createNode();
				categorySeparator.setViewType("categorySeparator");
				Map<String, String> viewDetails = new HashMap<String, String>();
				viewDetails.put("codeSyncType", childDescriptor.getCodeSyncType());
				viewDetails.put("title", category);
				String image = childDescriptor.getIconUrl();
				if (image != null) {
					String codeSyncPackage = CodeSyncPlugin.getInstance()
							.getBundleContext().getBundle().getSymbolicName();
					if (!image.startsWith("/")) {
						image = "/" + image;
					}
					image = codeSyncPackage + image;
				}
				viewDetails.put("newChildIcon", image);
				categorySeparator.setViewDetails(viewDetails);
				node.getPersistentChildren().add(categorySeparator);
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
			file.setName(descriptor.getDefaultName() + "." + descriptor.getExtension()); // TODO numbering logic
			CodeSyncOperationsService.getInstance().add(parent, file);
			parameters.put(PARENT_CODE_SYNC_ELEMENT, file);
		}
		return null;
	}

	protected List<CodeSyncElementDescriptor> getChildrenForDescriptor(CodeSyncElementDescriptor parentDescriptor) {
		List<CodeSyncElementDescriptor> result = new ArrayList<CodeSyncElementDescriptor>();
		for (CodeSyncElementDescriptor descriptor : CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors()) {
			for (String codeSyncTypeCategory : descriptor.getCodeSyncTypeCategories()) {
				if (parentDescriptor.getChildrenCodeSyncTypeCategories().contains(codeSyncTypeCategory)) {
					boolean alreadyAdded = false;
					for (CodeSyncElementDescriptor addedDescriptor : result) {
						if (addedDescriptor.getCategory().equals(descriptor.getCategory())) {
							alreadyAdded = true;
							break;
						}
					}
					if (!alreadyAdded) {
						result.add(descriptor);
					}
					break;
				}
			}
		}
		return result;
	}
	
	protected int getParameterValue(Map<String, Object> parameters, String name, int defaultValue) {
		if (parameters.containsKey(name)) {
			return (int) parameters.get(name);
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
			if (((CodeSyncRoot) eObject).getName().equals(path[0])) {
				root = (CodeSyncRoot) eObject;
				break;
			}
		}
		// no SrcDir => create it
		if (root == null) {
			root = CodeSyncFactory.eINSTANCE.createCodeSyncRoot();
			root.setName(path[0]);
			root.setAdded(true);
			root.setType(CodeSyncPlugin.FOLDER);
			codeSyncMappingResource.getContents().add(root);
		}
		
		CodeSyncElement codeSyncElement = root;
		for (int i = 1; i < path.length; i++) {
			boolean foundElement = false;
			for (CodeSyncElement child : codeSyncElement.getChildren()) {
				if (child.getName().equals(path[i])) {
					codeSyncElement = child;
					foundElement = true;
					break;
				}
			}
			if (!foundElement) {
				CodeSyncElement child = CodeSyncOperationsService.getInstance().create(CodeSyncPlugin.FOLDER);
				child.setName(path[i]);
				CodeSyncOperationsService.getInstance().add(codeSyncElement, child);
				codeSyncElement = child;
			}
		}
		return codeSyncElement;
	}
	
}
