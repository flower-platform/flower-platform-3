package org.flowerplatform.editor.model.java.remote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.DiagramUpdaterChangeProcessorContext;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.Node;
import org.flowerplatform.emf_model.notation.NotationFactory;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaAttributeModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaOperationModelAdapter;
import com.crispico.flower.mp.codesync.code.java.adapter.JavaTypeModelAdapter;
import com.crispico.flower.mp.model.astcache.code.AstCacheCodeFactory;
import com.crispico.flower.mp.model.astcache.code.Attribute;
import com.crispico.flower.mp.model.astcache.code.ModifiableElement;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.Operation;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.ScenarioElement;

public class JavaClassDiagramOperationsService {
	
	public static final String SERVICE_ID = "classDiagramOperationsDispatcher";
	
	public static final String ATTRIBUTE_SEPARATOR = "classAttributesCompartmentSeparator";
	
	public static final String OPERATIONS_SEPARATOR = "classOperationsCompartmentSeparator";
	
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
		notifyProcessors(context, view);
	}

	public void collapseCompartment(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		cls.getPersistentChildren().remove(view);
		notifyProcessors(context, cls);
	}
	
	public void expandCompartment_attributes(ServiceInvocationContext context, String viewId) {
		View cls = getViewById(context, viewId);
		// TODO test if there's already a separator? normally this shouldn't be available on client side in this case
		Node separator = NotationFactory.eINSTANCE.createNode();
		separator.setViewType(ATTRIBUTE_SEPARATOR);
		// add it after the class title
		cls.getPersistentChildren().add(1, separator);
		notifyProcessors(context, cls);
	}
	
	public void expandCompartment_operations(ServiceInvocationContext context, String viewId) {
		View cls = getViewById(context, viewId);
		Node separator = NotationFactory.eINSTANCE.createNode();
		separator.setViewType(OPERATIONS_SEPARATOR);
		// add at the end of the list
		cls.getPersistentChildren().add(separator);
		notifyProcessors(context, cls);
	}
	
	public void addNew_attribute(ServiceInvocationContext context, String viewId, String label) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		CodeSyncElement clsCse = (CodeSyncElement) cls.getDiagrammableElement();
		
		CodeSyncElement attributeCse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		Attribute attribute = AstCacheCodeFactory.eINSTANCE.createAttribute();
		attribute.setCodeSyncElement(attributeCse);
		
		processAttribute(attributeCse, attribute, label);
		
		int i = 0;
		boolean exists = true;
		while (exists) {
			i++;
			exists = false;
			for (CodeSyncElement child : clsCse.getChildren()) {
				if (child.getName().equals(attributeCse.getName() + i)) {
					exists = true;
					break;
				}
			}
		}
		attributeCse.setName(attributeCse.getName() + i);
		clsCse.getChildren().add(attributeCse);
	}
	
	protected void processAttribute(CodeSyncElement attributeCse, Attribute attribute, String label) {
		label = setVisibility(attribute, label);
		String[] info = label.split(":");
		attributeCse.setName(info[0]);
		attributeCse.setType(JavaAttributeModelAdapter.ATTRIBUTE);
		attribute.setType(info[1]);
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
		label = setVisibility(operation, label);
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
	
	protected String setVisibility(ModifiableElement element, String label) {
		int type = 0;
		char visibility = label.charAt(0);
		boolean result = false;
		switch (visibility) {
		case '+':
			type = org.eclipse.jdt.core.dom.Modifier.PUBLIC;
			result = true;
			break;
		case '-':
			type = org.eclipse.jdt.core.dom.Modifier.PRIVATE;
			result = true;
			break;
		case '#':
			type = org.eclipse.jdt.core.dom.Modifier.PROTECTED;
			result = true;
			break;
		case '~':
			type = org.eclipse.jdt.core.dom.Modifier.NONE;
			result = true;
			break;
		default:
			type = org.eclipse.jdt.core.dom.Modifier.NONE;
			break;
		}
		Modifier modifier = AstCacheCodeFactory.eINSTANCE.createModifier();
		modifier.setType(type);
		element.getModifiers().add(modifier);
		if (result) {
			return label.substring(1);
		}
		return label;
	}
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected View getViewById(ServiceInvocationContext context, String viewId) {
		return (View) getEditableResource(context).getEObjectById(viewId);
	}
	
	protected void notifyProcessors(ServiceInvocationContext context, View view) {
		Map<String, Object> processingContext = new HashMap<String, Object>();
		processingContext.put(DiagramEditorStatefulService.PROCESSING_CONTEXT_EDITABLE_RESOURCE, getEditableResource(context));
		DiagramUpdaterChangeProcessorContext.getDiagramUpdaterChangeDescriptionProcessingContext(processingContext, true);
		
		List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessors(view.getViewType());
		if (processors != null) {
			for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
				processor.processFeatureChanges(view.getDiagrammableElement(), null, view, processingContext);
			}
		}
	}
}
