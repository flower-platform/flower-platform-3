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
import java.security.AccessController;
import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.internal.permadmin.BundlePermissions;
import org.flowerplatform.common.CommonPlugin;
import org.flowerplatform.web.database.DatabaseOperation;
import org.flowerplatform.web.database.DatabaseOperationWrapper;
import org.flowerplatform.web.security.permission.AbstractTreePermission;
import org.flowerplatform.web.security.permission.AdminSecurityEntitiesPermissionDescriptor;
import org.flowerplatform.web.security.permission.FlowerWebFilePermission;
import org.flowerplatform.web.security.permission.FlowerWebFilePermissionDescriptor;
import org.flowerplatform.web.security.permission.ModifyTreePermissionsPermissionDescriptor;
import org.flowerplatform.web.security.permission.PermissionDescriptor;
import org.flowerplatform.web.security.service.PermissionService;
import org.hibernate.Query;

import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.PermissionEntity;
import org.flowerplatform.web.entity.User;

/**
 * This is our custom security policy responsible to check if permissions are allowed
 * to the current principal(s), by using the database and the permission caches that it maintains.
 * 
 * @author Cristi
 * @author Florin
 * 
 * @flowerModelElementId _bPAc8GRnEeGyd4yTk74SKw
 */
@SuppressWarnings("restriction")
public class FlowerWebPolicy extends Policy {
	
	/**
	 * @flowerModelElementId _psld4HJqEeG32IfhnS7SDQ
	 */
	private Policy defaultPolicy;
	
	/**
	 * The descriptors of the permissions that this policy enforces.
	 * 
	 * @see #addPermissionDescriptor()
	 * @flowerModelElementId _izbPoGRyEeGyd4yTk74SKw
	 */
	private PermissionDescriptorsTable permissionDescriptors = new PermissionDescriptorsTable();
	
	/**
	 * Cache for tree permissions. 
	 * The key of this map is the implemented permission type as defined by the permission descriptor.
	 * 
	 * <p>
	 * For a given permission (implementation) type, this cache holds the permissions for ALL users, groups, organizations.
	 * 
	 * @see #getTreePermissions()
	 * @flowerModelElementId _qS2ksGRpEeGyd4yTk74SKw
	 */
	protected Map<Class<? extends AbstractTreePermission>, TreePermissionCollection> treePermissionsCache = Collections.synchronizedMap(new HashMap<Class<? extends AbstractTreePermission>, TreePermissionCollection>());
	
	/**
	 * Cache for normal (not tree) permissions.
	 * The key is the user id. The value is a {@link PermissionCollection}, that contains
	 * all permissions for this user, the groups of the user and organizations of the user.
	 *
	 * @see #getPermissionsForUser()
	 * @see #clearUserPermissionsCache()
	 * 
	 * @flowerModelElementId _WWK-YGRqEeGyd4yTk74SKw
	 */
	protected Map<Long, PermissionCollection> normalPermissionsCache = Collections.synchronizedMap(new HashMap<Long, PermissionCollection>());

	private List<IFlowerWebPolicyExtension> extensions;
	
	/**
	 * Installs the permission descriptors for the built in permission types:
	 * 
	 * <ul>
	 * 	<li>{@link FlowerWebFilePermission}
	 * </ul>
	 * 
	 * @flowerModelElementId _f1H78GnXEeGiEKNiPvCvPw
	 */
	public FlowerWebPolicy(Policy defaultPolicy) {	
		this.defaultPolicy = defaultPolicy;
		addPermissionDescriptor(new AdminSecurityEntitiesPermissionDescriptor());
		addPermissionDescriptor(new FlowerWebFilePermissionDescriptor());
		addPermissionDescriptor(new ModifyTreePermissionsPermissionDescriptor());		
	}
	
	public List<IFlowerWebPolicyExtension> getExtensions() {
		if (extensions == null) {
			extensions = new ArrayList<IFlowerWebPolicyExtension>();
		}
		return extensions;
	}

	public void setExtensions(List<IFlowerWebPolicyExtension> extensions) {
		this.extensions = extensions;
	}
	
