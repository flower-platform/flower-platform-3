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
package org.flowerplatform.codesync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Tache Razvan Mihai
 * @author Mariana Gheorghe
 */
public class CodeSyncPropertiesProvider implements IPropertiesProvider {

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		List<Property> properties = new ArrayList<Property>();	

		CodeSyncElement codeSyncElement = getCodeSyncElement(selectedItem);
		if (codeSyncElement == null) {
			return Collections.emptyList();
		}
		List<String> features = CodeSyncOperationsService.getInstance().getFeatures(codeSyncElement.getType());
		
		for (String feature : features) {
			properties.add(new Property(feature, 
					CodeSyncOperationsService.getInstance().getFeatureValue(codeSyncElement, feature), false));
		}
		
		return properties;
	}

	@Override
	public void setProperty(SelectedItem selectedItem, String propertyName, Object propertyValue) {
		CodeSyncElement codeSyncElement = getCodeSyncElement(selectedItem);
		if (codeSyncElement == null) {
			return;
		}
		CodeSyncOperationsService.getInstance().setFeatureValue(codeSyncElement, propertyName, propertyValue);
	}
	
	protected CodeSyncElement getCodeSyncElement(SelectedItem selectedItem) {
		DiagramEditorStatefulService diagramEditorStatefulService = ((DiagramSelectedItem) selectedItem).getEditorStatefulService();		
		String diagramEditableResourcePath = ((DiagramSelectedItem) selectedItem).getDiagramEditableResourcePath();
		String id = ((DiagramSelectedItem) selectedItem).getXmiID();
		
		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) diagramEditorStatefulService.getEditableResource(diagramEditableResourcePath);
		View view = (View) diagramEditableResource.getEObjectById(id);
		return (CodeSyncElement) view.getDiagrammableElement();
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
