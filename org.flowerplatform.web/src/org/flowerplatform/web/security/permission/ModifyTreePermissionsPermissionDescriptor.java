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
import java.util.HashMap;
import java.util.Map;

import org.flowerplatform.web.security.sandbox.SecurityUtils;

import org.flowerplatform.web.WebPlugin;
import org.flowerplatform.web.entity.PermissionEntity;


/**
 * Descriptor for {@link ModifyTreePermissionsPermission}. See the doc of this
 * class for additional info.
 * 
 * @author Florin
 * @flowerModelElementId _Z94U0HgGEeGtTo1wOb4S9A
 */
public class ModifyTreePermissionsPermissionDescriptor extends PermissionDescriptor {

	/**
	 * @flowerModelElementId _Z94U0ngGEeGtTo1wOb4S9A
	 */
	@Override
	public Class<? extends Permission> getHandledPermissionType() {
		return ModifyTreePermissionsPermission.class;
	}

	/**
	 * @flowerModelElementId _Z95i8ngGEeGtTo1wOb4S9A
	 */
	@Override
	public Class<? extends Permission> getImplementedPermissionType() {
		return ModifyTreePermissionsPermission.class;
	}

	@Override
	public String getSimpleName() {
		return WebPlugin.getInstance().getMessage("entity.permission.simpleName.modifyTreePermissionsPermission");
	}

	@Override
	public int getOrder() {
		return 30;
	}
	
	@Override
	public Map<String, String> getHints() {
		Map<String, String> map = super.getHints();
		map.put(TYPE_FIELD, WebPlugin.getInstance().getMessage("entity.permission.modifyPermissionsPermission.type.hint"));
		map.put(NAME_FIELD, WebPlugin.getInstance().getMessage("entity.permission.modifyPermissionsPermission.resource.hint"));
		map.put(ACTIONS_FIELD, WebPlugin.getInstance().getMessage("entity.permission.modifyPermissionsPermission.actions.hint", getActions()));
		return map;
	}
	
	/**
	 * @flowerModelElementId _Z97YIHgGEeGtTo1wOb4S9A
	 */
	@Override
	public boolean isTreePermission() {
		return true;
	}

	/**
	 * @flowerModelElementId _Sc76AIIZEeGPwv1h63g-uQ
	 */
	@Override
	public Map<String, String> validate(Permission permission) {
		if (!(permission instanceof ModifyTreePermissionsPermission)) {
			throw new RuntimeException("Can handle only ModifyTreePermissionsPermission");
		}
		Map<String, String> validationResults = new HashMap<String, String>();
		
		if (!(pathIsRelativeToWorkspace(permission.getName()))) {
			validationResults.put(NAME_FIELD, WebPlugin.getInstance().getMessage("entity.permission.validation.invalidPath"));
		}		
		// actions can be a comma separated list of security entities (example: #org1, @group2, $user3)
		if (!permission.getActions().equals(PermissionEntity.ANY_ENTITY)) {	
			String[] entityNames = permission.getActions().split(",");			
			for (String assignedTo : entityNames) {
				assignedTo = assignedTo.trim();
				String message = SecurityUtils.validateSecurityEntity(assignedTo);
				if (message != null) {
					validationResults.put(ACTIONS_FIELD, message);
				}
			}
		}
		
		return validationResults;
	}

}