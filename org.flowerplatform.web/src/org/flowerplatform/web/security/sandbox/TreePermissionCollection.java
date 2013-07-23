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

import java.io.File;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.flowerplatform.web.security.permission.AbstractTreePermission;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;

import org.flowerplatform.web.entity.ISecurityEntity;

/**
 * Contains "tree-style" permissions (or hierarchical permission).
 * The collection is homogeneous (i.e. all the permissions are of same
 * type).
 * 
 * <p>
 * A tree-style permission has a path on which that permission makes sense.
 * Examples of tree-style permissions: file system permission, modify permissions
 * permission.
 * 
 * <p>
 * {@link AbstractTreePermission#AbstractTreePermission()}'s doc specifies conventions 
 * for the path and wildcard.
 * 
 * <p>
 * This class has logic to determine if a permission is given or not, based on
 * path verifications (i.e. verify the checked permission against the list of
 * permissions of this collection). If the algorithm matches some records (i.e. 
 * {@link AbstractTreePermission}) from this collection, it will delegate to the corresponding
 * permission, to continue the evaluation. 
 * 
 * @see AbstractTreePermission
 * 
 * @author Cristi
 * @author Florin
 * 
 * @flowerModelElementId _0lQyMGR1EeGyd4yTk74SKw
 */
public class TreePermissionCollection {
	
	/**
	 * The class of the tree permissions in this {@link TreePermissionCollection}.
	 * 
	 * <p>
	 * Used for sanity check (i.e. ensure that all the permissions are of this type).
	 * @flowerModelElementId _-k6p0GznEeGBsfNm1ipRfw
	 */
	private Class<? extends AbstractTreePermission> treePermissionClass;
	
	/**
	 * Flag indicating if the list is sorted or not.
	 * @flowerModelElementId _-k738GznEeGBsfNm1ipRfw
	 */
	private boolean sorted = false;
	
	/**
	 * @flowerModelElementId _puOcoHJqEeG32IfhnS7SDQ
	 */
	private File root;

	/**
	 * This is a list of entries ordered by path. The paths (names) of
	 * permissions encapsulated by {@link TreePermissionCollectionEntry} are
	 * relative to {@link #root}.
	 * 
	 * @flowerModelElementId _nyCdsGR9EeGyd4yTk74SKw
	 */
	public List<TreePermissionCollectionEntry> entries = new ArrayList<TreePermissionCollectionEntry>();

	/**
	 * @param treePermissionClass - The class of the tree permissions in this {@link TreePermissionCollection}.
	 * @flowerModelElementId _-lB-kGznEeGBsfNm1ipRfw
	 */
	public TreePermissionCollection(Class<? extends AbstractTreePermission> treePermissionClass, File root) {
		if (treePermissionClass == null) {
			throw new IllegalArgumentException("treePermissionClass cannot be null");
		}
		
		this.treePermissionClass = treePermissionClass;
		this.root = root;
	}
	
	/**
	 * Adds a permission to the collection, for the given principal.
	 * Throws an error if the type of <code>permission</code> differs from
	 * the permission type of this {@link TreePermissionCollection}.
	 * 
	 * @flowerModelElementId _66ej8GzdEeGBsfNm1ipRfw
	 */
	public void addPermission(ISecurityEntity securityEntity, AbstractTreePermission permission) {
		if (securityEntity == null || permission == null) {
			throw new IllegalArgumentException(String.format("securityEntity and permission cannot be null. securityEntity = %s, permission = %s", securityEntity, permission));
		}
		if (permission.getClass() != treePermissionClass) {
			throw new IllegalArgumentException(String.format("Attempted to add a permission of type %s in a TreePermissionCollection of type %s", permission.getClass(), treePermissionClass));
		}
		
		entries.add(new TreePermissionCollectionEntry(securityEntity, permission));
		sorted = false;
	}
	
