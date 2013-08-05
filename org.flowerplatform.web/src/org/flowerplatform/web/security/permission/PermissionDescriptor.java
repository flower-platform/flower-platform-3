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
package org.flowerplatform.web.security.permission;

import java.io.File;
import java.security.AccessController;
import java.security.Permission;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.web.security.sandbox.FlowerWebPolicy;

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.PermissionEntity;

/**
 * It keeps metadata for a permission type.
 * 
 * <p>
 * An important responsibility of this class is to do a mapping between
 * permission representations saved in database {@link org.flowerplatform.web.entity.PermissionEntity}
 * (i.e. {@link #getImplementedPermissionType()}) and <code>java.security.Permission</code> 
 * (i.e. {@link #getHandledPermissionType()}). The 2 of them are usually identical. 
 * 
 * @author Cristi
 * @author Florin
 * @author Mariana
 * 
 * 
 */
public abstract class PermissionDescriptor {	
	
	/**
	 * {@link Permission#getName()} is equivalent of {@link PermissionEntity#getResource()}. 
	 */
	static final String NAME_FIELD = "name";
	
	/**
	 * {@link Permission#getActions()} is equivalent of {@link PermissionEntity#getActions()}. 
	 */
	static final String ACTIONS_FIELD = "actions";
	
	static public final String ASSIGNED_TO_FIELD = "assignedTo";
	
	static final String TYPE_FIELD = "type";
	
	/**
	 * This usually returns the same value as {@link #getImplementedPermissionType()}.
	 * However, there are cases when the actual "implies" logic expects another
	 * type of permission, different from its actual type. E.g. {@link FlowerWebFilePermission}
	 * which expects a "parameter" of type {@link java.io.FilePermission}.
	 * 
	 * <p>
	 * {@link FlowerWebPolicy} organizes the cache based on the result of this method.
	 * 
	 * 
	 */
	public abstract Class<? extends Permission> getHandledPermissionType();
	
	/**
	 * This class is the one that comes with the <strong>implementation</strong>
	 * of the "implements" logic. The fully qualified of this type is saved
	 * in the database {@link PermissionEntity#getType()}. 
	 * 
	 * 
	 */
	public abstract Class<? extends Permission> getImplementedPermissionType();

	/**
	 * Should return <code>true</code> for tree permissions.
	 * 
	 * 
	 */
	public abstract boolean isTreePermission();
	
	/**
	 * Validates the name and action fields of a permission. 
	 * 
	 */
	public abstract Map<String, String> validate(Permission permission);
	
	/**
	 * Return the possible values for actions, in case these values form a finit set.
	 * 
	 */
	public List<String> getActions() {
		return Collections.emptyList();
	}
	
	/**
	 * 
	 */
	public String getName() {
		return getImplementedPermissionType().getName();
	}

	public void setName(String s) {}
	
	/**
	 * 
	 */
	public String getSimpleName() {
		return getImplementedPermissionType().getSimpleName();
	}
	
	public void setSimpleName(String s) {}
	
	public void setOrder(int i) {}
	
	/**
	 * Used to order the permissions on client side.
	 * 
	 * @author Mariana
	 */
	public int getOrder() {
		return 0;
	}
	
	public void setHints(Map<String, String> map) {}
	
	/**
	 * A map with hints that will be displayed in the permission form, depending on the type of 
	 * permission selected. The <code>ASSIGNED_TO</code> hint is the same for all the permissions.
	 * 
	 * @author Mariana
	 */
	public Map<String, String> getHints() {
		Map<String, String> map = new HashMap<String, String>();
		map.put(ASSIGNED_TO_FIELD, WebPlugin.getInstance().getMessage("entity.permission.assignedTo.hint"));
		return map;
	}
	
	/**
	 * Returns true if the path exists and it is relative to workspace.
	 */
	protected boolean pathIsRelativeToWorkspace(String path) {
		String runtimeWorkspace = ((FlowerWebPolicy)Policy.getPolicy()).getRuntimeWorkspace().getAbsolutePath();
		String name = runtimeWorkspace+ "/" + path;
		if (name.endsWith("*")) {
			name = name.substring(0, name.length() - 2);
		}
		final String name2 = name;
		// File.exists will determine a security check 
		return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {

			@Override
			public Boolean run() {
				File file = new File(name2);
				return file.exists();
			}
		});
		
	}
}