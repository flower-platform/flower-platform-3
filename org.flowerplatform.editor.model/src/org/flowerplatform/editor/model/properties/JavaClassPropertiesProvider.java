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
package org.flowerplatform.editor.model.properties;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.editor.model.remote.DiagramEditableResource;
import org.flowerplatform.editor.model.remote.DiagramEditorStatefulService;
import org.flowerplatform.emf_model.notation.View;
import org.flowerplatform.properties.Property;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.SelectedItem;

import com.crispico.flower.mp.model.astcache.code.Attribute;
import com.crispico.flower.mp.model.codesync.CodeSyncElement;
/**
 * @author Tache Razvan Mihai
 * @return
 */
public class JavaClassPropertiesProvider implements IPropertiesProvider {

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		
		List<Property> properties = new ArrayList<Property>();	

		DiagramEditorStatefulService diagramEditorStatefulService = ((DiagramSelectedItem) selectedItem).getEditorStatefulService();
		String diagramEditableResourcePath = ((DiagramSelectedItem) selectedItem).getDiagramEditableResourcePath();
		String id = ((DiagramSelectedItem) selectedItem).getXmiID();
		
		DiagramEditableResource diagramEditableResource = (DiagramEditableResource) diagramEditorStatefulService.getEditableResource(diagramEditableResourcePath);
		View view = (View) diagramEditableResource.getEObjectById(id);
		CodeSyncElement myCodeSyncObject = (CodeSyncElement) view.getDiagrammableElement();
		// TODO decide what properties are needed
		properties.add(new Property("Name", myCodeSyncObject.getName()));
		properties.add(new Property("Type", myCodeSyncObject.getType()));
		
		com.crispico.flower.mp.model.astcache.code.Class javaClass = (com.crispico.flower.mp.model.astcache.code.Class) myCodeSyncObject.getAstCacheElement();

		properties.add(new Property("Documentation", javaClass.getDocumentation()));
		// TODO decide what modifiers should be shown/edited: javaClass.getModifiers();
		return properties;
	}

}
