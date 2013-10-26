package org.flowerplatform.editor.model.properties;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.communication.service.ServiceInvocationContext;
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
public class DiagramPropertiesProvider extends AbstractModelPropertiesProvider<Diagram> {

	public static final String LOCATION_FOR_NEW_ELEMENTS_PROPERTY = "locationForNewElements";
	public static final String SHOW_LOCATION_FOR_NEW_ELEMENTS_DILOG_PROPERTY = "showLocationForNewElementsDialog";
	
	@Override
	public List<String> getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Property getProperty(SelectedItem selectedItem, String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		List<Property> properties = new ArrayList<Property>();	
		Diagram diagram = resolveSelectedItem((DiagramSelectedItem) selectedItem);

		properties.add(new Property(LOCATION_FOR_NEW_ELEMENTS_PROPERTY, 
				diagram.getLocationForNewElements(), "StringWithDialog", true));
		properties.add(new Property(SHOW_LOCATION_FOR_NEW_ELEMENTS_DILOG_PROPERTY, 
				diagram.isShowLocationForNewElementsDialog(), "Boolean", false));
		
		return properties;
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
		} else if (propertyName.equals(SHOW_LOCATION_FOR_NEW_ELEMENTS_DILOG_PROPERTY)) {
			resolvedSelectedItem.setShowLocationForNewElementsDialog((Boolean) propertyValue);
		}
		
	}
	
	
}
