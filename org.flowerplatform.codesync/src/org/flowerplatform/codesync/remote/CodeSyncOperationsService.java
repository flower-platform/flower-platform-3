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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.flowerplatform.codesync.config.extension.FeatureAccessExtension;
import org.flowerplatform.codesync.wizard.MDADependencyDescriptor;
import org.flowerplatform.codesync.wizard.remote.MDADependency;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.properties.remote.Property;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.FeatureChange;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncOperationsService {

	public static final String ID = "codeSyncOperationsService";

	public static CodeSyncOperationsService getInstance() {
		return (CodeSyncOperationsService) CommunicationPlugin.getInstance()
				.getServiceRegistry().getService(ID);
	}
	
	private DiagramEditorStatefulService getDiagramEditorStatefulService() {
		return (DiagramEditorStatefulService) EditorPlugin.getInstance().getEditorStatefulServiceByEditorName("diagram");
	}
	
	public CodeSyncElement create(String codeSyncType) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncType);
		if (descriptor == null) {
			throw new RuntimeException("Cannot find descriptor for codeSyncType = " + codeSyncType);
		}
		CodeSyncElement codeSyncElement = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		codeSyncElement.setType(descriptor.getCodeSyncType());
		// set the key feature to the default value
		setFeatureValue(codeSyncElement, descriptor.getKeyFeature(), descriptor.getDefaultName());
		return codeSyncElement;
	}
	
	public void add(CodeSyncElement parent, CodeSyncElement elementToAdd) {
		elementToAdd.setAdded(true);
		parent.getChildren().add(elementToAdd);
		propagateParentSyncFalse(elementToAdd);
	}
	
	/**
	 * Delegates to registered {@link FeatureAccessExtension}s.
	 */
	public Object getFeatureValue(CodeSyncElement codeSyncElement, String feature) {
		List<FeatureAccessExtension> converters = 
				CodeSyncPlugin.getInstance().getFeatureAccessExtensions();
		for (FeatureAccessExtension converter : converters) {
			if (converter.hasCodeSyncType(codeSyncElement.getType())) {
				return converter.getValue(codeSyncElement, feature);
			}
		}
		return null;
	}
	
	/**
	 * Returns the value of <code>feature</code> on the <code>codeSyncElement</code>, first from the
	 * list of {@link org.eclipse.emf.ecore.change.FeatureChange}s, if it exists.
	 */
	public Object getFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature) {
		FeatureChange featureChange = codeSyncElement.getFeatureChanges().get(feature);
		if (featureChange != null) {
			return featureChange.getNewValue();
		}
		
		if (feature.getEContainingClass().isSuperTypeOf(codeSyncElement.eClass())) {
			return codeSyncElement.eGet(feature);
		} else {
			AstCacheElement astElement = codeSyncElement.getAstCacheElement();
			if (astElement != null) {
				return astElement.eGet(feature);
			}
		}
	
		return null;
	}
	
	public Object getOldFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature) {
		FeatureChange featureChange = codeSyncElement.getFeatureChanges().get(feature);
		if (featureChange != null) {
			return featureChange.getOldValue();
		} else {
			return getFeatureValue(codeSyncElement, feature);
		}
	}
	
	/**
	 * Delegates to registered {@link FeatureAccessExtension}s.
	 */
	public void setFeatureValue(CodeSyncElement codeSyncElement, String feature, Object newValue) {
		List<FeatureAccessExtension> converters = 
				CodeSyncPlugin.getInstance().getFeatureAccessExtensions();
		for (FeatureAccessExtension converter : converters) {
			if (converter.hasCodeSyncType(codeSyncElement.getType())) {
				converter.setValue(codeSyncElement, feature, newValue);
			}
		}
	}
	
	public void setFeatureValue(CodeSyncElement codeSyncElement, EStructuralFeature feature, Object newValue) {
		Object oldValue = getOldFeatureValue(codeSyncElement, feature);
		createAndAddFeatureChange(codeSyncElement, feature, oldValue, newValue);
	}
	
	/**
	 * Creates and adds a {@link FeatureChange} if the <code>oldValue</code> and <code>newValue</code> are different. 
	 * 
	 * Important: we first remove, and then add a new feature change to trigger a change description on the {@link CodeSyncElement},
	 * so the processors can update the views.
	 * 
	 * @author Sebastian Solomon
	 */
	protected void createAndAddFeatureChange(CodeSyncElement element,
			EStructuralFeature feature, Object oldValue, Object newValue) {
		element.getFeatureChanges().removeKey(feature);
		if (!safeEquals(newValue, oldValue)) {
			FeatureChange featureChange = CodeSyncFactory.eINSTANCE
					.createFeatureChange();
			// first add the FC to the map
			// so the feature will be available when setting old and new value
			element.getFeatureChanges().put(feature, featureChange);
			featureChange.setOldValue(oldValue);
			featureChange.setNewValue(newValue);

			if (element.isSynchronized()) {
				element.setSynchronized(false);
				propagateParentSyncFalse(element);
			}

		} else if (element.getFeatureChanges().size() == 0) {
			propagateParentSyncTrue(element);
		}
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	protected void propagateParentSyncFalse(CodeSyncElement element) {
		while (element.eContainer() != null) {

			EObject parent = element.eContainer();
			if (parent instanceof CodeSyncElement) {
				element = (CodeSyncElement) parent;
				if (element.isSynchronized()) {
					element.setChildrenSynchronized(false);
				}
			} else
				return;

		}

	}
	
	/**
	 * @author Sebastian Solomon
	 */
	protected void propagateParentSyncTrue(CodeSyncElement element) {
		if (!element.isAdded() && !element.isDeleted()
				&& element.getFeatureChanges().size() == 0) {
			element.setSynchronized(true); // orange
			if (allChildrenGreen(element)) // if all childs green =>become green
				element.setChildrenSynchronized(true);
			// * walk whole parent hierarchy; set childrenSync = true if all
			// children are sync,not newly added not deleted
			while (element.eContainer() != null) {
				if (element.eContainer() instanceof CodeSyncElement) {
					element = (CodeSyncElement) element.eContainer();
					if (allChildrenGreen(element)) {
						element.setChildrenSynchronized(true);
					} else
						return; // if one child is notSync, return
				}
			}
		}
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	private boolean allChildrenGreen(CodeSyncElement element) {
		for (CodeSyncElement cse : element.getChildren()) {
			if (cse.isAdded() || cse.isDeleted() || !cse.isSynchronized()
					|| !cse.isChildrenSynchronized())
				return false;
		}
		return true;
	}
	
	protected boolean safeEquals(Object a, Object b) {
		if (a == null) {
			return b == null;
		} else {
			return a.equals(b);
		}
	}
	
	public Object getKeyFeatureValue(CodeSyncElement element) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(element.getType());
		return getFeatureValue(element, descriptor.getKeyFeature());
	}
	
	public void setKeyFeatureValue(CodeSyncElement element, Object newValue) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(element.getType());
		String keyFeature = descriptor.getKeyFeature();
		setFeatureValue(element, keyFeature, newValue);
	}
	
	public void markDeleted(CodeSyncElement element) {
		// TODO
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	protected void propagateOnChildDelete(CodeSyncElement cse) {

		for (CodeSyncElement child : cse.getChildren()) {
			child.setDeleted(true);
			propagateOnChildDelete(child);
		}

	}
	
	public boolean hasChildWithKeyFeatureValue(CodeSyncElement parent, String childCodeSyncType, String keyFeatureValue) {
		for (CodeSyncElement child : parent.getChildren()) {
			if (!childCodeSyncType.equals(child.getType())) {
				continue;
			}
			if (keyFeatureValue.equals(getKeyFeatureValue(child))) {
				return true;
			}
		}
		return false;
	}
		
	public List<String> getFeatures(String codeSyncType) {
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncType);
		return descriptor.getFeatures();
	}
	
	@RemoteInvocation
	public void synchronize(ServiceInvocationContext context, String path, String technology) {
		File diagram;
		try {
			diagram = (File) EditorPlugin.getInstance().getFileAccessController().getFile(path);
		} catch (Exception e) {
			throw new RuntimeException(path);
		}
		File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile(diagram);
		File srcDir = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(project, "js");
		CodeSyncPlugin.getInstance().getCodeSyncAlgorithmRunner().runCodeSyncAlgorithm(project, srcDir, technology, context.getCommunicationChannel(), true);
	}

	@RemoteInvocation
	public String regenerateDescriptors() {
		return CodeSyncPlugin.getInstance().regenerateDescriptors();
	}
		
	///////////////////////////////////////////////
	// WIZARD METHODS
	///////////////////////////////////////////////
	
	private void getAllMDAElements(Object object, List<TreeNode> result, String name) {
		for (EObject entity : object instanceof Resource ? ((Resource) object).getContents() : ((CodeSyncElement) object).getChildren()) {
			String type = ((CodeSyncElement) entity).getType();
			if (type.equals("mdaElement") && (name != null ? ((CodeSyncElement) entity).getName().equals(name) : true)) {
				TreeNode node = getTreeNodeFromMDAElement((CodeSyncElement) entity);				
				for (EObject attribute : ((CodeSyncElement) entity).getChildren()) {									
					if (node.getChildren() == null) {
						node.setChildren(new ArrayList<TreeNode>());
						node.setHasChildren(true);
					}
					node.getChildren().add(getTreeNodeFromMDAElement((CodeSyncElement) attribute));
				}
				result.add(node);
			}
			getAllMDAElements((CodeSyncElement) entity, result, name);
		}
	}
	
	private CodeSyncElement getMDAElement(Object object, PathFragment mdaElementInfo) {
		for (EObject entity : object instanceof Resource ? ((Resource) object).getContents() : ((CodeSyncElement) object).getChildren()) {		
			if (((CodeSyncElement) entity).getType().equals(mdaElementInfo.getType()) 
					&& (mdaElementInfo.getName() != null ? getKeyFeatureValue((CodeSyncElement) entity).equals(mdaElementInfo.getName()) : true)) {
				return (CodeSyncElement) entity;
			}
			CodeSyncElement cse = getMDAElement((CodeSyncElement) entity, mdaElementInfo);
			if (cse != null) {
				return cse;
			}
		}
		return null;
	}
	
	private TreeNode getTreeNodeFromMDAElement(CodeSyncElement cse) {
		TreeNode node = new TreeNode();
		String keyFeature = (String) getKeyFeatureValue(cse);
		node.setLabel(keyFeature);
		node.setIcon(CodeSyncPlugin.getInstance().getResourceUrl("images/wizard/wand-hat.png"));
		node.setPathFragment(new PathFragment(keyFeature, cse.getType()));
		return node;
	}
	
	private List<Property> getProperties(String mdaElementName, MDADependencyDescriptor descriptor) {
		List<Property> properties = new ArrayList<Property>();
		for (Property property : descriptor.getProperties()) {			
			properties.add(new Property()
							.setNameAs(property.getName())
							.setTypeAs(property.getType())
							.setValueAs(String.format((String) property.getValue(), mdaElementName))
							.setReadOnlyAs(property.getReadOnly()));
		}
		return properties;
	}
	
	private Resource getCodeSyncMappingResourceFromPath(String editorResourcePath) {
		DiagramEditableResource der = (DiagramEditableResource) getDiagramEditorStatefulService().getEditableResource(editorResourcePath);
		ResourceSet resourceSet = der.getResourceSet();
		File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile((File) der.getFile());
		return CodeSyncPlugin.getInstance().getCodeSyncMapping(project, resourceSet);		
	}
	
	@RemoteInvocation
	public List<TreeNode> getMDAElementsFromModel(ServiceInvocationContext context, String editorResourcePath) {		
		List<TreeNode> result = new ArrayList<TreeNode>();
		getAllMDAElements(getCodeSyncMappingResourceFromPath(editorResourcePath), result, null);
		return result;
	}
		
	@RemoteInvocation
	public List<MDADependency> getMDADependencies(PathFragment mdaElementInfo) {
		List<MDADependency> dependencies = new ArrayList<MDADependency>();
		for (RelationDescriptor descriptor : CodeSyncPlugin.getInstance().getRelationDescriptors()) {
			if (descriptor instanceof MDADependencyDescriptor 
					&& descriptor.getSourceCodeSyncTypes().contains(mdaElementInfo.getType()) 
					&& ((MDADependencyDescriptor) descriptor).getProperties() != null) {
				dependencies.add(new MDADependency((MDADependencyDescriptor) descriptor, getProperties(mdaElementInfo.getName(), (MDADependencyDescriptor) descriptor)));
			}
		}
		return dependencies;
	}
	
	@RemoteInvocation
	public void addMDADependency(ServiceInvocationContext context, String editorResourcePath, MDADependency dependency, PathFragment mdaElementInfo) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (Property property : dependency.getProperties()) {
			parameters.put(property.getName(), property.getValue());
		}
		
		Resource resource = getCodeSyncMappingResourceFromPath(editorResourcePath);
		CodeSyncElement source = getMDAElement(resource, mdaElementInfo);
		
		parameters.put(CodeSyncDiagramOperationsService1.SOURCE, source);
		
		CodeSyncDiagramOperationsService1 service = (CodeSyncDiagramOperationsService1) 
				CommunicationPlugin.getInstance().getServiceRegistry().getService("codeSyncDiagramOperationsService");
		
		service.addNewRelation(getCodeSyncMappingResourceFromPath(editorResourcePath), dependency.getType(), parameters);
	}
}
