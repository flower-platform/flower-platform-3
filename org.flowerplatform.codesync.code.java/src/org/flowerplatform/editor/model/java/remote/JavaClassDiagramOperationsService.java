package org.flowerplatform.editor.model.java.remote;

import java.util.List;

import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.EditorModelPlugin;
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
import com.crispico.flower.mp.model.astcache.code.AstCacheCodeFactory;
import com.crispico.flower.mp.model.astcache.code.Attribute;
import com.crispico.flower.mp.model.astcache.code.ModifiableElement;
import com.crispico.flower.mp.model.astcache.code.Modifier;
import com.crispico.flower.mp.model.astcache.code.Operation;
import com.crispico.flower.mp.model.astcache.code.Parameter;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncFactory;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

public class JavaClassDiagramOperationsService {
	
	public static final String SERVICE_ID = "classDiagramOperationsDispatcher";
	
	public static final String ATTRIBUTE_SEPARATOR = "classAttributesCompartmentSeparator";
	
	public static final String OPERATIONS_SEPARATOR = "classOperationsCompartmentSeparator";
	
	public void setInplaceEditorText(ServiceInvocationContext context, String viewId, String text) {
		View view = getViewById(context, viewId);
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		CodeSyncPlugin.getInstance().setFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name(), text);
	}

	public void collapseCompartment(ServiceInvocationContext context, String viewId) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		cls.getPersistentChildren().remove(view);
		notifyProcessors(cls);
	}
	
	public void expandCompartment_attributes(ServiceInvocationContext context, String viewId) {
		View cls = getViewById(context, viewId);
		// TODO test if there's already a separator? normally this shouldn't be available on client side in this case
		Node separator = NotationFactory.eINSTANCE.createNode();
		separator.setViewType(ATTRIBUTE_SEPARATOR);
		// add it after the class title
		cls.getPersistentChildren().add(1, separator);
		notifyProcessors(cls);
	}
	
	public void expandCompartment_operations(ServiceInvocationContext context, String viewId) {
		View cls = getViewById(context, viewId);
		Node separator = NotationFactory.eINSTANCE.createNode();
		separator.setViewType(OPERATIONS_SEPARATOR);
		// add at the end of the list
		cls.getPersistentChildren().add(separator);
		notifyProcessors(cls);
	}
	
	public void addNew_attribute(ServiceInvocationContext context, String viewId, String label) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		CodeSyncElement clsCse = (CodeSyncElement) cls.getDiagrammableElement();
		
		CodeSyncElement attributeCse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		Attribute attribute = AstCacheCodeFactory.eINSTANCE.createAttribute();
		attribute.setCodeSyncElement(attributeCse);
		
		label = setVisibility(attribute, label);
		String[] info = label.split(":");
		attributeCse.setName(info[0]);
		attributeCse.setType(JavaAttributeModelAdapter.ATTRIBUTE);
		attribute.setType(info[1]);
		
		clsCse.getChildren().add(attributeCse);
	}
	
	public void addNew_operation(ServiceInvocationContext context, String viewId, String label) {
		View view = getViewById(context, viewId);
		View cls = (View) view.eContainer();
		CodeSyncElement clsCse = (CodeSyncElement) cls.getDiagrammableElement();
		
		CodeSyncElement operationCse = CodeSyncFactory.eINSTANCE.createCodeSyncElement();
		Operation operation = AstCacheCodeFactory.eINSTANCE.createOperation();
		operation.setCodeSyncElement(operationCse);
		
		label = setVisibility(operation, label);
		int lastIndexOfColon = label.lastIndexOf(":");
		String name = label.substring(0, lastIndexOfColon);
		int indexOfBracket = name.indexOf("(");
		operationCse.setName(name.substring(0, indexOfBracket) + "()");
		String[] parameters = name.substring(indexOfBracket + 1, name.length() -1)
				.split(",");
		for (String parameter : parameters) {
			Parameter param = AstCacheCodeFactory.eINSTANCE.createParameter();
			String[] info2 = parameter.split(":");
			param.setName(info2[0]);
			param.setType(info2[1]);
			operation.getParameters().add(param);
		}
		operation.setType(label.substring(lastIndexOfColon + 1));
		operationCse.setType(JavaOperationModelAdapter.OPERATION);
		
		clsCse.getChildren().add(operationCse);
	}
	
	public void addNewConnection(ServiceInvocationContext context, String diagramId, String sourceViewId, String targetViewId) {
		View sourceView = getViewById(context, sourceViewId);
		View targetView = getViewById(context, targetViewId);
		Diagram diagram = (Diagram) getViewById(context, diagramId);
		
		Edge edge = NotationFactory.eINSTANCE.createEdge();
		edge.setViewType("scenarioInterraction");
		edge.setSource(sourceView);
		edge.setTarget(targetView);
		diagram.getPersistentEdges().add(edge);
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
	
	protected void notifyProcessors(View view) {
		List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessors(view.getViewType());
		if (processors != null) {
			for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
				processor.processFeatureChanges(view.getDiagrammableElement(), null, view, null);
			}
		}
	}
}
