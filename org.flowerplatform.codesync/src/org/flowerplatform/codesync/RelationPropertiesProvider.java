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

import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.editor.model.properties.AbstractModelPropertiesProvider;

import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Mariana Gheorghe
 */
public class RelationPropertiesProvider extends AbstractModelPropertiesProvider<Relation> {

	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		List<Property> properties = new ArrayList<Property>();
		
		Relation relation = resolveSelectedItem((DiagramSelectedItem) selectedItem);
		if (relation == null) {
			return Collections.emptyList();
		}
		
		RelationDescriptor descriptor = CodeSyncPlugin.getInstance().getRelationDescriptor(relation.getType());
		properties.add(new Property("Type", descriptor.getType(), true));
		properties.add(new Property("Label", descriptor.getLabel(), false));
		
		return properties;
	}

	@Override
	public Relation resolveSelectedItem(DiagramSelectedItem selectedItem) {
		return (Relation) getViewById(selectedItem).getDiagrammableElement();
	}

	@Override
	protected void doSetProperty(DiagramSelectedItem selectedItem,
			Relation resolvedSelectedItem, String propertyName,
			Object propertyValue) {
		// TODO Auto-generated method stub
		
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