	/**
	 * Checks if the <code>permission</code> is granted to the <code>principal</code>.
	 * 
	 * <p>
	 * Contains the path check algorithm, described in the spec/mind map file.
	 * 
	 * @param permission - Can be an {@link AbstractTreePermission} or normal {@link Permission},
	 * depending on the permission type of this {@link TreePermissionCollection}. E.g. for a 
	 * {@link FlowerWebFilePermission} the parameter would be of type {@link java.io.FilePermission}. 
	 * 
	 * @flowerModelElementId _TXFU4mbMEeGOeOE1u9CeQw
	 */
	public boolean implies(FlowerWebPrincipal principal, Permission permission) {
		
		if (!sorted) {
			// sort entries by permission path		
			Collections.sort(entries, new Comparator<TreePermissionCollectionEntry>() {

				@Override
				public int compare(TreePermissionCollectionEntry e1, TreePermissionCollectionEntry e2) {
					return e1.getPermission().getName().compareTo(e2.getPermission().getName());
				}
			});
			sorted = true;
		}
		
		boolean refused = false;
		String pathRefused = null;
		
		for (int i = entries.size() - 1; i >= 0; i--) {					
			TreePermissionCollectionEntry entry = entries.get(i);			
			AbstractTreePermission availablePermission = entry.getPermission();
			ISecurityEntity securityEntity = entry.getSecurityEntity();
			
			// if we found a permission that denies access
			// we continue searching for permissions that allow access
			// as long as the path is the same. If path changes we do not
			// continue searching.
			if (refused && ! pathRefused.equals(availablePermission.getName())) {
				return false;
			}					
			
			// if permission is java.io.FilePermission, we do not need to worry about * and - inconsistency 
			// because we will be asked for a permission without wildcards
			if (pathMatch(permission.getName(), availablePermission.getName())) { // if path matches
				if (securityEntity.contains(principal.getUser())) { 
					if (availablePermission.impliesWithoutTreePathCheck(permission)) { // and permission is allowed
						return true;
					} else {
						refused = true;
						pathRefused = availablePermission.getName();
					}
				}
			} 
		}
		
		return false;
	}
	
	/**
	 * @param path
	 * @param referencePath - The path of AbtractTreePermission (that supports * wildcard).
	 * @return If <code>path</code> matches <code>referencePath</code>.
	 * 
	 * @flowerModelElementId _f1d6MWnXEeGiEKNiPvCvPw
	 */
	public boolean pathMatch(String path, String referencePath) {
		boolean match = false;
		// referencePath is a path relative to root (the path from a AbstractTreePermission)
		referencePath = new File(root.getPath() + "/" + referencePath).getAbsolutePath().replace("\\", "/");
		// path can be a relative path to user dir, not to root. For example new
		// File("a.txt").canRead will trigger a check for FilePermission("a.txt", "read"). 
		path = new File(path).getAbsolutePath().replace("\\", "/");
		if (referencePath.endsWith(AbstractTreePermission.STAR_WILDCARD)){
			// path=D:/temp/a/file.txt
			// referencePath=D:/temp/*			
			referencePath = referencePath.substring(0, referencePath.length() - 2);
			if (path.startsWith(referencePath)){
				match = true;
			}
		} else if (referencePath.equals(path)) {
			// path=D:/temp/a
			// referencePath=D:/temp/a
			match = true;
		}  
		return match;
	}

	/**
	 * Returns true if the path is a child for the root of this permission
	 * collection.
	 * 
	 * @param path
	 *            a path to be checked if belongs to root. If it a relative path
	 *            it will be considered relative to user directory, not to root.
	 * 
	 * @flowerModelElementId _puRf8HJqEeG32IfhnS7SDQ
	 */
	public boolean belongsToRoot(String path) {
		String rootPath = root.getAbsolutePath().replace("\\", "/");
//		 A call like new File("some_relative_path").canRead will determine a security check for this path.
//		 But this path is relative to user directory, not to this <code>root</root>. 
		path = new File(path).getAbsolutePath().replace("\\", "/"); // path can be relative to user.dir. See getAbsolutePath()
		return path.startsWith(rootPath);
	}
}