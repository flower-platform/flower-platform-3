package org.flowerplatform.codesync;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.communication.CommunicationPlugin;
import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.SelectedItem;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;
/**
 * @author Razvan Tache
 */
public class CodeSyncPropertiesProvider implements IPropertiesProvider {

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		
		List<Property> properties = new ArrayList<Property>();	

		String diagramEditorStatefulServiceId = ((DiagramSelectedItem) selectedItem).getEditorStatefulServiceId();
		DiagramEditorStatefulService diagramEditorStatefulService = getDiagramEditorStatefulService(diagramEditorStatefulServiceId);	
		String diagramEditableResourcePath = ((DiagramSelectedItem) selectedItem).getDiagramEditableResourcePath();
		String id = ((DiagramSelectedItem) selectedItem).getXmiID();
		
		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) diagramEditorStatefulService.getEditableResource(diagramEditableResourcePath);
		View view = (View) diagramEditableResource.getEObjectById(id);
		CodeSyncElement myCodeSyncObject = (CodeSyncElement) view.getDiagrammableElement();
		
		// properties = getFeatures(myCodeSyncObject.getType);
		return null;
	}

	@Override
	public void setProperty(SelectedItem selectedItem, String propertyName, Object propertyValue) {
		
	}

	private DiagramEditorStatefulService getDiagramEditorStatefulService(String diagramEditorStatefulServiceId) {
		return (DiagramEditorStatefulService)CommunicationPlugin.getInstance().getServiceRegistry().getService(diagramEditorStatefulServiceId);
	}

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
}
