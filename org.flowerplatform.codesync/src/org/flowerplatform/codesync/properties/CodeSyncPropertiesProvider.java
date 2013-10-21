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
package org.flowerplatform.codesync.properties;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.properties.Property;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.SelectedItem;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * @author Tache Razvan Mihai
 */
public class CodeSyncPropertiesProvider implements IPropertiesProvider {

	/**
	 * @author Tache Razvan Mihai
	 * @author Mariana Gheorghe
	 */
	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		List<Property> properties = new ArrayList<Property>();	

		DiagramEditorStatefulService diagramEditorStatefulService = ((DiagramSelectedItem) selectedItem).getEditorStatefulService();		
		String diagramEditableResourcePath = ((DiagramSelectedItem) selectedItem).getDiagramEditableResourcePath();
		String id = ((DiagramSelectedItem) selectedItem).getXmiID();
		
		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) diagramEditorStatefulService.getEditableResource(diagramEditableResourcePath);
		View view = (View) diagramEditableResource.getEObjectById(id);
		CodeSyncElement codeSyncElement = (CodeSyncElement) view.getDiagrammableElement();
		List<String> features = CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getFeatures(codeSyncElement.getType());
		
		for (String feature : features) {
			properties.add(new Property(feature, 
					CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getFeatureValue(codeSyncElement, feature)));
		}
		
		return properties;
	}

	@Override
	public void setProperty(SelectedItem selectedItem, Property property) {
		
	}

}
