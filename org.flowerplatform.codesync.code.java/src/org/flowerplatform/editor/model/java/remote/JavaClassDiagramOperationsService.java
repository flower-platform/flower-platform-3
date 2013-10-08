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
package org.flowerplatform.editor.model.java.remote;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.flowerplatform.common.ied.InplaceEditorLabelParseResult;
import org.flowerplatform.common.ied.InplaceEditorLabelParser;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.java.JavaClassChildProcessor;
import org.flowerplatform.editor.model.java.JavaInplaceEditorProvider;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.web.projects.remote.ProjectsService;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.CodeSyncCodePlugin;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAttributeModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaOperationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodeFactory;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage;
import com.crispico.flower.mp.model.astcache.code.Attribute;
import com.crispico.flower.mp.model.astcache.code.ExtendedModifier;
import com.crispico.flower.mp.model.astcache.code.ModifiableElement;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.Operation;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.astcache.code.TypedElement;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

/**
 * @author Cristian Spiescu
 * @author Mariana Gheorghe
 */
public class JavaClassDiagramOperationsService {
	
	public static final String SERVICE_ID = "classDiagramOperationsDispatcher";
	
	public static final String ATTRIBUTE_SEPARATOR = "classAttributesCompartmentSeparator";
	
	public static final String OPERATIONS_SEPARATOR = "classOperationsCompartmentSeparator";
	
	private InplaceEditorLabelParser labelParser = new InplaceEditorLabelParser(new JavaInplaceEditorProvider());
	
