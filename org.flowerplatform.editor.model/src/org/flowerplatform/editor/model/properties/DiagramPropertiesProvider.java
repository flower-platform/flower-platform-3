package org.flowerplatform.editor.model.properties;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.EditorModelPlugin;
import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.properties.remote.Property;

/**
 * @author Cristina Constantinescu
 */

public class DiagramPropertiesProvider extends AbstractModelPropertiesProvider<DiagramSelectedItem, Diagram> {

	public static final String LOCATION_FOR_NEW_ELEMENTS_PROPERTY = "locationForNewElements";
	public static final String SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_PROPERTY = "showLocationForNewElementsDialog";
	
	@Override
	public List<String> getPropertyNames(DiagramSelectedItem selectedItem, Diagram resolvedSelectedItem) {
		List<String> propertiesNames = new ArrayList<String>();
		
		propertiesNames.add(LOCATION_FOR_NEW_ELEMENTS_PROPERTY);
		propertiesNames.add(SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_PROPERTY);

		return propertiesNames;
	}

	@Override
	public Property getProperty(DiagramSelectedItem selectedItem, Diagram diagram, String propertyName) {
		
		switch (propertyName) {
			case LOCATION_FOR_NEW_ELEMENTS_PROPERTY: {
				return new Property()
						.setNameAs(LOCATION_FOR_NEW_ELEMENTS_PROPERTY)
						.setValueAs(diagram.getLocationForNewElements())
						.setTypeAs("StringWithDialog")
						.setReadOnlyAs(true);
			}
			case SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_PROPERTY: {
				return new Property()
						.setNameAs(SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_PROPERTY)
						.setValueAs(diagram.isShowLocationForNewElementsDialog())
						.setTypeAs("Boolean")
						.setReadOnlyAs(false);
			}
		}
		return null;
	}

	/**
	 * @author Cristian Spiescu
	 */
	@Override
	protected void doSetProperty(DiagramSelectedItem selectedItem,
			Diagram resolvedSelectedItem, String propertyName,
			Object propertyValue) {

		if (propertyName.equals(LOCATION_FOR_NEW_ELEMENTS_PROPERTY)) {
			resolvedSelectedItem.setLocationForNewElements((String) propertyValue);
		} else if (propertyName.equals(SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_PROPERTY)) {
			resolvedSelectedItem.setShowLocationForNewElementsDialog((Boolean) propertyValue);
		}

	}
	
	private DiagramEditorStatefulService getDiagramEditorStatefulService(String diagramEditorStatefulServiceId) {
		return (DiagramEditorStatefulService)CommunicationPlugin.getInstance().getServiceRegistry().getService(diagramEditorStatefulServiceId);
	}
	
//	@Override
//	public Diagram resolveSelectedItem(DiagramSelectedItem selectedItem) {
//		String diagramEditorStatefulServiceId = selectedItem.getEditorStatefulServiceId();
//		DiagramEditorStatefulService diagramEditorStatefulService = getDiagramEditorStatefulService(diagramEditorStatefulServiceId);	
//		String diagramEditableResourcePath = selectedItem.getDiagramEditableResourcePath();
//		String id = selectedItem.getXmiID();
//
//		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) diagramEditorStatefulService.getEditableResource(diagramEditableResourcePath);
//		return (Diagram) diagramEditableResource.getEObjectById(id);
//	}

	@Override
	public Pair<String, String> getIconAndLabel(DiagramSelectedItem selectedItem, Diagram diagram) {
		String icon = EditorModelPlugin.getInstance().getResourceUrl("images/icon_flower.gif");
		String label = FilenameUtils.getBaseName(selectedItem.getDiagramEditableResourcePath());
		return new Pair<String, String>(icon, label);
	}

}
