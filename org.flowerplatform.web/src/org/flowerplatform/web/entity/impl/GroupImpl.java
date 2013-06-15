/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.flowerplatform.web.entity.EntityPackage;
import org.flowerplatform.web.entity.Group;
import org.flowerplatform.web.entity.GroupUser;
import org.flowerplatform.web.entity.ISecurityEntity;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.User;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Group</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.impl.GroupImpl#getOrganization <em>Organization</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.GroupImpl#getGroupUsers <em>Group Users</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GroupImpl extends NamedEntityImpl implements Group {
	/**
	 * The cached value of the '{@link #getOrganization() <em>Organization</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrganization()
	 * @generated
	 * @ordered
	 */
	protected Organization organization;

	/**
	 * The cached value of the '{@link #getGroupUsers() <em>Group Users</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupUsers()
	 * @generated
	 * @ordered
	 */
	protected EList<GroupUser> groupUsers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EntityPackage.Literals.GROUP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Organization getOrganization() {
		if (organization != null && organization.eIsProxy()) {
			InternalEObject oldOrganization = (InternalEObject)organization;
			organization = (Organization)eResolveProxy(oldOrganization);
			if (organization != oldOrganization) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EntityPackage.GROUP__ORGANIZATION, oldOrganization, organization));
			}
		}
		return organization;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Organization basicGetOrganization() {
		return organization;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOrganization(Organization newOrganization) {
		Organization oldOrganization = organization;
		organization = newOrganization;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.GROUP__ORGANIZATION, oldOrganization, organization));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GroupUser> getGroupUsers() {
		if (groupUsers == null) {
			groupUsers = new EObjectResolvingEList<GroupUser>(GroupUser.class, this, EntityPackage.GROUP__GROUP_USERS);
		}
		return groupUsers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 * @author Mariana
	 */
	public boolean contains(ISecurityEntity securityEntity) {
		boolean includes = false;
		
		if (securityEntity instanceof User) {
			User user = (User) securityEntity;
			if (ALL.equals(getName())) {
				includes = true;
			} else {
				for (GroupUser groupUser: groupUsers) {
					if (user.equals(groupUser.getUser())) {
						includes = true;
						break;
					}
				}
			}
		} else if (securityEntity instanceof Group) {
			includes = this.equals(securityEntity);
		} 		

		return includes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EntityPackage.GROUP__ORGANIZATION:
				if (resolve) return getOrganization();
				return basicGetOrganization();
			case EntityPackage.GROUP__GROUP_USERS:
				return getGroupUsers();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EntityPackage.GROUP__ORGANIZATION:
				setOrganization((Organization)newValue);
				return;
			case EntityPackage.GROUP__GROUP_USERS:
				getGroupUsers().clear();
				getGroupUsers().addAll((Collection<? extends GroupUser>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case EntityPackage.GROUP__ORGANIZATION:
				setOrganization((Organization)null);
				return;
			case EntityPackage.GROUP__GROUP_USERS:
				getGroupUsers().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case EntityPackage.GROUP__ORGANIZATION:
				return organization != null;
			case EntityPackage.GROUP__GROUP_USERS:
				return groupUsers != null && !groupUsers.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //GroupImpl
