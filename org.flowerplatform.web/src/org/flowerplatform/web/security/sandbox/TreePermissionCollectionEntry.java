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

import org.flowerplatform.web.security.permission.AbstractTreePermission;

import org.flowerplatform.web.entity.ISecurityEntity;

/**
 * List entry for {@link TreePermissionCollection}. Associates
 * an {@link AbstractTreePermission} with an {@link IFlowerWebPrincipal}.
 * 
 * @author Cristi
 * @author Florin
 * 
 * @flowerModelElementId _bPwo4GR9EeGyd4yTk74SKw
 */
public class TreePermissionCollectionEntry {	
	
	/**
	 * @flowerModelElementId _F26agG0ZEeGBsfNm1ipRfw
	 */
	public ISecurityEntity securityEntity;
	
	/**
	 * @flowerModelElementId _dM5rAGR9EeGyd4yTk74SKw
	 */
	private AbstractTreePermission permission;	
	
	/**
	 * @flowerModelElementId _TXHKEGbMEeGOeOE1u9CeQw
	 */
	public TreePermissionCollectionEntry(ISecurityEntity securityEntity, AbstractTreePermission permission) {
		this.securityEntity = securityEntity;
		this.permission = permission;
	}

	/**
	 * @flowerModelElementId _f1gWcmnXEeGiEKNiPvCvPw
	 */
	public AbstractTreePermission getPermission() {
		return permission;
	}
	
	/**
	 * @flowerModelElementId _puT8MHJqEeG32IfhnS7SDQ
	 */
	public ISecurityEntity getSecurityEntity() {
		return securityEntity;
	}
	
	/**
	 * @flowerModelElementId _f1gWdGnXEeGiEKNiPvCvPw
	 */
	@Override
	public String toString() {
		return String.format("[entity=%s permission=%s]", securityEntity, permission);
	}
}