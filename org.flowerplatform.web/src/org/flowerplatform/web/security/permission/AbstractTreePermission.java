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

import org.flowerplatform.web.security.sandbox.TreePermissionCollection;


/**
 * This class should be extended by tree-style permissions.
 * 
 * <p>
 * This kind of permissions always belong to a {@link TreePermissionCollection}, and they
 * should never be evaluated out of their parent {@link TreePermissionCollection} context.
 * If this is attempted via the standard {@link #implies(Permission)} method return false, so there
 * is no security risk.
 * 
 * <p>
 * Subclasses need to implement {@link #impliesWithoutTreePathCheck()}.
 *
 * @see TreePermissionCollection
 * 
 * @author Cristi
 * @author Florin
 * @flowerModelElementId _TWzBAGbMEeGOeOE1u9CeQw
 */
public abstract class AbstractTreePermission extends Permission {

	/**
	 * @flowerModelElementId _jSsrsGxsEeGBsfNm1ipRfw
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @flowerModelElementId _f03dQWnXEeGiEKNiPvCvPw
	 */
	public static final String STAR_WILDCARD = "*";
	
	/**
	 * @see Getter.
	 * 
	 * @flowerModelElementId _f04EUWnXEeGiEKNiPvCvPw
	 */
	protected String actions;
	
	/**
	 * The reason for which the constructor takes as argument a
	 * TreePermissionCollection is that this class is an implementation detail
	 * for TreePermissionCollection and should not be used outside of this
	 * context.
	 * 
	 * @param path - The path supports the * wildcard that it means all subfolders and files
	 * recursively. Note that this is different from java.io.FilePermission,
	 * where - has this meaning. This path is relative as described in {@link TreePermissionCollection}
	 * 
	 * @flowerModelElementId _f04rYGnXEeGiEKNiPvCvPw
	 */
	public AbstractTreePermission(String path, String actions) {
		super(path);
		this.actions = actions;
	}
	
	/**
	 * Parameters (or actions) for the current permission. Same meaning as {@link Permission#getActions()}.
	 * 
	 * @flowerModelElementId _f055gmnXEeGiEKNiPvCvPw
	 */
	public String getActions() {
		return actions;
	}

	/**
	 * This method is "de-activated", so that it cannot be used as a normal
	 * <code>java.security.Permission</code>.
	 * @flowerModelElementId _cLwaAGxzEeGBsfNm1ipRfw
	 */
	public final boolean implies(Permission permission) {
		throw new IllegalAccessError(String.format("AbstractTreePermission.implies() invoked. This kind of permission cannot be" +
				"evaluated outside a TreePermissionCollection. Argument was %s", permission));
	}
	
	/**
	 * This method needs to be implemented to check the given parameter.
	 * 
	 * <p>
	 * When this method is called, one can assume that the path from the tree
	 * has already been checked in TreePermissionCollection and it is a match.
	 * Implementation of this method should only check the actions.
	 * 
	 * @flowerModelElementId _TWzoFGbMEeGOeOE1u9CeQw
	 */
	public abstract boolean impliesWithoutTreePathCheck(Permission permission);
}