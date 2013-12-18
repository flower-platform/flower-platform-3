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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.codesync.config.extension.AddNewExtension_TopLevelElement;
import org.flowerplatform.codesync.config.extension.FeatureAccessExtension;
import org.flowerplatform.codesync.wizard.WizardDependencyDescriptor;
import org.flowerplatform.codesync.wizard.remote.WizardDependency;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.communication.tree.remote.PathFragment;
import org.flowerplatform.communication.tree.remote.TreeNode;
import org.flowerplatform.editor.EditorPlugin;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.AstCacheElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.FeatureChange;
import com.crispico.flower.mp.model.codesync.Relation;

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
	
	private Diagram getDiagramFromEditableResourcePath(String editorResourcePath) {		
		DiagramEditableResource der = (DiagramEditableResource) getDiagramEditorStatefulService().getEditableResource(editorResourcePath);
		for (EObject eObject : der.getMainResource().getContents()) {
			if (eObject instanceof Diagram) {
				return (Diagram) eObject;
			}
		}
		return null;
	}
	
	private Resource getCodeSyncMappingResource(String editorResourcePath) {
		DiagramEditableResource der = (DiagramEditableResource) getDiagramEditorStatefulService().getEditableResource(editorResourcePath);
		return CodeSyncDiagramOperationsService1.getCodeSyncMappingResource(der);	
	}
	
	public String getCodeSyncElementPath(EObject cse) {
		String location = "";
		EObject parent = cse;
		while (parent instanceof CodeSyncElement && parent != null) {
			if (location != "") {
				location = "\\" + location;
			}
			location = getKeyFeatureValue((CodeSyncElement) parent) + location;
			parent = parent.eContainer();
		}
		return location;
	}
	
	public void getViewsForCorrespondingCodeSyncElement(CodeSyncElement cse, View view, List<String> list) {
		Iterator<EObject> iter = view.eContents().iterator();		
		while (iter.hasNext()) {
			EObject next = iter.next();
			if (next instanceof View) {
				if (((View) next).getDiagrammableElement() != null && ((View) next).getDiagrammableElement().equals(cse)) {
					if (!list.contains(((View) next).getIdBeforeRemoval())) {
						list.add(((View) next).getIdBeforeRemoval());			
					}
				} else {
					getViewsForCorrespondingCodeSyncElement(cse, (View) next, list);
				}				
			}		
		}
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
	
	private void createWizardElementsTreeStructure(Object object, List<TreeNode> structure) {
		for (EObject entity : object instanceof Resource ? ((Resource) object).getContents() : ((CodeSyncElement) object).getChildren()) {
			CodeSyncElement cse = (CodeSyncElement) entity;
			if (cse.getType().equals(CodeSyncPlugin.WIZARD_ELEMENT)) {
				TreeNode node = getTreeNodeFromwWizardElement(cse);				
				for (EObject attribute : cse.getChildren()) {									
					if (node.getChildren() == null) {
						node.setChildren(new ArrayList<TreeNode>());
						node.setHasChildren(true);
					}
					TreeNode child = getTreeNodeFromwWizardElement((CodeSyncElement) attribute);
					node.getChildren().add(child);
					child.setParent(node);
				}
				structure.add(node);
			}
			createWizardElementsTreeStructure(entity, structure);
		}
	}
	
	private CodeSyncElement getWizardElementFromPath(Object object, List<PathFragment> path) {
		CodeSyncElement wizardElement = getWizardElementFromParentByName(object, path.get(0));
		if (wizardElement != null && path.size() == 2) {
			return getWizardElementFromParentByName(wizardElement, path.get(1));
		}		
		return wizardElement;
	}
	
	private CodeSyncElement getWizardElementFromParentByName(Object object, PathFragment wizardElementInfo) {
		for (EObject entity : object instanceof Resource ? ((Resource) object).getContents() : ((CodeSyncElement) object).getChildren()) {		
			CodeSyncElement cse = (CodeSyncElement) entity;
			if (cse.getType().equals(wizardElementInfo.getType()) && (wizardElementInfo.getName() != null ? cse.eResource().getURIFragment(cse).equals(wizardElementInfo.getName()) : true)) {
				return cse;
			}
			CodeSyncElement result = getWizardElementFromParentByName(cse, wizardElementInfo);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
	
	private TreeNode getTreeNodeFromwWizardElement(CodeSyncElement wizardElement) {
		TreeNode node = new TreeNode();
		String keyFeature = (String) getKeyFeatureValue(wizardElement);
		node.setLabel(keyFeature);
		node.setIcon(CodeSyncPlugin.getInstance().getResourceUrl("images/wizard/wand-hat.png"));
		node.setPathFragment(new PathFragment(wizardElement.eResource().getURIFragment(wizardElement), wizardElement.getType()));
		return node;
	}
		
	public Relation getRelation(CodeSyncElement cse, String relationType) {
		for (Relation relation : cse.getRelations()) {
			if (relation.getType().equals(relationType)) {
				return relation;
			}
		}
		return null;
	}
	
	@RemoteInvocation
	public List<TreeNode> getWizardElements(ServiceInvocationContext context, String editorResourcePath) {		
		List<TreeNode> structure = new ArrayList<TreeNode>();
		createWizardElementsTreeStructure(getCodeSyncMappingResource(editorResourcePath), structure);
		return structure;
	}
		
	@RemoteInvocation
	public List<WizardDependency> getWizardDependencies(ServiceInvocationContext context, String editorResourcePath, List<PathFragment> path) {
		List<WizardDependency> dependencies = new ArrayList<WizardDependency>();
		
		Resource resource = getCodeSyncMappingResource(editorResourcePath);
		CodeSyncElement wizardElement = getWizardElementFromPath(resource, path);
		PathFragment parentWizardElementInfo = path.get(path.size() - 1);
		
		for (RelationDescriptor descriptor : CodeSyncPlugin.getInstance().getRelationDescriptors()) {
			if (descriptor instanceof WizardDependencyDescriptor && descriptor.getSourceCodeSyncTypes().contains(parentWizardElementInfo.getType())) {
				WizardDependencyDescriptor wizardDescriptor = (WizardDependencyDescriptor) descriptor;
				WizardDependency dependency = new WizardDependency()
												.setLabelAs(wizardDescriptor.getLabel())
												.setTypeAs(wizardDescriptor.getType());
				
				Relation relation = getRelation(wizardElement, wizardDescriptor.getType());
				if (relation != null) {
					CodeSyncElement target = relation.getTarget();
					dependency.setTargetLabelAs((String) getCodeSyncElementPath(target))
							  .setTargetIconUrlAs(CodeSyncPlugin.getInstance().getResourceUrl(
									  CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(target.getType()).getIconUrl()));
				}
				dependencies.add(dependency);
			}
		}
		return dependencies;
	}
	
	@RemoteInvocation
	public boolean generateWizardDependencies(ServiceInvocationContext context, String editorResourcePath, List<WizardDependency> dependencies, List<PathFragment> path) {	
		Resource resource = getCodeSyncMappingResource(editorResourcePath);
		CodeSyncElement source = getWizardElementFromPath(resource, path);
		
		for (WizardDependency dependency : dependencies) {
			Map<String, Object> parameters = new HashMap<String, Object>();		
			parameters.put(CodeSyncPlugin.SOURCE, source);
			
			CodeSyncDiagramOperationsService1 service = (CodeSyncDiagramOperationsService1) 
					CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncDiagramOperationsService1.ID);
			
			try {
				service.addNewRelationElement(resource, dependency.getType(), parameters);
			} catch (Exception e) {
				context.getCommunicationChannel().sendCommandWithPush(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								e.getMessage(), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
				return false;
			}
		}		
		return true;
	}

	@RemoteInvocation
	public boolean dragOnDiagramWizardDependenciesTargets(ServiceInvocationContext context, String editorResourcePath, List<WizardDependency> dependencies, List<PathFragment> path) {	
		Resource resource = getCodeSyncMappingResource(editorResourcePath);
		CodeSyncElement source = getWizardElementFromPath(resource, path);
		
		for (WizardDependency dependency : dependencies) {
			CodeSyncDiagramOperationsService1 service = (CodeSyncDiagramOperationsService1) 
					CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncDiagramOperationsService1.ID);
			
			Diagram diagram = getDiagramFromEditableResourcePath(editorResourcePath);
			Relation relation = getRelation(source, dependency.getType());
			
			CodeSyncElement target = relation.getTarget();
			if (source.getType().equals(CodeSyncPlugin.WIZARD_ATTRIBUTE)) {
				target = (CodeSyncElement) target.eContainer();
			}
			service.addOnDiagram(context, diagram.eResource().getURIFragment(diagram), null, target, new HashMap<String, Object>());
		}		
		return true;
	}
	
	@RemoteInvocation
	public boolean dragOnDiagramWizardElements(ServiceInvocationContext context, String editorResourcePath, List<List<PathFragment>> paths) {	
		Resource resource = getCodeSyncMappingResource(editorResourcePath);
		
		CodeSyncDiagramOperationsService1 service = (CodeSyncDiagramOperationsService1) 
				CommunicationPlugin.getInstance().getServiceRegistry().getService(CodeSyncDiagramOperationsService1.ID);
		
		Diagram diagram = getDiagramFromEditableResourcePath(editorResourcePath);
		
		for (List<PathFragment> path : paths) {
			CodeSyncElement source = getWizardElementFromPath(resource, path);
			service.addOnDiagram(context, diagram.eResource().getURIFragment(diagram), null, source, new HashMap<String, Object>());
		}
		
		return true;
	}
	
	public void selectWizardDependenciesTargetsFromDiagram(ServiceInvocationContext context, String editorResourcePath, List<WizardDependency> dependencies, List<PathFragment> path) {
		Resource resource = getCodeSyncMappingResource(editorResourcePath);
		CodeSyncElement source = getWizardElementFromPath(resource, path);
		
		List<String> list = new ArrayList<String>();
		for (WizardDependency dependency : dependencies) {
			Diagram diagram = getDiagramFromEditableResourcePath(editorResourcePath);			
			CodeSyncElement target = getRelation(source, dependency.getType()).getTarget();
				
			getViewsForCorrespondingCodeSyncElement(target, diagram, list);		
		}	
		if (list.size() > 0) {
			getDiagramEditorStatefulService().client_selectObjects(
					context.getCommunicationChannel(), 
					getDiagramEditorStatefulService().getEditableResource(editorResourcePath).getEditorStatefulClientId(), 
					list);
		}
	}
	
	public void selectWizardElementsFromDiagram(ServiceInvocationContext context, String editorResourcePath, List<List<PathFragment>> paths) {
		Resource resource = getCodeSyncMappingResource(editorResourcePath);
		
		Diagram diagram = getDiagramFromEditableResourcePath(editorResourcePath);
		
		List<String> list = new ArrayList<String>();
		for (List<PathFragment> path : paths) {
			CodeSyncElement source = getWizardElementFromPath(resource, path);
			getViewsForCorrespondingCodeSyncElement(source, diagram, list);
		}
		if (list.size() > 0) {
			getDiagramEditorStatefulService().client_selectObjects(
					context.getCommunicationChannel(), 
					getDiagramEditorStatefulService().getEditableResource(editorResourcePath).getEditorStatefulClientId(), 
					list);
		}
	}
	
	@RemoteInvocation
	public boolean addWizardElement(ServiceInvocationContext context, String editorResourcePath, boolean addWizardAttribute, List<PathFragment> parentPath) {	
		Resource resource = getCodeSyncMappingResource(editorResourcePath);
		
		CodeSyncElement codeSyncElement = null;
		CodeSyncElement parentCodeSyncElement = null;
		if (addWizardAttribute) {	
			codeSyncElement = CodeSyncOperationsService.getInstance().create(CodeSyncPlugin.WIZARD_ATTRIBUTE);
			parentCodeSyncElement = getWizardElementFromPath(resource, parentPath);
		} else {	
			codeSyncElement = CodeSyncOperationsService.getInstance().create(CodeSyncPlugin.WIZARD_ELEMENT);
			parentCodeSyncElement = AddNewExtension_TopLevelElement.getOrCreateCodeSyncElementForLocation(resource, "/wizardElements".split("/"));			
		}		
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
		
		CodeSyncOperationsService.getInstance().setKeyFeatureValue(codeSyncElement,  descriptor.getDefaultName());
		add(parentCodeSyncElement, codeSyncElement);
					
		return true;
	}
	
}
