package org.flowerplatform.editor.model.java.remote;

import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.View;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

public class JavaClassDiagramOperationsService {
	
	public void setInplaceEditorText(ServiceInvocationContext context, String viewId, String text) {
		DiagramEditableResource editableResource = (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
		View view = (View) editableResource.getEObjectById(viewId);
		CodeSyncElement cse = (CodeSyncElement) view.getDiagrammableElement();
		CodeSyncPlugin.getInstance().setFeatureValue(cse, CodeSyncPackage.eINSTANCE.getCodeSyncElement_Name(), text);
	}

}
