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
import java.util.List;

import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;
import org.flowerplatform.web.security.sandbox.SecurityUtils;

import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;

/**
 * The owner of this permission can create/update/delete ISecurityEntities:
 * {@link Organization}, {@link Group}, {@link User}).
 * The {@link #actions} can have the value * meaning that the modified security entity can be assigned to anyone.
 * Actions can be a csv list of organizations and/or groups to which the modified security entity can belong.
 * 
 * <p>
 * E.g. if actions = #org1, @group, the owner can create user or groups that belong to #org1, or users that belong to @group.
 * 
 * @author Florin
 * 
 * 
 */
public class AdminSecurityEntitiesPermission extends Permission {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private String actions;
	
	/**
	 * 
	 */
	private List<ISecurityEntity> assignableSecurityEntities;
	
	/**
	 * @param name - not used
	 * @param actions
	 * 
	 */
	public AdminSecurityEntitiesPermission(String name, String actions) {
		super(name);
		this.actions = actions;
	}

	/**
	 * 
	 */
	@Override
	public String getActions() {
		return actions;
	}

	/**
	 * 
	 */
	@Override
	public boolean implies(Permission permission) {
		if (!(permission instanceof AdminSecurityEntitiesPermission)) {
			return false;
		}
		if (actions.equals(PermissionEntity.ANY_ENTITY)) {
			// permission to create any SecurityEntity that is assigned to any security entity 
			return true;
		}
		if (assignableSecurityEntities == null) {			
			assignableSecurityEntities = SecurityEntityAdaptor.csvStringToSecurityEntityList(actions, true);
		}
		
		AdminSecurityEntitiesPermission other = (AdminSecurityEntitiesPermission) permission;
		if (other.actions.equals(PermissionEntity.ANY_ENTITY)) {
			return false;
		}
		List<ISecurityEntity> securityEntitiesToBeChecked = SecurityEntityAdaptor.csvStringToSecurityEntityList(other.actions, false);
		boolean implies = false;
		for (ISecurityEntity securityEntity: securityEntitiesToBeChecked) {
			implies |= SecurityUtils.securityEntityIsAssignable(assignableSecurityEntities, securityEntity); 
		}
		
		return implies;
	}

	/**
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AdminSecurityEntitiesPermission)) {
			return false;
		}
		
		AdminSecurityEntitiesPermission other = (AdminSecurityEntitiesPermission) obj;
		return getName().equals(other.getName()) && getActions().equals(other.getActions());
	}

	/**
	 * 
	 */
	@Override
	public int hashCode() {
		return getName().hashCode() + 31 * getActions().hashCode();
	}
}