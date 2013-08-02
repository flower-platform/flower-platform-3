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


import java.security.Permission;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.web.WebPlugin;


/**
 * Descriptor for {@link FlowerWebFilePermission}. See the doc of this class
 * for additional info.
 * 
 * @author Cristi
 * @author Florin
 * 
 * 
 */
public class FlowerWebFilePermissionDescriptor extends PermissionDescriptor {

	/**
	 * 
	 */
	@Override
	public Class<? extends Permission> getHandledPermissionType() {
		return java.io.FilePermission.class;
	}

	/**
	 * 
	 */
	@Override
	public Class<? extends Permission> getImplementedPermissionType() {
		return org.flowerplatform.web.security.permission.FlowerWebFilePermission.class;
	}

	@Override
	public String getSimpleName() {
		return WebPlugin.getInstance().getMessage("entity.permission.simpleName.flowerWebFilePermission");
	}
	
	@Override
	public int getOrder() {
		return 10;
	}

	@Override
	public Map<String, String> getHints() {
		Map<String, String> map = super.getHints();
		map.put(TYPE_FIELD, WebPlugin.getInstance().getMessage("entity.permission.filePermission.type.hint"));
		map.put(NAME_FIELD, WebPlugin.getInstance().getMessage("entity.permission.filePermission.resource.hint"));
		map.put(ACTIONS_FIELD, WebPlugin.getInstance().getMessage("entity.permission.filePermission.actions.hint", getActions()));
		return map;
	}

	/**
	 * 
	 */
	@Override
	public boolean isTreePermission() {
		return true;
	}

	/**
	 * 
	 */
	@Override
	public List<String> getActions() {
		return Arrays.asList(FlowerWebFilePermission.READ, FlowerWebFilePermission.READ_WRITE, FlowerWebFilePermission.READ_WRITE_DELETE, FlowerWebFilePermission.NONE);
	}
	
	/**
	 * 
	 */
	@Override
	public Map<String, String> validate(Permission permission) {
		if (!(permission instanceof FlowerWebFilePermission)) {
			throw new RuntimeException("Can handle only FlowerWebFilePermission");
		}
		Map<String, String> validationResults = new HashMap<String, String>();
		
		if (!(pathIsRelativeToWorkspace(permission.getName()))) {
			validationResults.put(NAME_FIELD, WebPlugin.getInstance().getMessage("entity.permission.validation.invalidPath"));
		}
		if (!getActions().contains(permission.getActions())) {
			validationResults.put(ACTIONS_FIELD, WebPlugin.getInstance().getMessage("entity.permission.validation.invalidAction", getActions()));
		}
		
		return validationResults;
	}

}