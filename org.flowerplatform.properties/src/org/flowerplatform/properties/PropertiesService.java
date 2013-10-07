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
package org.flowerplatform.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.SelectedItem;
/**
 * @author Tache Razvan Mihai
 * @return
 */
public class PropertiesService {
	
	public List<Property> getProperties(List<SelectedItem> selection) {
		HashMap<String, IPropertiesProvider> propertiesProvidersMapped = PropertiesPlugin.getInstance().getPropertiesProviders();
		List<Property> properties = new ArrayList<Property>();
		for(SelectedItem selectedItem : selection) {
			List<Property> newProperties = new ArrayList<Property>();
			// get the right provider
			IPropertiesProvider itemProvider = propertiesProvidersMapped.get(selectedItem.getItemType());
			// retrieve properties by providers
			newProperties = itemProvider.getProperties(selectedItem);
			// merge with the previous results
			if(properties.isEmpty()) {
				properties.addAll(newProperties);
			} else {
				for(Property property:properties) {
					// select common property, server logic to be discused
				}
				properties.addAll(newProperties);
			}
		}	
		return properties;
	}
}
