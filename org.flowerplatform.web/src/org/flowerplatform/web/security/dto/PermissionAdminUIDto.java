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
package org.flowerplatform.web.security.dto;

import java.security.Policy;

import org.flowerplatform.web.security.sandbox.FlowerWebPolicy;

import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.dto.NamedDto;

/**
 * @see PermissionEntity
 * 
 * @author Cristi
 * @author Cristina
 * @flowerModelElementId _TUbqMFcNEeGL3vi-zPhopA
 */
public class PermissionAdminUIDto extends NamedDto {
	
	/**
	 * @flowerModelElementId _QRzh8F34EeGwLIVyv_iqEg
	 */
	private String type;
	
	/**
	 * @flowerModelElementId _TUcRQ1cNEeGL3vi-zPhopA
	 */
	private String actions;
	
	/**
	 * @flowerModelElementId _TUcRRVcNEeGL3vi-zPhopA
	 */
	private String assignedTo;
	
	private boolean isEditable;

	public String getActions() {
		return actions;
	}

	public void setActions(String actions) {
		this.actions = actions;
	}

	public String getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * If <code>false</code>, then this permission will be disabled for edit/delete
	 * on client side.
	 * 
	 * @author Mariana
	 */
	public boolean getIsEditable() {
		return isEditable;
	}

	public void setIsEditable(boolean editable) {
		this.isEditable = editable;
	}

	public PermissionAdminUIDto() {
		super();		
	}

	/**
	 * Examples: 
	 * 	@user1: AdminSecurityEntitiesPermission(null, #org1)
	 *  @user1: FlowerWebFilePermission(root/dir1, read-write-delete)
	 * 
	 * @author Mariana
	 */
	@Override
	public String toString() {
		return String.format("%s: %s(%s, %s)", 
				assignedTo, 
				((FlowerWebPolicy) Policy.getPolicy()).getPermissionDescriptor(type).getSimpleName(), 
				getName() != null && getName().length() > 0 ? getName() : "null", 
				actions);
	}
	
}