/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.impl;

import java.util.Date;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.flowerplatform.web.entity.EntityPackage;
import org.flowerplatform.web.entity.Organization;
import org.flowerplatform.web.entity.RecentResource;
import org.flowerplatform.web.entity.User;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Recent Resource</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.impl.RecentResourceImpl#getLabel <em>Label</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.RecentResourceImpl#getPath <em>Path</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.RecentResourceImpl#getOrganization <em>Organization</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.RecentResourceImpl#getLastAccess <em>Last Access</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.RecentResourceImpl#getLastAccessUser <em>Last Access User</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.RecentResourceImpl#getLastSave <em>Last Save</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.RecentResourceImpl#getLastSaveUser <em>Last Save User</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RecentResourceImpl extends EntityImpl implements RecentResource {
	/**
	 * The default value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected static final String LABEL_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLabel() <em>Label</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLabel()
	 * @generated
	 * @ordered
	 */
	protected String label = LABEL_EDEFAULT;

	/**
	 * The default value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected static final String PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPath() <em>Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPath()
	 * @generated
	 * @ordered
	 */
	protected String path = PATH_EDEFAULT;

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
	 * The default value of the '{@link #getLastAccess() <em>Last Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastAccess()
	 * @generated
	 * @ordered
	 */
	protected static final Date LAST_ACCESS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLastAccess() <em>Last Access</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastAccess()
	 * @generated
	 * @ordered
	 */
	protected Date lastAccess = LAST_ACCESS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLastAccessUser() <em>Last Access User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastAccessUser()
	 * @generated
	 * @ordered
	 */
	protected User lastAccessUser;

	/**
	 * The default value of the '{@link #getLastSave() <em>Last Save</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastSave()
	 * @generated
	 * @ordered
	 */
	protected static final Date LAST_SAVE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLastSave() <em>Last Save</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastSave()
	 * @generated
	 * @ordered
	 */
	protected Date lastSave = LAST_SAVE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLastSaveUser() <em>Last Save User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastSaveUser()
	 * @generated
	 * @ordered
	 */
	protected User lastSaveUser;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RecentResourceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EntityPackage.Literals.RECENT_RESOURCE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLabel(String newLabel) {
		String oldLabel = label;
		label = newLabel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.RECENT_RESOURCE__LABEL, oldLabel, label));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPath() {
		return path;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPath(String newPath) {
		String oldPath = path;
		path = newPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.RECENT_RESOURCE__PATH, oldPath, path));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EntityPackage.RECENT_RESOURCE__ORGANIZATION, oldOrganization, organization));
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
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.RECENT_RESOURCE__ORGANIZATION, oldOrganization, organization));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLastAccess() {
		return lastAccess;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastAccess(Date newLastAccess) {
		Date oldLastAccess = lastAccess;
		lastAccess = newLastAccess;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.RECENT_RESOURCE__LAST_ACCESS, oldLastAccess, lastAccess));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User getLastAccessUser() {
		if (lastAccessUser != null && lastAccessUser.eIsProxy()) {
			InternalEObject oldLastAccessUser = (InternalEObject)lastAccessUser;
			lastAccessUser = (User)eResolveProxy(oldLastAccessUser);
			if (lastAccessUser != oldLastAccessUser) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EntityPackage.RECENT_RESOURCE__LAST_ACCESS_USER, oldLastAccessUser, lastAccessUser));
			}
		}
		return lastAccessUser;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User basicGetLastAccessUser() {
		return lastAccessUser;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastAccessUser(User newLastAccessUser) {
		User oldLastAccessUser = lastAccessUser;
		lastAccessUser = newLastAccessUser;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.RECENT_RESOURCE__LAST_ACCESS_USER, oldLastAccessUser, lastAccessUser));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getLastSave() {
		return lastSave;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastSave(Date newLastSave) {
		Date oldLastSave = lastSave;
		lastSave = newLastSave;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.RECENT_RESOURCE__LAST_SAVE, oldLastSave, lastSave));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User getLastSaveUser() {
		if (lastSaveUser != null && lastSaveUser.eIsProxy()) {
			InternalEObject oldLastSaveUser = (InternalEObject)lastSaveUser;
			lastSaveUser = (User)eResolveProxy(oldLastSaveUser);
			if (lastSaveUser != oldLastSaveUser) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EntityPackage.RECENT_RESOURCE__LAST_SAVE_USER, oldLastSaveUser, lastSaveUser));
			}
		}
		return lastSaveUser;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User basicGetLastSaveUser() {
		return lastSaveUser;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastSaveUser(User newLastSaveUser) {
		User oldLastSaveUser = lastSaveUser;
		lastSaveUser = newLastSaveUser;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.RECENT_RESOURCE__LAST_SAVE_USER, oldLastSaveUser, lastSaveUser));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EntityPackage.RECENT_RESOURCE__LABEL:
				return getLabel();
			case EntityPackage.RECENT_RESOURCE__PATH:
				return getPath();
			case EntityPackage.RECENT_RESOURCE__ORGANIZATION:
				if (resolve) return getOrganization();
				return basicGetOrganization();
			case EntityPackage.RECENT_RESOURCE__LAST_ACCESS:
				return getLastAccess();
			case EntityPackage.RECENT_RESOURCE__LAST_ACCESS_USER:
				if (resolve) return getLastAccessUser();
				return basicGetLastAccessUser();
			case EntityPackage.RECENT_RESOURCE__LAST_SAVE:
				return getLastSave();
			case EntityPackage.RECENT_RESOURCE__LAST_SAVE_USER:
				if (resolve) return getLastSaveUser();
				return basicGetLastSaveUser();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case EntityPackage.RECENT_RESOURCE__LABEL:
				setLabel((String)newValue);
				return;
			case EntityPackage.RECENT_RESOURCE__PATH:
				setPath((String)newValue);
				return;
			case EntityPackage.RECENT_RESOURCE__ORGANIZATION:
				setOrganization((Organization)newValue);
				return;
			case EntityPackage.RECENT_RESOURCE__LAST_ACCESS:
				setLastAccess((Date)newValue);
				return;
			case EntityPackage.RECENT_RESOURCE__LAST_ACCESS_USER:
				setLastAccessUser((User)newValue);
				return;
			case EntityPackage.RECENT_RESOURCE__LAST_SAVE:
				setLastSave((Date)newValue);
				return;
			case EntityPackage.RECENT_RESOURCE__LAST_SAVE_USER:
				setLastSaveUser((User)newValue);
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
			case EntityPackage.RECENT_RESOURCE__LABEL:
				setLabel(LABEL_EDEFAULT);
				return;
			case EntityPackage.RECENT_RESOURCE__PATH:
				setPath(PATH_EDEFAULT);
				return;
			case EntityPackage.RECENT_RESOURCE__ORGANIZATION:
				setOrganization((Organization)null);
				return;
			case EntityPackage.RECENT_RESOURCE__LAST_ACCESS:
				setLastAccess(LAST_ACCESS_EDEFAULT);
				return;
			case EntityPackage.RECENT_RESOURCE__LAST_ACCESS_USER:
				setLastAccessUser((User)null);
				return;
			case EntityPackage.RECENT_RESOURCE__LAST_SAVE:
				setLastSave(LAST_SAVE_EDEFAULT);
				return;
			case EntityPackage.RECENT_RESOURCE__LAST_SAVE_USER:
				setLastSaveUser((User)null);
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
			case EntityPackage.RECENT_RESOURCE__LABEL:
				return LABEL_EDEFAULT == null ? label != null : !LABEL_EDEFAULT.equals(label);
			case EntityPackage.RECENT_RESOURCE__PATH:
				return PATH_EDEFAULT == null ? path != null : !PATH_EDEFAULT.equals(path);
			case EntityPackage.RECENT_RESOURCE__ORGANIZATION:
				return organization != null;
			case EntityPackage.RECENT_RESOURCE__LAST_ACCESS:
				return LAST_ACCESS_EDEFAULT == null ? lastAccess != null : !LAST_ACCESS_EDEFAULT.equals(lastAccess);
			case EntityPackage.RECENT_RESOURCE__LAST_ACCESS_USER:
				return lastAccessUser != null;
			case EntityPackage.RECENT_RESOURCE__LAST_SAVE:
				return LAST_SAVE_EDEFAULT == null ? lastSave != null : !LAST_SAVE_EDEFAULT.equals(lastSave);
			case EntityPackage.RECENT_RESOURCE__LAST_SAVE_USER:
				return lastSaveUser != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (label: ");
		result.append(label);
		result.append(", path: ");
		result.append(path);
		result.append(", lastAccess: ");
		result.append(lastAccess);
		result.append(", lastSave: ");
		result.append(lastSave);
		result.append(')');
		return result.toString();
	}

} //RecentResourceImpl
