package org.flowerplatform.web.security.permission;

import java.security.Permission;
import java.util.List;

import org.flowerplatform.web.security.sandbox.SecurityEntityAdaptor;
import org.flowerplatform.web.security.sandbox.SecurityUtils;

import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.PermissionEntity;

/**
 * The owner of this permission can create/update/delete any link {@link PermissionEntity} 
 * with type {@link AbstractTreePermission}  that meets the following conditions:
 * <ul>
 * <li>the modified permission matches with the path of this permission</li>
 * <li>the security entities of the modified permission are included by the actions of this permission</li>
 * </ul>
 * 
 * <p>
 * In case of a permission update, both the old values and the new values of the 
 * updated permission must be allowed by this.
 * 
 * <p>
 * E.g.:
 * name = org1/*, actions = #org1,$user1
 * The owner can create:
 * 1) a PermisisonEntity of type FlowerWebFilePermission with a name(/resource/target/path) that matches org1/* and assigned to security entity contained in #org1,$user1
 * 2) a PermissionEntity of type ModifyTreePermissionsPermission with a name(/resource/target/path) that matches org1/* and assigned to security entity contained in #org1,$user1. 
 * Also, this is a special case: the parameters of the PermissionEntity will be the actions of a ModifyTreePermissionsPermission. So when checking if a PermissionEntity of type ModifyTreePermissionsPermission
 * is permitted, the call will be SecurityManager.checkPermission(ModifyTreePermissionsPermission(PermissionEntity.resource, PermissionEntity.assignedTo + PermissionEntity.actions))
 * 
 * <p>
 * Clarification:
 * PermissionEntity.resource is the name for a permission (AbstractTreePermission).
 * PermissionEntity.actions is actions for a permission.
 * 
 * @author Florin
 * 
 * @flowerModelElementId _Z990YHgGEeGtTo1wOb4S9A
 */
public class ModifyTreePermissionsPermission extends AbstractTreePermission {

	/**
	 * @flowerModelElementId _Z9-bcXgGEeGtTo1wOb4S9A
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PermissionEntities can use SecurityEntities from this list.
	 * Usage means:
	 * <ul>
	 * <li> to appear in PermissionEntities.assignedTo or PermissionEntities.actions when checking for ModifyTreePermissionsPermission </li>
	 * <li> to appear in PermissionEntities.assignedTo in case for a FlowerWebFilePermission (or others) </li>
	 * </ul>
	 * @flowerModelElementId _Z9_CgXgGEeGtTo1wOb4S9A
	 */
	private List<ISecurityEntity> assignableSecurityEntities;

	/**
	 * @param name
	 *            the path on the file system. Attention: if it is a relative
	 *            path, it will be considered relative to user directory, not to
	 *            runtime workspace of running eclipse.
	 * @param actions
	 * @flowerModelElementId _Z-AQoHgGEeGtTo1wOb4S9A
	 */
	public ModifyTreePermissionsPermission(String name, String actions) {
		super(name, actions);		
	}
	
	/**
	 * @flowerModelElementId _Z-BewngGEeGtTo1wOb4S9A
	 */
	@Override
	public boolean impliesWithoutTreePathCheck(Permission permission) {
		if (!(permission instanceof ModifyTreePermissionsPermission)) {
			return false;
		}
		if (actions.equals(PermissionEntity.ANY_ENTITY)) {
			// permission to create any SecurityEntity that is assigned to any security entity 
			return true;
		}
		if (assignableSecurityEntities == null) {			
			assignableSecurityEntities = SecurityEntityAdaptor.csvStringToSecurityEntityList(actions);
		}
				
		ModifyTreePermissionsPermission modifyPermission = (ModifyTreePermissionsPermission) permission;
		// security entities that we must check if they are allowed to be used by a PermissionEntity
		List<ISecurityEntity> securityEntitiesToBeChecked = SecurityEntityAdaptor.csvStringToSecurityEntityList(modifyPermission.actions);
		boolean implies = true; 
		for (ISecurityEntity securityEntity: securityEntitiesToBeChecked) {
			implies &= SecurityUtils.securityEntityIsAssignable(assignableSecurityEntities, securityEntity);
		}
		
		return implies;
	}
	
	/**
	 * @flowerModelElementId _Z-Cs4ngGEeGtTo1wOb4S9A
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ModifyTreePermissionsPermission)) {
			return false;
		}
		
		ModifyTreePermissionsPermission other = (ModifyTreePermissionsPermission) obj;
		return getName().equals(other.getName()) && getActions().equals(other.getActions());
	}

	/**
	 * @flowerModelElementId _Z-EiEXgGEeGtTo1wOb4S9A
	 */
	@Override
	public int hashCode() {
		return getName().hashCode() + 31 * getActions().hashCode();
	}
}