	/**
	 * Adds a new permission descriptor.
	 * 
	 * @see #permissionDescriptors
	 * 
	 * @flowerModelElementId _tKCyQGR_EeGyd4yTk74SKw
	 */
	public void addPermissionDescriptor(PermissionDescriptor descriptor) {
		permissionDescriptors.put(descriptor);
	}
	
	/**
	 * Returns an empty collection because we "disabled" this method. 
	 * In theory, only {@link #implies()} of this class is supposed to call this method.
	 * 
	 * @flowerModelElementId _OXSuMGRoEeGyd4yTk74SKw
	 */
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		return new Permissions(); 
	}
	
	/**
	 * Method introduced because connecting JVisualVM through jmx would not work due
	 * to permission problems.
	 * It seemed that when using the default PolicyFile it returned a modifiable {@link PermissionCollection}
	 * along with some permisions in it. I think it is safe to return a modifiable {@link PermissionCollection}
	 * without any permissions inside.
	 * 
	 * @author Sorin
	 */
	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		return new Permissions();
	}
	
	/**
	 * Permission check main logic. Uses the caches (that are populated from the DB) to see
	 * if the permission is granted to the current user (or principal).
	 * 
	 * @flowerModelElementId _B9OS0GRrEeGyd4yTk74SKw
	 */
	public boolean implies(final ProtectionDomain domain, final Permission permission) { 

		if (permission.getClass().getName().equals("javax.security.auth.AuthPermission") && permission.getName().equals("doAsPrivileged")) {
			// coding like this to avoid ClassCircularityError
			// allow this permission for everyone
			return true;
		}
		String flowerWebPolicy$CallPrivilegedImpliesAction_className = FlowerWebPolicy.class.getName() + "$CallPrivilegedImpliesAction";
		String flowerWebPolicy$CallPrivilegedImpliesAction_classFile = flowerWebPolicy$CallPrivilegedImpliesAction_className.replace(".", File.separator) + ".class";
		if (permission.getName().contains(flowerWebPolicy$CallPrivilegedImpliesAction_classFile) 
				&& permission.getActions().equals("read")) {
			// The code below tries to read CallPrivilegedImpliesAction class, which triggers a security check (read permission for the class file).
			// As a result this method is called and the code below tries to read CallPrivilegedImpliesAction, .... and so a ClassCircularityError occurs.
			// So we allow this permission to avoid this error.
			return true;
		}		
		if (permission.getClass().getName().equals("java.util.PropertyPermission") && 
				permission.getName().equals("user.dir") && 
				permission.getActions().equals("read")) {			
			// allow user.dir permission for everyone
			return true;
		}
		
		// AccessController.doPrivileged means that a new access control context
		// will be created that will have a single bundle protection domain (the
		// one for *.mp.web, that has all privileges).
		// This is because we need to have all permissions in order to be able
		// to decide if a permission is allowed. For example,
		// File.getAbsolutePath, called in
		// TreePermissionCollection will require access to read the system
		// property user.dir.
		// The protection domains in the access control context might or might
		// have not this permission. So we make a privileged call
		// to ensure that required permission is allowed.
		return AccessController.doPrivileged(new CallPrivilegedImpliesAction(domain, permission), null); // call to privilegedImplies		
	}
	
	/**
	 * @flowerModelElementId _xK_hgHQdEeGvmLPkt9AQrg
	 */
	@SuppressWarnings({ "unchecked" })
	private boolean privilegedImplies(ProtectionDomain domain, Permission permission) {
		boolean implies = false;
				
		PermissionDescriptor descriptor = permissionDescriptors.getByHandledPermissionType(permission.getClass());
		TreePermissionCollection treePermissionCollection = null;		
		if (domain.getPrincipals().length > 0 && descriptor != null && descriptor.isTreePermission()) {			
			treePermissionCollection = getTreePermissions((Class<? extends AbstractTreePermission>) descriptor.getImplementedPermissionType());
		}
				
		if (domain.getPrincipals().length > 0 && treePermissionCollection != null && treePermissionCollection.belongsToRoot(permission.getName())) {
			// when domain has a principal and requests a permission from eclipse workspace, we handle this by verifying our 
			// database permissions			
			FlowerWebPrincipal principal = (FlowerWebPrincipal) domain.getPrincipals()[0];
			implies = treePermissionCollection.implies((FlowerWebPrincipal) principal, permission);
			if (!implies) {
				// if this policy denies permission, an exception is thrown to stop ProtectionDomain to check its permissions
				// if this would return false, ProtectionDomain would ask its BundlesPermissions (osgi permissions table)
				throw new FlowerWebSecurityException(String.format("Permission denied for %s", permission));
			}
		} else if (domain.getPrincipals().length > 0 && descriptor != null && !descriptor.isTreePermission()) {
			FlowerWebPrincipal principal = (FlowerWebPrincipal) domain.getPrincipals()[0];
			PermissionCollection permissions = getNormalPermissions(principal.getUser());
			implies = permissions.implies(permission);
			if (!implies) {
				throw new FlowerWebSecurityException(String.format("Permission denied for %s", permission));
			}
		} else if (domain.getPermissions() instanceof BundlePermissions) {
			// let BundlePermissions from ProtectionDomain decide
			// if osgi security table is empty (currently it is), all permissions will be allowed
			// see SecurityAdmin.checkPermission() and SecurityAdmin.DEFAULT_DEFAULT; 
			implies = false;
		} else /*if (domain.getPrincipals().length == 0)*/ {
			// There will be some occasions when the domain it is not the domain of an osgi bundle.
			// This is true for web server code (lib/catalina.jar, bin/bootstrap.jar etc.), 
			// for org.eclipse.osgi_3.5.2.R35x_v20100126.jar, for org.flowerplatform.web.app project.
			// This means we allow the requested permission (as all.policy gives all permission to everyone) 
			implies = defaultPolicy.implies(domain, permission);
		}
		
		return implies;
	}
	
	
	/**
	 * The path (a.k.a resource, name) of a tree permission is relative to the workspace.
	 * 
	 * @flowerModelElementId _psohMXJqEeG32IfhnS7SDQ
	 */
	public File getRuntimeWorkspace() {
		return CommonPlugin.getInstance().getWorkspaceRoot();
	}
	
	/**
	 * Returns the tree permission cache for the specified type.
	 * If the cache is empty, it populates it from the DB.
	 * 
	 * @flowerModelElementId _M3ZE4GRsEeGyd4yTk74SKw
	 */
	private TreePermissionCollection getTreePermissions(Class<? extends AbstractTreePermission> treePermissionType) {
		PermissionService permissionService = PermissionService.getInstance();
		TreePermissionCollection treePermissionCollection = treePermissionsCache.get(treePermissionType);		
		if (treePermissionCollection == null) {
			// permissions are ordered by path
			List<org.flowerplatform.web.entity.PermissionEntity> permissions = permissionService.findPermissionsByType(treePermissionType.getName());

			treePermissionCollection = new TreePermissionCollection(treePermissionType, getRuntimeWorkspace());
			for (org.flowerplatform.web.entity.PermissionEntity permissionEntity : permissions) {
				// use eager loading for the entity's associations because we're keeping the entity in the cache
				ISecurityEntity securityEntity = SecurityEntityAdaptor.toSecurityEntity(permissionEntity.getAssignedTo(), true);
				AbstractTreePermission treePermission = (AbstractTreePermission) permissionService.createPermission(permissionEntity);
				treePermissionCollection.addPermission(securityEntity, treePermission);
			}
			treePermissionsCache.put(treePermissionType, treePermissionCollection);
		}
		return treePermissionCollection;
	}

	/**
	 * Returns all the permissions that are enforced for the user (the permissions for the user,
	 * the permissions for the user's group and the permissions for the user's organizations).
	 * 
	 * @author Cristi
	 * @author Florin
	 * @autor Mariana
	 * 
	 * @flowerModelElementId _xLB9wHQdEeGvmLPkt9AQrg
	 */
	PermissionCollection getNormalPermissions(final User user) {	
		if (normalPermissionsCache.get(user.getId()) == null) {
			final Permissions permissions = new Permissions();
			
			new DatabaseOperationWrapper(new DatabaseOperation() {
				
				@Override
				public void run() {
					
					Query q = wrapper.createQuery("SELECT p " +
											"FROM PermissionEntity p " +
											"WHERE p.assignedTo = :assigned_to " +
											"OR p.assignedTo = '@ALL'" +
											"OR EXISTS (SELECT g.name FROM User u JOIN u.groupUsers gu JOIN gu.group g WHERE u.login = :login AND p.assignedTo = CONCAT('@', g.name)) "+
											"OR EXISTS (SELECT o2.name FROM User u2 JOIN u2.organizationUsers ou2 JOIN ou2.organization o2 WHERE u2.login = :login AND p.assignedTo = CONCAT('#', o2.name)) ");
					
					q.setParameter("assigned_to", '$' + user.getLogin());
					q.setParameter("login", user.getLogin());

					@SuppressWarnings("unchecked")
					List<PermissionEntity> resultList = q.list();
					PermissionService permissionService = (PermissionService) PermissionService.getInstance();
					for (PermissionEntity entity: resultList) {				
						PermissionDescriptor descriptor = getPermissionDescriptor(entity.getType());
						if (!descriptor.isTreePermission()) {
							permissions.add(permissionService.createPermission(entity));
						}
					}
					normalPermissionsCache.put(user.getId(), permissions);
				}
			});
		}
		return normalPermissionsCache.get(user.getId());
	}

	/**
	 * If <code>permissionEntity</code> is of tree type, this method will clear
	 * the entry for the implemented permission type (as defined by IPermissionDescriptor) 
	 * from {@link #treePermissionsCache}. If  <code>permissionEntity</code> is a normal 
	 * permission, this method will clear {@link #normalPermissionsCache}.
	 * @flowerModelElementId _xLDL4HQdEeGvmLPkt9AQrg
	 */
	public void updateCachesFor(PermissionEntity permissionEntity) {		
		PermissionDescriptor descriptor = getPermissionDescriptor(permissionEntity.getType());
		
		if (descriptor.isTreePermission()) {
			treePermissionsCache.remove(descriptor.getImplementedPermissionType());
		} else {
			normalPermissionsCache.clear();
		}
	}
	
	/**
	 * Caches are cleared.
	 * @flowerModelElementId _xLEaAHQdEeGvmLPkt9AQrg
	 */
	public void updateCachesFor(ISecurityEntity securityEntity) {
		// FlowerWebPolicy.treePermissionsCache must be updated because the entries in the TreePermissionCollection have an ISecurityEntity.		
		treePermissionsCache.clear();
		// FlowerWebPolicy.normalPermissionsCache must be updated when user, group or organization changes (because user might not belong anymore to group/organization)
		if (securityEntity instanceof User) {
			User user = (User) securityEntity;
			normalPermissionsCache.remove(user.getId());
		} else {
			normalPermissionsCache.clear();
		}
	}
	
	/**
	 * @flowerModelElementId _SdTGYIIZEeGPwv1h63g-uQ
	 */
	public List<PermissionDescriptor> getPermissionDescriptors() {
		return permissionDescriptors.getPermissionDescriptors();
	}
	
	/**
	 * @flowerModelElementId _Jm2XsHmVEeG7_fzMWZxiHA
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PermissionDescriptor getPermissionDescriptor(String permissionEntityType) {
		Class clazz;
		try {
			clazz = Class.forName(permissionEntityType);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		return permissionDescriptors.getByImplementedPermissionType(clazz);
	}
	
	/**
	 * This calls the privilegedImplies method.
	 *
	 */
	class CallPrivilegedImpliesAction implements PrivilegedAction<Boolean> {

		private ProtectionDomain domain;
		
		private Permission permission;
		
		public CallPrivilegedImpliesAction(ProtectionDomain domain, Permission permission) {
			this.domain = domain;
			this.permission = permission;
		}
		
		@Override
		public Boolean run() {
			return privilegedImplies(domain, permission);
		}
		
	}
}