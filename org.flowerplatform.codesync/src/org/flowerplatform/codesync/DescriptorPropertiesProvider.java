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
import java.util.HashMap;
import java.util.List;

import org.flowerplatform.codesync.properties.remote.StringSelectedItem;
import org.flowerplatform.codesync.remote.CodeSyncElementDescriptor;
import org.flowerplatform.codesync.remote.RelationDescriptor;
import org.flowerplatform.common.util.Pair;
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
public class DescriptorPropertiesProvider implements IPropertiesProvider<StringSelectedItem, Object> {

	private final static Logger logger = LoggerFactory.getLogger(DescriptorPropertiesProvider.class);

	private PropertyDescriptor[] pds = null;
	private HashMap<String, PropertyDescriptor> propertiesMap =  null;
	
	/**
	 * The property grid should be readOnly
	 */
	@Override
	public boolean setProperty(ServiceInvocationContext context, StringSelectedItem selectedItem, String propertyName,
			Object propertyValue) {
		throw new UnsupportedOperationException();
		//return false;
	}

	@Override
	public List<String> getPropertyNames(StringSelectedItem selectedItem, Object descriptor) {
		// use javaBeans to get its the properties (and make our life a lot easier)		
		propertiesMap = new HashMap<String, PropertyDescriptor>();
		List<String> propertyNames = new ArrayList<String>();
		try {
			BeanInfo info = null;
			if (selectedItem.getIsRelation()) {
				info = Introspector.getBeanInfo(RelationDescriptor.class);
			} else {
				info = Introspector.getBeanInfo(CodeSyncElementDescriptor.class);
			}
			pds = info.getPropertyDescriptors();
			
			for (PropertyDescriptor pd: pds) {
				// discard class (as we don't need to show this)
				if (pd.getPropertyType().equals(Class.class)) {
					continue;
				}
				propertiesMap.put(pd.getDisplayName(), pd);
			}
		} catch (IntrospectionException ex) {
			logger.error("Exception getting properties on CodeSyncElementDesriptor.class", ex);
		}
		propertyNames.addAll(propertiesMap.keySet());
		return propertyNames;
	}

	@Override
	public Property getProperty(StringSelectedItem selectedItem, Object descriptor, String propertyName) {
		PropertyDescriptor pd = propertiesMap.get(propertyName);
		try {
			Object result = pd.getReadMethod().invoke(descriptor, (Object[]) null);
			if (result != null && pd.getPropertyType() != null && pd.getPropertyType().equals(List.class)) {
				StringBuffer listVal = new StringBuffer();
				// just get the list and put it in a string with all the elements
				@SuppressWarnings("unchecked")
				List<Object> results = (List<Object>) result;
				for (Object id: results) {
					if (listVal.length() > 0) {
						listVal.append(", ");
					}
					listVal.append("\"");
					listVal.append(id.toString());
					listVal.append("\"");
				}
					result = "[" + listVal.toString() + "]";
			}
			return  new Property()
					.setName(pd.getDisplayName())
					.setValue(result);
		} catch (InvocationTargetException | IllegalAccessException ex) {
			logger.error("Exception getting property value for CodeSyncElementDescriptor.class", ex);
		}
		return null;
	}

	@Override
	public Object resolveSelectedItem(StringSelectedItem selectedItem) {
		Object descriptor = null;
		if (selectedItem.getIsRelation()) {
			descriptor = CodeSyncPlugin.getInstance().getRelationDescriptor(selectedItem.getDescriptorName());
		} else {
			descriptor = CodeSyncPlugin.getInstance().getCodeSyncElementDescriptor(selectedItem.getDescriptorName());
		}
		return descriptor;
	}

	@Override
	public Pair<String, String> getIconAndLabel(StringSelectedItem selectedItem, Object descriptor) {
		String label = "";
		String icon = "";
		if (selectedItem.getIsRelation()) {
			label = ((RelationDescriptor) descriptor).getLabel();
			icon = ((RelationDescriptor) descriptor).getIconUrl();
		} else {
			label = ((CodeSyncElementDescriptor) descriptor).getLabel();
			icon = ((CodeSyncElementDescriptor) descriptor).getIconUrl();
		}
		return new Pair<String, String>(CodeSyncPlugin.getInstance().getResourceUrl(icon), label);
	}

}
