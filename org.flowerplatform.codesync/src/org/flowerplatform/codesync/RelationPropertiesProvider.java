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
import java.util.List;

import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.common.util.Pair;
import org.flowerplatform.editor.model.properties.AbstractModelPropertiesProvider;
import org.flowerplatform.editor.model.properties.remote.DiagramSelectedItem;
import org.flowerplatform.properties.remote.Property;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;
import com.crispico.flower.mp.model.codesync.Relation;

/**
 * @author Mariana Gheorghe
 */
public class RelationPropertiesProvider extends AbstractModelPropertiesProvider<DiagramSelectedItem, Relation> {

	@Override
	public List<String> getPropertyNames(DiagramSelectedItem selectedItem,	Relation resolvedSelectedItem) {
		List<String> propertiesNames = new ArrayList<String>();
		
		propertiesNames.add("Type");
		propertiesNames.add("Label");

		return propertiesNames;
	}

	@Override
	public Property getProperty(DiagramSelectedItem selectedItem, Relation resolvedSelectedItem, String propertyName) {
		Relation relation = resolveSelectedItem((DiagramSelectedItem) selectedItem);
		if (relation == null) {
			return null;
		}
		
		RelationDescriptor descriptor = CodeSyncPlugin.getInstance().getRelationDescriptor(relation.getType());
		switch (propertyName) {
			case "Type": {
				return new Property()
						.setNameAs("Type")
						.setValueAs(descriptor.getType());						
			}
			case "Label": {
				return new Property()
						.setNameAs("Label")
						.setValueAs(descriptor.getLabel());						
						
			}
		}
		return null;
	}

	@Override
	public Relation resolveSelectedItem(DiagramSelectedItem selectedItem) {
		return (Relation) getViewById(selectedItem).getDiagrammableElement();		
	}

	@Override
	public Pair<String, String> getIconAndLabel(DiagramSelectedItem selectedItem, Relation resolvedSelectedItem) {
		Relation relation = resolveSelectedItem((DiagramSelectedItem) selectedItem);
		if (relation == null) {
			return null;
		}
		
		RelationDescriptor descriptor = CodeSyncPlugin.getInstance().getRelationDescriptor(relation.getType());
		
		return new Pair<String, String>(descriptor.getIconUrl(), descriptor.getLabel());
	}

	@Override
	protected void doSetProperty(DiagramSelectedItem selectedItem, Relation resolvedSelectedItem, String propertyName,
			Object propertyValue) {
		// TODO Auto-generated method stub		
	}

}
