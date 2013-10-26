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

import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author Razvan Tache
 */
public class PropertiesService {
	
	private final static Logger logger = LoggerFactory.getLogger(PropertiesService.class);

	public List<Property> getProperties(List<SelectedItem> selection) {
		HashMap<String, IPropertiesProvider> propertiesProvidersMapped = PropertiesPlugin.getInstance().getPropertiesProviders();
		List<Property> properties = new ArrayList<Property>();
		
		for (SelectedItem selectedItem : selection) {
			if (logger.isDebugEnabled()) {
				logger.debug("Getting the property list for the selection: {}", selectedItem);
			}
			List<Property> newProperties = new ArrayList<Property>();
			// get the right provider
			IPropertiesProvider itemProvider = propertiesProvidersMapped.get(selectedItem.getItemType());
			// retrieve properties by providers
			if (itemProvider != null) {
				newProperties = itemProvider.getProperties(selectedItem);
			}
			// merge with the previous results
			if (properties.isEmpty()) {
				properties.addAll(newProperties);
			} else {
				// select common property, server logic to be discused
				throw new UnsupportedOperationException();
			}
		}	
		
		return properties;
	}
	
	/**
	 * @return Returns <code>true</code> if the operation was successful (i.e. changes applied for all selected items). 
	 * 		<code>false</code> otherwise (e.g. for at least one selected item, if the underlying lock mechanism fails). 
	 * 		In this case, the client will request the original value, to update the UI.
	 * 
	 * @author Razvan Tache
	 * @author Cristian Spiescu
	 */
	public boolean setProperties(ServiceInvocationContext context, List<SelectedItem> selection, String propertyName, Object propertyValue) {
		if (logger.isDebugEnabled()) {
			logger.debug("Setting property {} = {}, for selection = {}", new Object[] { propertyName, propertyValue, selection });
		}
		boolean result = true;
		HashMap<String, IPropertiesProvider> propertiesProvidersMapped = PropertiesPlugin.getInstance().getPropertiesProviders();
		for (SelectedItem selectedItem : selection) {
			// get the right provider
			IPropertiesProvider itemProvider = propertiesProvidersMapped.get(selectedItem.getItemType());
			// set the property
			if (!itemProvider.setProperty(context, selectedItem, propertyName, propertyValue)) {
				if (logger.isDebugEnabled()) {
					logger.debug("Failed setting property {} = {}, for element {} / {}", new Object[] { propertyName, propertyValue, selectedItem, /*resovedSelectedItem*/ }); //TODO CS for Razvan: uncomment this
				}
				result = false;
			}
		}	
		return result;
	}
}
