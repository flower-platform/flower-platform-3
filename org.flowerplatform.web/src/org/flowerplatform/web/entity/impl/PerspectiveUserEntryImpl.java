/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.web.entity.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.flowerplatform.web.entity.EntityPackage;
import org.flowerplatform.web.entity.PerspectiveUserEntry;
import org.flowerplatform.web.entity.User;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Perspective User Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.web.entity.impl.PerspectiveUserEntryImpl#getUser <em>User</em>}</li>
 *   <li>{@link org.flowerplatform.web.entity.impl.PerspectiveUserEntryImpl#getSerializedLayoutData <em>Serialized Layout Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PerspectiveUserEntryImpl extends NamedEntityImpl implements PerspectiveUserEntry {
	/**
	 * The cached value of the '{@link #getUser() <em>User</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUser()
	 * @generated
	 * @ordered
	 */
	protected User user;

	/**
	 * The default value of the '{@link #getSerializedLayoutData() <em>Serialized Layout Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSerializedLayoutData()
	 * @generated
	 * @ordered
	 */
	protected static final String SERIALIZED_LAYOUT_DATA_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSerializedLayoutData() <em>Serialized Layout Data</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSerializedLayoutData()
	 * @generated
	 * @ordered
	 */
	protected String serializedLayoutData = SERIALIZED_LAYOUT_DATA_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PerspectiveUserEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EntityPackage.Literals.PERSPECTIVE_USER_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User getUser() {
		if (user != null && user.eIsProxy()) {
			InternalEObject oldUser = (InternalEObject)user;
			user = (User)eResolveProxy(oldUser);
			if (user != oldUser) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EntityPackage.PERSPECTIVE_USER_ENTRY__USER, oldUser, user));
			}
		}
		return user;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public User basicGetUser() {
		return user;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUser(User newUser) {
		User oldUser = user;
		user = newUser;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.PERSPECTIVE_USER_ENTRY__USER, oldUser, user));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSerializedLayoutData() {
		return serializedLayoutData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSerializedLayoutData(String newSerializedLayoutData) {
		String oldSerializedLayoutData = serializedLayoutData;
		serializedLayoutData = newSerializedLayoutData;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EntityPackage.PERSPECTIVE_USER_ENTRY__SERIALIZED_LAYOUT_DATA, oldSerializedLayoutData, serializedLayoutData));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case EntityPackage.PERSPECTIVE_USER_ENTRY__USER:
				if (resolve) return getUser();
				return basicGetUser();
			case EntityPackage.PERSPECTIVE_USER_ENTRY__SERIALIZED_LAYOUT_DATA:
				return getSerializedLayoutData();
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
			case EntityPackage.PERSPECTIVE_USER_ENTRY__USER:
				setUser((User)newValue);
				return;
			case EntityPackage.PERSPECTIVE_USER_ENTRY__SERIALIZED_LAYOUT_DATA:
				setSerializedLayoutData((String)newValue);
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
			case EntityPackage.PERSPECTIVE_USER_ENTRY__USER:
				setUser((User)null);
				return;
			case EntityPackage.PERSPECTIVE_USER_ENTRY__SERIALIZED_LAYOUT_DATA:
				setSerializedLayoutData(SERIALIZED_LAYOUT_DATA_EDEFAULT);
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
			case EntityPackage.PERSPECTIVE_USER_ENTRY__USER:
				return user != null;
			case EntityPackage.PERSPECTIVE_USER_ENTRY__SERIALIZED_LAYOUT_DATA:
				return SERIALIZED_LAYOUT_DATA_EDEFAULT == null ? serializedLayoutData != null : !SERIALIZED_LAYOUT_DATA_EDEFAULT.equals(serializedLayoutData);
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
		result.append(" (serializedLayoutData: ");
		result.append(serializedLayoutData);
		result.append(')');
		return result.toString();
	}

} //PerspectiveUserEntryImpl