	public void setInplaceEditorText(ServiceInvocationContext context, String viewId, String text) {
		View view = getViewById(context, viewId);
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		if (cse.getType().equals(JavaAttributeModelAdapter.ATTRIBUTE)) {
			processAttribute(cse, (Attribute) cse.getAstCacheElement(), text);
		}
		if (cse.getType().equals(JavaOperationModelAdapter.OPERATION)) {
			processOperation(cse, (Operation) cse.getAstCacheElement(), text);
		}
		if (cse.getType().equals(JavaTypeModelAdapter.CLASS)) {
			CodeSyncPlugin.getInstance().setFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name(), text);
		}
	}
	
	public String getInplaceEditorText(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context, viewId);
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		for (IDiagrammableElementFeatureChangesProcessor processor : EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor()
				.getDiagrammableElementFeatureChangesProcessors(view.getViewType())) {
			if (processor instanceof JavaClassChildProcessor) {
				return ((JavaClassChildProcessor) processor).getLabel(cse, true);
			}
		}
		throw new RuntimeException("Cannot edit this element!");
	}

	public void collapseCompartment(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		cls.getPersistentChildren().remove(view);
	}
	
	public void expandCompartment_attributes(ServiceInvocationContext context, String viewId) {
		View cls = getViewById(context, viewId);
		// TODO test if there's already a separator? normally this shouldn't be available on client side in this case
		Node separator = NotationFactory.eINSTANCE.createNode();
		separator.setViewType(ATTRIBUTE_SEPARATOR);
		// add it after the class title
		cls.getPersistentChildren().add(1, separator);
	}
	
	public void expandCompartment_operations(ServiceInvocationContext context, String viewId) {
		View cls = getViewById(context, viewId);
		Node separator = NotationFactory.eINSTANCE.createNode();
		separator.setViewType(OPERATIONS_SEPARATOR);
		// add at the end of the list
		cls.getPersistentChildren().add(separator);
	}
	
	/**
	 * Creates a new {@link CodeSyncElement} with an associated {@link Attribute}.
	 * @author Sebastian Solomon
	 */
	public void addNew_attribute(ServiceInvocationContext context, String viewId, String label) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		CodeSyncElement clsCse = (CodeSyncElement) cls.getDiagrammableElement();
		
		if (!clsCse.isDeleted()){ //if isDeleted(), don't add attribute
			CodeSyncElement attributeCse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			attributeCse.setType(JavaAttributeModelAdapter.ATTRIBUTE);
			// this is a new element => set added flag
			attributeCse.setAdded(true);
			Attribute attribute = AstCacheCodeFactory.eINSTANCE.createAttribute();
			attribute.setCodeSyncElement(attributeCse);
		
			processAttribute(attributeCse, attribute, label);
			
			DiagramEditableResource der = getEditableResource(context);
			ResourceSet resourceSet = der.getResourceSet();
			IProject project = ProjectsService.getInstance().getProjectWrapperResourceFromFile(der.getFile()).getProject();
			Resource astCache = CodeSyncCodePlugin.getInstance().getAstCache(project, resourceSet);
			astCache.getContents().add(attribute);
		
			clsCse.getChildren().add(attributeCse);
		
			CodeSyncPlugin.getInstance().propagateParentSyncFalse(attributeCse);
		}
		
	}
	

	protected void processAttribute(CodeSyncElement attributeCse, Attribute attribute, String label) {
		InplaceEditorLabelParseResult result = labelParser.parseAttributeLabel(label);
		setName(attributeCse, result.getName());
		setInitializer(attributeCse, attribute, result.getDefaultValue());
		setType(attributeCse, attribute, result.getType());
		setVisibility(attributeCse, attribute, result.getVisibilityCharacter());
	}
	
	/**
	 * Creates a new {@link CodeSyncElement} with an associated {@link Operation}.
	 * @author Sebastian Solomon
	 */
	public void addNew_operation(ServiceInvocationContext context, String viewId, String label) {
		
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		CodeSyncElement clsCse = (CodeSyncElement) cls.getDiagrammableElement();

		if (!clsCse.isDeleted()){ //if isDeleted(), don't add element
		
			CodeSyncElement operationCse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
			operationCse.setType(JavaOperationModelAdapter.OPERATION);
			// this is a new element => set added flag
			operationCse.setAdded(true);
			Operation operation = AstCacheCodeFactory.eINSTANCE.createOperation();
			operation.setCodeSyncElement(operationCse);
		
			processOperation(operationCse, operation, label);
			
			DiagramEditableResource der = getEditableResource(context);
			ResourceSet resourceSet = der.getResourceSet();
			IProject project = ProjectsService.getInstance().getProjectWrapperResourceFromFile(der.getFile()).getProject();
			Resource astCache = CodeSyncCodePlugin.getInstance().getAstCache(project, resourceSet);
			astCache.getContents().add(operation);
			
			clsCse.getChildren().add(operationCse);
		
			CodeSyncPlugin.getInstance().propagateParentSyncFalse(operationCse);
		}
		
	}
	
	protected void processOperation(CodeSyncElement operationCse, Operation operation, String label) {
		InplaceEditorLabelParseResult result = labelParser.parseOperationLabel(label);
		setName(operationCse, result.getName());
		setType(operationCse, operation, result.getType());
		setVisibility(operationCse, operation, result.getVisibilityCharacter());
		setParameters(operationCse, operation, result.getParameters());
	}
	
	/**
	 * @author Sebastian Solomon
	 */
	public void deleteView(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context, viewId);
		
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		
		if (cse.getType().equals(JavaAttributeModelAdapter.ATTRIBUTE) ||
				cse.getType().equals(JavaOperationModelAdapter.OPERATION)||
				cse.getType().equals(JavaTypeModelAdapter.CLASS)) {
			CodeSyncElement cls = (CodeSyncElement) cse.eContainer();
			if (cse.isAdded()){
				CodeSyncPlugin.getInstance().propagateParentSyncTrue(cse);
				//delete it
				if (cse.getType().equals(JavaTypeModelAdapter.CLASS)){
					View parent = (View) view.eContainer();
					parent.getPersistentChildren().remove(view);
				}else {
					 
					cls.getChildren().remove(cse);
				}
			}else{ //mark as deleted
				cse.setDeleted(true);
				CodeSyncPlugin.getInstance().propagateParentSyncFalse(cse);
				CodeSyncPlugin.getInstance().propageteOnChildDelete(cse); 
			}
				
		}

	}
	
	///////////////////////
	// Model modifications
	///////////////////////
	
	protected void setName(CodeSyncElement element, String newName) {
		EStructuralFeature feature = CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name();
		CodeSyncPlugin.getInstance().setFeatureValue(element, feature, newName);
	}
	
	protected void setType(CodeSyncElement element, TypedElement typedElement, String newType) {
		EStructuralFeature feature = AstCacheCodePackage.eINSTANCE.getTypedElement_Type();
		CodeSyncPlugin.getInstance().setFeatureValue(element, feature, newType);
	}
	
	protected void setInitializer(CodeSyncElement element, Attribute attribute, String newInitializer) {
		EStructuralFeature feature = AstCacheCodePackage.eINSTANCE.getAttribute_Initializer();
		CodeSyncPlugin.getInstance().setFeatureValue(element, feature, newInitializer);
	}
	
	protected void setVisibility(CodeSyncElement element, ModifiableElement modifiableElement, char newVisibility) {
		int type = 0;
		switch (newVisibility) {
		case '+':
			type = org.eclipse.jdt.core.dom.Modifier.PUBLIC;
			break;
		case '-':
			type = org.eclipse.jdt.core.dom.Modifier.PRIVATE;
			break;
		case '#':
			type = org.eclipse.jdt.core.dom.Modifier.PROTECTED;
			break;
			
		default:
			type = org.eclipse.jdt.core.dom.Modifier.NONE;
			break;
		}
		
		Collection<ExtendedModifier> modifiers = EcoreUtil.copyAll(getModifiers(element));
		Modifier modifier = getVisibility(modifiers);
		Modifier newModifier = AstCacheCodeFactory.eINSTANCE.createModifier();
		newModifier.setType(type);
		if (modifier == null || modifier.getType() != type) {
			if (modifier != null) {
				modifiers.remove(modifier);
			}
			modifiers.add(newModifier);
		}
		EStructuralFeature feature = AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers();
		CodeSyncPlugin.getInstance().setFeatureValue(element, feature, modifiers);
	}
	
	protected List<ExtendedModifier> getModifiers(CodeSyncElement codeSyncElement) {
		return (List<ExtendedModifier>) CodeSyncPlugin.getInstance().getFeatureValue(codeSyncElement,
				AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers());
	}
	
	protected Modifier getVisibility(Collection<ExtendedModifier> modifiers) {
		for (ExtendedModifier modifier : modifiers) {
			if (modifier instanceof Modifier) {
				switch (((Modifier) modifier).getType()) {
				case org.eclipse.jdt.core.dom.Modifier.PUBLIC:
				case org.eclipse.jdt.core.dom.Modifier.PRIVATE:
				case org.eclipse.jdt.core.dom.Modifier.PROTECTED:	
				case org.eclipse.jdt.core.dom.Modifier.NONE:
					return (Modifier) modifier;
				}
			}
		}
		return null;
	}
	
	protected void setParameters(CodeSyncElement element, Operation operation, Collection<InplaceEditorLabelParseResult> newParameters) {
		// first create the list of parameters corresponding to newParameters
		List<Parameter> parameters = new BasicEList<>();
		for (InplaceEditorLabelParseResult newParameter : newParameters) {
			Parameter parameter = AstCacheCodeFactory.eINSTANCE.createParameter();
			parameter.setName(newParameter.getName());
			parameter.setType(newParameter.getType());
			parameters.add(parameter);
		}
		
		// compare the new parameters with the current list
		EStructuralFeature feature = AstCacheCodePackage.eINSTANCE.getOperation_Parameters();
		CodeSyncPlugin.getInstance().setFeatureValue(element, feature, parameters);
	}
	
	///////////////////////
	// Utils
	///////////////////////
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected View getViewById(ServiceInvocationContext context, String viewId) {
		return (View) getEditableResource(context).getEObjectById(viewId);
	}
	
}
