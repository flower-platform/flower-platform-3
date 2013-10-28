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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.properties.remote.StringSelectedItem;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.communication.service.ServiceInvocationContext;
import org.flowerplatform.properties.providers.IPropertiesProvider;
import org.flowerplatform.properties.remote.Property;
import org.flowerplatform.properties.remote.SelectedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.crispico.flower.mp.codesync.base.CodeSyncPlugin;

/**
 * Properties provider for CodeSyncElementDescriptor and RelationDescriptor
 * 
 * @author Mircea Negreanu
 */
public class DescriptorPropertiesProvider implements IPropertiesProvider {

	private final static Logger logger = LoggerFactory.getLogger(DescriptorPropertiesProvider.class);
	
	@Override
	public List<String> getPropertyNames() {
		return null;
	}

	@Override
	public Property getProperty(SelectedItem selectedItem, String propertyName) {
		return null;
	}

	/**
	 * Get the list of the properties for the selectedItem.
	 */
	@Override
	public List<Property> getProperties(SelectedItem selectedItem) {
		List<Property> properties = new ArrayList<>();
		
		if (selectedItem instanceof StringSelectedItem) {
			StringSelectedItem item = (StringSelectedItem)selectedItem;
			if (!item.getIsRelation()) {
				// get the codeSyncDescriptor which corresponds to the name received from client
				CodeSyncElementDescriptor descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(item.getDescriptorName());
				// if the descriptor has been found
				if (descriptor != null) {
					// use javaBeans to get its the properties (and make our life a lot easier)
					PropertyDescriptor[] pds = null;
					try {
						BeanInfo info = Introspector.getBeanInfo(CodeSyncElementDescriptor.class);
						pds = info.getPropertyDescriptors();
					} catch (IntrospectionException ex) {
						logger.error("Exception getting properties on CodeSyncElementDesriptor.class", ex);
					}
					
					// iterate on each property and get its value
					if (pds != null) {
						for (PropertyDescriptor pd: pds) {
							// discard class (as we don't need to show this)
							if (pd.getPropertyType().equals(Class.class)) {
								continue;
							}
							// try/catch so we can advance even if we have a problem
							try {
								Object result = pd.getReadMethod().invoke(descriptor, (Object[]) null);
								if (result != null && pd.getPropertyType() != null && pd.getPropertyType().equals(List.class)) {
									StringBuffer listVal = new StringBuffer();
									// just get the list and put it in a string with all the elements
									for (Object id: (List<Object>) result) {
										if (listVal.length() > 0) {
											listVal.append(", ");
										}
										listVal.append("\"");
										listVal.append(id.toString());
										listVal.append("\"");
									}
									result = listVal.toString();
								}
								properties.add(new Property(pd.getDisplayName(), result));
							} catch (InvocationTargetException | IllegalAccessException ex) {
								logger.error("Exception getting property value for CodeSyncElementDescriptor.class", ex);
							}
						}
					}
				}
			}
		}
		
		return properties;
	}

	/**
	 * The property should be readOnly
	 */
	@Override
	public boolean setProperty(ServiceInvocationContext context,
			SelectedItem selectedItem, String propertyName, Object propertyValue) {
		throw new UnsupportedOperationException();
	}

}
