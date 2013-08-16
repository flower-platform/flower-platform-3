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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.flowerplatform.common.ied.InplaceEditorLabelParseResult;
import org.flowerplatform.common.ied.InplaceEditorLabelParser;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.java.JavaInplaceEditorProvider;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
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
	 * @param context
	 * @param viewId
	 * @param label
	 */
	public void addNew_attribute(ServiceInvocationContext context, String viewId, String label) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		CodeSyncElement clsCse = (CodeSyncElement) cls.getDiagrammableElement();
		
		CodeSyncElement attributeCse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		attributeCse.setType(JavaAttributeModelAdapter.ATTRIBUTE);
		// this is a new element => set added flag
		attributeCse.setAdded(true);
		Attribute attribute = AstCacheCodeFactory.eINSTANCE.createAttribute();
		attribute.setCodeSyncElement(attributeCse);
		
		processAttribute(attributeCse, attribute, label);
		
//		int i = 0;
//		boolean exists = true;
//		while (exists) {
//			i++;
//			exists = false;
//			for (CodeSyncElement child : clsCse.getChildren()) {
//				if (child.getName().equals(attributeCse.getName() + i)) {
//					exists = true;
//					break;
//				}
//			}
//		}
//		attributeCse.setName(attributeCse.getName() + i);
		clsCse.getChildren().add(attributeCse);
	}
	
	protected void processAttribute(CodeSyncElement attributeCse, Attribute attribute, String label) {
		InplaceEditorLabelParseResult result = labelParser.parseAttributeLabel(label);
		setName(attributeCse, result.getName());
		setInitializer(attributeCse, attribute, result.getDefaultValue());
		setType(attributeCse, attribute, result.getType());
		setVisibility(attributeCse, attribute, result.getVisibilityCharacter());
	}
	
	public void addNew_operation(ServiceInvocationContext context, String viewId, String label) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		CodeSyncElement clsCse = (CodeSyncElement) cls.getDiagrammableElement();
		
		CodeSyncElement operationCse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		Operation operation = AstCacheCodeFactory.eINSTANCE.createOperation();
		operation.setCodeSyncElement(operationCse);
		
		processOperation(operationCse, operation, label);
		
		int i = 0;
		boolean exists = true;
		while (exists) {
			i++;
			exists = false;
			StringBuilder builder = new StringBuilder(operationCse.getName());
			builder.insert(builder.indexOf("("), i);
			for (CodeSyncElement child : clsCse.getChildren()) {
				if (child.getName().equals(builder.toString())) {
					exists = true;
					break;
				}
			}
		}
		StringBuilder builder = new StringBuilder(operationCse.getName());
		builder.insert(builder.indexOf("("), i);
		operationCse.setName(builder.toString());
		clsCse.getChildren().add(operationCse);
	}
	
	protected void processOperation(CodeSyncElement operationCse, Operation operation, String label) {
		setVisibility(operationCse, operation, label.charAt(0));
		label = label.substring(1);
		int lastIndexOfColon = label.lastIndexOf(":");
		String name = label.substring(0, lastIndexOfColon);
		int indexOfBracket = name.indexOf("(");
		operationCse.setName(name.substring(0, indexOfBracket) + "()");
		String[] parameters = name.substring(indexOfBracket + 1, name.length() -1)
				.split(",");
		operation.getParameters().clear();
		for (String parameter : parameters) {
			Parameter param = AstCacheCodeFactory.eINSTANCE.createParameter();
			param.setType(parameter);
			operation.getParameters().add(param);
		}
		operation.setType(label.substring(lastIndexOfColon + 1));
		operationCse.setType(JavaOperationModelAdapter.OPERATION);
	}
	
	public void deleteView(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context, viewId);
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		if (cse.getType().equals(JavaAttributeModelAdapter.ATTRIBUTE) ||
				cse.getType().equals(JavaOperationModelAdapter.OPERATION)) {
			CodeSyncElement cls = (CodeSyncElement) cse.eContainer();
			cls.getChildren().remove(cse);
		}
		if (cse.getType().equals(JavaTypeModelAdapter.CLASS)) {
			View parent = (View) view.eContainer();
			parent.getPersistentChildren().remove(view);
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
		Modifier oldModifier = getVisibility(modifiableElement);
		EStructuralFeature feature = AstCacheCodePackage.eINSTANCE.getModifiableElement_Modifiers();
		Collection<ExtendedModifier> oldModifiers = EcoreUtil.copyAll((Collection) getValue(element, feature));
		if (oldModifier == null || oldModifier.getType() != type) {
			Modifier modifier = AstCacheCodeFactory.eINSTANCE.createModifier();
			modifier.setType(type);
			modifiableElement.getModifiers().remove(oldModifier);
			modifiableElement.getModifiers().add(modifier);
		}
		if (!element.isAdded()) {
			Collection<ExtendedModifier> newModifiers = EcoreUtil.copyAll(modifiableElement.getModifiers());
			CodeSyncPlugin.getInstance().createAndAddFeatureChange(element, feature, oldModifiers, newModifiers);
		}
	}
	
	protected Modifier getVisibility(ModifiableElement modifiableElement) {
		for (ExtendedModifier modifier : modifiableElement.getModifiers()) {
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
	
	///////////////////////
	// Utils
	///////////////////////
	
	protected Object getValue(CodeSyncElement element, EStructuralFeature feature) {
		return CodeSyncPlugin.getInstance().getFeatureValue(element, feature);
	}
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected View getViewById(ServiceInvocationContext context, String viewId) {
		return (View) getEditableResource(context).getEObjectById(viewId);
	}
	
}
