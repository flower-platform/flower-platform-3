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