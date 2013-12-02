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
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.flowerplatform.codesync.config.extension.AddNewExtension;
import org.flowerplatform.codesync.config.extension.InplaceEditorExtension;
import org.flowerplatform.codesync.config.extension.InplaceEditorParseException;
import org.flowerplatform.codesync.processor.RelationsChangesDiagramProcessor;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.command.DisplaySimpleMessageClientCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.RemoteInvocation;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.CategorySeparator;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.NotationPackage;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncDiagramOperationsService {

	public static final String VIEW = "view";
	public static final String PARENT_CODE_SYNC_ELEMENT = "parentCodeSyncElement";
	public static final String PARENT_VIEW = "parentView";

	public static final String ID = "codeSyncDiagramOperationsService";
	
	public static CodeSyncDiagramOperationsService getInstance() {
		return (CodeSyncDiagramOperationsService) CommunicationPlugin.getInstance()
				.getServiceRegistry().getService(ID);
	}
	
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
		return addOnDiagram(context.getAdditionalData(), diagramId, viewIdOfParent, codeSyncElement, parameters);
	}

	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public String addOnDiagram(Map<String, Object> context, String diagramId, String viewIdOfParent, CodeSyncElement codeSyncElement, Map<String, Object> parameters) {
		// create view			
		View parentView = viewIdOfParent == null ? null : getViewById(context, viewIdOfParent);
		
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		
		// run all AddNewExtensions
		Resource codeSyncMappingResource = getCodeSyncMappingResource(getEditableResource(context));
		for (AddNewExtension addNewExtension : CodeSyncPlugin.getInstance().getAddNewExtensions()) {
			if (!addNewExtension.addNew(codeSyncElement, parentView, codeSyncMappingResource, parameters)) {
				// element created, don't allow other extensions to perform add logic
				break;
			}
		}	
			
		CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());
		
		Node view = (Node) parameters.get(VIEW);
		
		// add to diagram
		if (parameters.containsKey(PARENT_VIEW)) {
			parentView = (View) parameters.get(PARENT_VIEW);
		}
		if (parentView == null && viewIdOfParent == null) {
			parentView = getViewById(context, diagramId);
		}
		parentView.getPersistentChildren().add(view);
		view.setViewType(parentView.getViewType() + "." + codeSyncElement.getType());
				
		// add to parent
		if (descriptor.getCreateCodeSyncElement()) { // can create and add codeSyncElement to resource	
			view.setDiagrammableElement(codeSyncElement);
			if (codeSyncElement.eContainer() == null) {
				CodeSyncElement parentCodeSyncElement = null;
				if (parameters.containsKey(PARENT_CODE_SYNC_ELEMENT)) {
					parentCodeSyncElement = (CodeSyncElement) parameters.get(PARENT_CODE_SYNC_ELEMENT);
				} else {
					parentCodeSyncElement = (CodeSyncElement) parentView.getDiagrammableElement();
				}
				CodeSyncOperationsService.getInstance().add(parentCodeSyncElement, codeSyncElement);
			}
		}		
		// return ID of the view
		return view.getIdBeforeRemoval();		
	}
	
	public List<CodeSyncElementDescriptor> getChildrenCategories(String codeSyncType) {
		CodeSyncElementDescriptor parentDescriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncType);
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
	
	/**
	 * Only removes the view from the diagram, does not delete the model element.
	 */
	@RemoteInvocation
	public void removeView(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context.getAdditionalData(), viewId);
		// doing this because the idBeforeRemoval is not set during binary deserialization
		view.setIdBeforeRemoval(view.eResource().getURIFragment(view));
		if (view instanceof Edge) {
			Diagram diagram = getDiagram(context.getAdditionalData());
			diagram.getPersistentEdges().remove(view);
			Edge edge = (Edge) view;
			edge.getSource().getSourceEdges().remove(edge);
			edge.getTarget().getTargetEdges().remove(edge);
		} else {
			View parentView = (View) view.eContainer();
			parentView.getPersistentChildren().remove(view);
		}
	}
	
	@RemoteInvocation
	public void collapseCompartment(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context.getAdditionalData(), viewId);
		View parentCompartment = (View) view.eContainer();
		parentCompartment.getPersistentChildren().remove(view);
	}
	
	@RemoteInvocation
	public void expandCompartment(ServiceInvocationContext context, String viewId, String category) {
		View view = getViewById(context.getAdditionalData(), viewId);
		for (CodeSyncElementDescriptor descriptor : getCodeSyncElementDescriptors()) {
			if (category.equals(descriptor.getCategory())) {
				addCategorySeparator(view, (CodeSyncElement) view.getDiagrammableElement(), descriptor);
				break;
			}
		}
	}
	
	/**
	 * @author Mariana Gheorghe
	 * @author Cristina Constantinescu
	 */
	public void addCategorySeparator(View view, CodeSyncElement codeSyncElement, CodeSyncElementDescriptor descriptor) {
		CategorySeparator categorySeparator = NotationFactory.eINSTANCE.createCategorySeparator();
		categorySeparator.setViewType("categorySeparator");
		categorySeparator.setCategory(descriptor.getCategory());
		categorySeparator.setNewChildCodeSyncType(descriptor.getCodeSyncType());
		String image = descriptor.getIconUrl();
		if (image != null) {
			String codeSyncPackage = CodeSyncPlugin.getInstance()
					.getBundleContext().getBundle().getSymbolicName();
			if (!image.startsWith("/")) {
				image = "/" + image;
			}
			image = codeSyncPackage + image;
		}
		categorySeparator.setNewChildIcon(image);
		
		CodeSyncElementDescriptor viewDescriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(codeSyncElement.getType());				
		int index = 0;
		for (View child : view.getPersistentChildren()) {
			CodeSyncElement childElement = (CodeSyncElement) child.getDiagrammableElement();
			if (childElement == null) { 
				boolean foundOrderIndex = false;
				// CategorySeparator, search for orderIndex in list of children that has this set as category
				for (CodeSyncElementDescriptor descr : CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors()) {
					for (String codeSyncTypeCategory : descr.getCodeSyncTypeCategories()) {
						if (viewDescriptor.getChildrenCodeSyncTypeCategories().contains(codeSyncTypeCategory)) {
							if (((CategorySeparator) child).getCategory().equals(descr.getCategory())) {
								if (descr.getOrderIndex() <= descriptor.getOrderIndex()) {
									index++;
									foundOrderIndex = true;
									break;
								}
							}							
						}
					}
					if (foundOrderIndex) {
						break;
					}
				}		
			} else {
				CodeSyncElementDescriptor childDescriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(childElement.getType());
				if (childDescriptor.getOrderIndex() <= descriptor.getOrderIndex()) {
					index++;
				}
			}
		}
		if (index != 0) {
			view.getPersistentChildren().add(index, categorySeparator);
		} else {
			view.getPersistentChildren().add(categorySeparator);
		}
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	@RemoteInvocation
	public String getInplaceEditorText(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context.getAdditionalData(), viewId);
				
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (InplaceEditorExtension extension : CodeSyncPlugin.getInstance().getInplaceEditorExtensions()) {
			try {
				if (!extension.getInplaceEditorText(view, parameters)) {
					// the extension provided the right text, so don't continue with the others
					break;
				}
			} catch (InplaceEditorParseException e) {
				// the extension had to provide the right text, but something happened
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								e.getMessage(), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
			}
		}
			
		if (!parameters.containsKey(InplaceEditorExtension.VIEW_TEXT)) {
			throw new RuntimeException("'parameters' doesn't contain key InplaceEditorExtension.VIEW_TEXT!");
		}
		return (String) parameters.get(InplaceEditorExtension.VIEW_TEXT);
	}
	
	/**
	 * @author Cristina Constantinescu
	 */
	@RemoteInvocation
	public void setInplaceEditorText(ServiceInvocationContext context, String viewId, String text) {
		View view = getViewById(context.getAdditionalData(), viewId);
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		for (InplaceEditorExtension extension : CodeSyncPlugin.getInstance().getInplaceEditorExtensions()) {
			try {
				if (!extension.setInplaceEditorText(view, text, parameters)) {	
					// the extension set the new text, so don't continue with the others
					break;
				}
			} catch (InplaceEditorParseException e) {
				// the extension had to set the new text, but something happened
				context.getCommunicationChannel().appendOrSendCommand(
						new DisplaySimpleMessageClientCommand(
								CommonPlugin.getInstance().getMessage("error"), 
								e.getMessage(), 
								DisplaySimpleMessageClientCommand.ICON_ERROR));
			}
		}
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
	}
	
	public void displayMissingRelations(ServiceInvocationContext context, String viewId, boolean addMissingElements) {
		CodeSyncElement cse = (CodeSyncElement) getViewById(context.getAdditionalData(), viewId).getDiagrammableElement();
		List<View> views = getViewsForElement(cse);
		
		// add edges for relations where this element is the source
		for (View view : views) {
			addEdgesForOutgoingRelations(cse, view, addMissingElements, context.getAdditionalData());
		}
		
		// add edges for relations where this element is the target
		addEdgesForIncomingRelations(cse, addMissingElements, context.getAdditionalData());
	}
	
	/**
	 * Adds new {@link Edge}s for each {@link Relation} that starts from {@code object}, 
	 * from the {@code associatedViewOnOpenDiagram} to all the views corresponding to the relation's target.
	 */
	public void addEdgesForOutgoingRelations(EObject object, View associatedViewOnOpenDiagram, boolean addMissingElements, Map<String, Object> context) {
		Diagram diagram = getDiagram(context);
		List<Relation> relations = ((CodeSyncElement) object).getRelations();
		for (Relation relation : relations) {
			List<View> views = getViewsForElement(relation.getTarget());
			// if there are no views for the target and addMissingElements is true
			if (addMissingElements && views.size() == 0) {
				String viewId = addOnDiagram(context, diagram.eResource().getURIFragment(diagram), null, relation.getTarget(), null);
				views.add(getViewById(context, viewId));
			}
			for (View target : views) {
				if (getEdge(associatedViewOnOpenDiagram, target) == null) {
					createEdge(relation, associatedViewOnOpenDiagram, target, diagram);
				}
			}
		}
	}
	
	/**
	 * Adds new {@link Edge}s for each {@link Relation} that ends in {@code object},
	 * from all the views corresponding to the relation's source to to all the views corresponding to the relation's target.
	 */
	public void addEdgesForIncomingRelations(EObject object, boolean addMissingElements, Map<String, Object> context) {
		CodeSyncElement cse = (CodeSyncElement) object;
		Diagram diagram = getDiagram(context);
		for (EObject eObject : getInverseReferencesForElement(cse, CodeSyncPackage.eINSTANCE.getRelation_Target())) {
			Relation relation = (Relation) eObject;
			List<View> sourceViews = getViewsForElement(relation.getSource());
			if (addMissingElements && sourceViews.size() == 0) {
				String viewId = addOnDiagram(context, diagram.eResource().getURIFragment(diagram), null, relation.getSource(), null);
				sourceViews.add(getViewById(context, viewId));
			}
			for (View sourceView : sourceViews) {
				for (View targetView : getViewsForElement(cse)) {
					if (getEdge(sourceView, targetView) == null) {
						createEdge(relation, sourceView, targetView, diagram);
					}
				}
			}
		}
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
	
	/**
	 * Gets the {@link Edge} with the specified <code>source</code> and <code>target</code>,
	 * if it exists.
	 */
	public Edge getEdge(View source, View target) {
		for (Edge edge : source.getSourceEdges()) {
			if (edge.getSource().equals(source) && edge.getTarget().equals(target)) {
				return edge;
			}
		}
		return null;
	}
	
	/**
	 * Some views (e.g. class title) cannot have edges even though their diagrammable element can have relations.
	 */
	protected boolean acceptsEdges(View view) {
		List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor()
				.getDiagrammableElementFeatureChangesProcessors(view.getViewType());
		for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
			if (processor instanceof RelationsChangesDiagramProcessor) {
				return true;
			}
		}
		return false;
	}
	
	public Diagram getDiagram(Map<String, Object> context) {
		DiagramEditableResource der = (DiagramEditableResource) context.get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
		for (EObject eObject : der.getMainResource().getContents()) {
			if (eObject instanceof Diagram) {
				return (Diagram) eObject;
			}
		}
		throw new RuntimeException("No diagram for " + der.getEditableResourcePath());
	}
	
	/**
	 * Finds all the {@link View}s for <code>element</code> and filters out
	 * elements that cannot accept edges.
	 */
	public List<View> getViewsForElement(CodeSyncElement element) {
		List<View> views = (List<View>) getInverseReferencesForElement(element,
				NotationPackage.eINSTANCE.getView_DiagrammableElement());
		// filter out views that can't have edges
		for (Iterator<View> iterator = views.iterator(); iterator.hasNext();) {
			View view = (View) iterator.next();
			if (!acceptsEdges(view)) {
				iterator.remove();
			}
		}
		return views;
	}
	
	protected List<? extends EObject> getInverseReferencesForElement(CodeSyncElement element, EStructuralFeature feature) {
		List<EObject> result = new ArrayList<EObject>();
		ECrossReferenceAdapter adapter = ECrossReferenceAdapter.getCrossReferenceAdapter(element);
		for (Setting setting : adapter.getNonNavigableInverseReferences(element)) {
			if (feature.equals(setting.getEStructuralFeature())) {
				result.add(setting.getEObject());
			}
		}
		return result;
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