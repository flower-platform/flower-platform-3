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
package org.flowerplatform.web.security.sandbox;

import java.security.Permission;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.web.security.permission.PermissionDescriptor;


/**
 * The descriptors of the permissions that {@link FlowerWebPolicy} enforces. 
 * 
 * @author Florin
 * 
 * 
 */
class PermissionDescriptorsTable {

	/**
	 * The key of this map is handled permission type.
	 * 
	 */
	private Map<Class<? extends Permission>, PermissionDescriptor> handledPermissionDescriptors = new LinkedHashMap<Class<? extends Permission>, PermissionDescriptor>();
		
	/**
	 * The key of this map is implemented permission type.
	 * 
	 */
	private Map<Class<? extends Permission>, PermissionDescriptor> implementedPermissionDescriptors = new LinkedHashMap<Class<? extends Permission>, PermissionDescriptor>();
	
	/**
	 * 
	 */
	public void put(PermissionDescriptor descriptor) {
		handledPermissionDescriptors.put(descriptor.getHandledPermissionType(), descriptor);
		implementedPermissionDescriptors.put(descriptor.getImplementedPermissionType(), descriptor);
	}
	
	/**
	 * 
	 */
	public PermissionDescriptor getByHandledPermissionType(Class<? extends Permission> type) {
		return handledPermissionDescriptors.get(type);
	}
	
	/**
	 * 
	 */
	public PermissionDescriptor getByImplementedPermissionType(Class<? extends Permission> type) {
		return implementedPermissionDescriptors.get(type);
	}
	
	/**
	 * 
	 */
	public List<PermissionDescriptor> getPermissionDescriptors() {
		return new ArrayList<PermissionDescriptor>(implementedPermissionDescriptors.values());
	}
}