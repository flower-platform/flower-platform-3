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
import java.security.Policy;

import org.flowerplatform.web.security.sandbox.FlowerWebPolicy;
import org.flowerplatform.web.security.sandbox.IFlowerWebPolicyExtension;
import org.flowerplatform.web.security.sandbox.TreePermissionCollection;


/**
 * This is a special type of permission (that is a tree
 * permission, so it extends {@link AbstractTreePermission}): its
 * "implies" logic reacts only to permissions of type
 * {@link java.io.FilePermission}. E.g. it is used within calls
 * that originate from something like 
 * <code>SecurityManager.checkPermission(java.io.FilePermission)</code>.
 * This means that all existing code that checks for "normal" permissions
 * will end up interrogating "our kind" of file permissions.
 * 
 * <p>
 * One reason for which we have our own {@link java.io.FilePermission} is that
 * we want to be able to represent deny permissions. The original
 * permission doesn't allow "none" permissions (like this one).
 * 
 * <p>
 * <strong>NOTE:</strong> This permission should never be used directly (e.g.
 * <code>SecurityManager.checkPermission({@link FlowerWebFilePermission})</code>). The correct
 * form is: <code>SecurityManager.checkPermission(java.io.FilePermission)</code>.
 * 
 * @author Florin
 * @author Cristi
 * @flowerModelElementId _TW1dQGbMEeGOeOE1u9CeQw
 */
public class FlowerWebFilePermission extends AbstractTreePermission {
	
	/**
	 * @flowerModelElementId _-TdF0Gx4EeGBsfNm1ipRfw
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @flowerModelElementId _TW1dQ2bMEeGOeOE1u9CeQw
	 */
	public static final String NONE = "none";
	
	/**
	 * @flowerModelElementId _TW2EUGbMEeGOeOE1u9CeQw
	 */
	public static final String READ = "read";
	
	/**
	 * @flowerModelElementId _TW2EUmbMEeGOeOE1u9CeQw
	 */
	public static final String READ_WRITE = "read-write";
	
	/**
	 * @flowerModelElementId _TW2rYGbMEeGOeOE1u9CeQw
	 */
	public static final String READ_WRITE_DELETE = "read-write-delete";
	
	/**
	 * Allowed actions are:
	 * <ul>
	 * <li>none</li>
	 * <li>read</li>
	 * <li>read-write</li>
	 * <li>read-write-delete</li>
	 * </ul>
	 * 
	 * Please see the doc from super for path conventions.
	 * 
	 * @flowerModelElementId _TW2rYmbMEeGOeOE1u9CeQw
	 */
	public FlowerWebFilePermission(String path, String actions) {
		super(path, actions);
	}

	/**
	 * Return <code>true</code> if a compatible <strong>{@link java.io.FilePermission}</strong>
	 * is passed. According to the super doc, the tree path compatibility is not checked here
	 * because it is verified by {@link TreePermissionCollection}. 
	 * 
	 * @flowerModelElementId _TW3Sc2bMEeGOeOE1u9CeQw
	 */
	@Override
	public boolean impliesWithoutTreePathCheck(Permission permission) {
		if (!(permission instanceof java.io.FilePermission)) {
			return false;
		}
		
		java.io.FilePermission javaFilePermission = (java.io.FilePermission) permission;		
		boolean implies = true;
		
		if (getActions().equals(NONE)) {
			implies = false;
		} else {
			// we allow something				
			if (javaFilePermission.getActions().contains("read")) {
				implies &= isReadAllowed(); 
			}
			if (javaFilePermission.getActions().contains("write")) {
				implies &= isWriteAllowed();
			}
			if (javaFilePermission.getActions().contains("delete")) {
				implies &= isDeleteAllowed();
			}
			if (javaFilePermission.getActions().contains("execute")) {
				for (IFlowerWebPolicyExtension extension : ((FlowerWebPolicy) Policy.getPolicy()).getExtensions()) {
					if (extension.impliesWithoutTreePathCheck_isExecutable(permission)) {
						return true;
					}
				}
				implies = false;
			}
		}
		return implies;
	}

	/**
	 * @flowerModelElementId _f1AAIGnXEeGiEKNiPvCvPw
	 */
	private boolean isReadAllowed() {
		return getActions().equals(READ) || getActions().equals(READ_WRITE) || getActions().equals(READ_WRITE_DELETE);
	}
	
	/**
	 * @flowerModelElementId _f1AAImnXEeGiEKNiPvCvPw
	 */
	private boolean isWriteAllowed() {
		return getActions().equals(READ_WRITE) || getActions().equals(READ_WRITE_DELETE);
	}
	
	/**
	 * @flowerModelElementId _f1AnMWnXEeGiEKNiPvCvPw
	 */
	private boolean isDeleteAllowed() {
		return getActions().equals(READ_WRITE_DELETE);
	}
	
	/**
	 * @flowerModelElementId _f1AnM2nXEeGiEKNiPvCvPw
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FlowerWebFilePermission)) {
			return false;
		}
		FlowerWebFilePermission other = (FlowerWebFilePermission)obj;
		return getName().equals(other.getName()) && getActions().equals(other.getActions());
	}

	/**
	 * @flowerModelElementId _f1BOQ2nXEeGiEKNiPvCvPw
	 */
	@Override
	public int hashCode() {
		return getName().hashCode() + 3 * getActions().hashCode(); 
	}
}