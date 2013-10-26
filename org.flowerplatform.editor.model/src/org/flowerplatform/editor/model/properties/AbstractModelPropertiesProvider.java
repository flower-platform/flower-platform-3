package org.flowerplatform.editor.model.properties;

import org.eclipse.emf.ecore.EObject;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.command.AbstractServerCommand;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.communication.stateful_service.ServiceInvocationContextUpgradedToStatefulServiceInvocationContext;
import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.SelectedItem;

/**
 * @author Cristian Spiescu
 */
public abstract class AbstractModelPropertiesProvider<RES_SEL_ITEM extends EObject> implements IPropertiesProvider {

	protected DiagramEditorStatefulService getDiagramEditorStatefulService(DiagramSelectedItem selectedItem) {
		String diagramEditorStatefulServiceId = selectedItem.getEditorStatefulServiceId();
		return (DiagramEditorStatefulService) CommunicationPlugin.getInstance().getServiceRegistry().getService(diagramEditorStatefulServiceId);
		
	}
	
	@SuppressWarnings("unchecked")
	public RES_SEL_ITEM resolveSelectedItem(DiagramSelectedItem selectedItem) {
		return (RES_SEL_ITEM) getViewById(selectedItem);
	}
	
	protected View getViewById(DiagramSelectedItem selectedItem) {
		String diagramEditableResourcePath = selectedItem.getDiagramEditableResourcePath();
		String id = selectedItem.getXmiID();
		
		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) getDiagramEditorStatefulService(selectedItem).getEditableResource(diagramEditableResourcePath);
		return (View) diagramEditableResource.getEObjectById(id);		
	}
	
	@Override
	public boolean setProperty(ServiceInvocationContext context, final SelectedItem selectedItem, final String propertyName, final Object propertyValue) {
		final RES_SEL_ITEM resolvedSelectedItem = resolveSelectedItem((DiagramSelectedItem) selectedItem);
		return getDiagramEditorStatefulService((DiagramSelectedItem) selectedItem).attemptUpdateEditableResourceContent(
				new ServiceInvocationContextUpgradedToStatefulServiceInvocationContext(context),
				((DiagramSelectedItem) selectedItem).getDiagramEditableResourcePath(), 
				new AbstractServerCommand() {
					@Override
					public void executeCommand() {
						doSetProperty((DiagramSelectedItem) selectedItem, resolvedSelectedItem, propertyName, propertyValue);
					}
				});
	}

    /**
     * This method is executed under the {@link DiagramEditorStatefulService} shell. I.e. lock logic happens, change recording, etc.
     */
    abstract protected void doSetProperty(DiagramSelectedItem selectedItem, RES_SEL_ITEM resolvedSelectedItem, String propertyName, Object propertyValue);

}
