package org.flowerplatform.editor.model.properties;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;

/**
 * @author Cristina Constantinescu
 */
public class DiagramPropertiesProvider implements IPropertiesProvider {

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		List<Property> properties = new ArrayList<Property>();	
		Diagram diagram = getDiagram(selectedItem);
				
		properties.add(new Property("newElementsPath", diagram.getNewElementsPath(), false));
		properties.add(new Property("showNewElementsPathDialog", diagram.isShowNewElementsPathDialog(), false));
		
		return properties;
	}

	@Override
	public void setProperty(SelectedItem selectedItem, String propertyName, Object propertyValue) {
		Diagram diagram = getDiagram(selectedItem);
		
		if (propertyName.equals("newElementsPath")) {
			diagram.setNewElementsPath((String) propertyValue);
		} else if (propertyName.equals("showNewElementsPathDialog")) {
			diagram.setShowNewElementsPathDialog((boolean) propertyValue);
		}
	}
	
	private DiagramEditorStatefulService getDiagramEditorStatefulService(String diagramEditorStatefulServiceId) {
		return (DiagramEditorStatefulService)CommunicationPlugin.getInstance().getServiceRegistry().getService(diagramEditorStatefulServiceId);
	}
	
	private Diagram getDiagram(SelectedItem selectedItem) {
		String diagramEditorStatefulServiceId = ((DiagramSelectedItem) selectedItem).getEditorStatefulServiceId();
		DiagramEditorStatefulService diagramEditorStatefulService = getDiagramEditorStatefulService(diagramEditorStatefulServiceId);	
		String diagramEditableResourcePath = ((DiagramSelectedItem) selectedItem).getDiagramEditableResourcePath();
		String id = ((DiagramSelectedItem) selectedItem).getXmiID();
		
		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) diagramEditorStatefulService.getEditableResource(diagramEditableResourcePath);
		return (Diagram) diagramEditableResource.getEObjectById(id);		
	}
}
