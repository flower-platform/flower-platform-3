package org.flowerplatform.editor.mindmap.remote;

import java.util.List;

import org.flowerplatform.codesync.remote.CodeSyncElementFeatureChangesProcessor;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.change_processor.IDiagrammableElementFeatureChangesProcessor;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.MindMapNode;
import org.flowerplatform.emf_model.notation.View;

/**
 * @author Cristina Constantinescu
 */
public class MindMapDiagramOperationsService {

	private static final String SERVICE_ID = "mindMapDiagramOperationsService";
	
	public static MindMapDiagramOperationsService getInstance() {
		return (MindMapDiagramOperationsService) CommunicationPlugin.getInstance().getServiceRegistry().getService(SERVICE_ID);
	}
	
	
	public void setExpanded(ServiceInvocationContext context, String viewId, boolean expanded) {
		MindMapNode node = getMindMapNodeById(context, viewId);
		node.setExpanded(expanded);
		
		if (!expanded) {
			node.getPersistentChildren().clear();
		}
		notifyProcessors(node);
	}
	
	protected DiagramEditableResource getEditableResource(ServiceInvocationContext context) {
		return (DiagramEditableResource) context.getAdditionalData().get(DiagramEditorStatefulService.ADDITIONAL_DATA_EDITABLE_RESOURCE);
	}
	
	protected MindMapNode getMindMapNodeById(ServiceInvocationContext context, String viewId) {
		return (MindMapNode) getEditableResource(context).getEObjectById(viewId);
	}
	
	protected void notifyProcessors(View view) {
		List<IDiagrammableElementFeatureChangesProcessor> processors = EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().getDiagrammableElementFeatureChangesProcessors(view.getViewType());
		if (processors != null) {
			for (IDiagrammableElementFeatureChangesProcessor processor : processors) {
				if (processor instanceof CodeSyncElementFeatureChangesProcessor) {
					processor.processFeatureChanges(view.getDiagrammableElement(), null, view, null);
				}
			}
		}
	}
}
