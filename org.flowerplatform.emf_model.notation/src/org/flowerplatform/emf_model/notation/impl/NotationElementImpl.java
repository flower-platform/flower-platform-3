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
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.notation.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.flowerplatform.emf_model.notation.NotationElement;
import org.flowerplatform.emf_model.notation.NotationPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.NotationElementImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.NotationElementImpl#getIdBeforeRemoval <em>Id Before Removal</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class NotationElementImpl extends EObjectImpl implements NotationElement {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final long ID_EDEFAULT = 0L;
	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected long id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdBeforeRemoval() <em>Id Before Removal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdBeforeRemoval()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_BEFORE_REMOVAL_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getIdBeforeRemoval() <em>Id Before Removal</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdBeforeRemoval()
	 * @generated
	 * @ordered
	 */
	protected String idBeforeRemoval = ID_BEFORE_REMOVAL_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotationElementImpl() {
		super();
	}

	/**
	 * @author Mariana
	 * @generated NOT
	 */
	@Override
	public NotificationChain eBasicSetContainer(InternalEObject newContainer, int newContainerFeatureID, NotificationChain msgs) {
		NotificationChain notificationChain = super.eBasicSetContainer(newContainer, newContainerFeatureID, msgs);
		if (eResource() != null) {
			setIdBeforeRemoval(eResource().getURIFragment(this));
		}
		return notificationChain;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NotationPackage.Literals.NOTATION_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(long newId) {
		long oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.NOTATION_ELEMENT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getIdBeforeRemoval() {
		return idBeforeRemoval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdBeforeRemoval(String newIdBeforeRemoval) {
		String oldIdBeforeRemoval = idBeforeRemoval;
		idBeforeRemoval = newIdBeforeRemoval;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.NOTATION_ELEMENT__ID_BEFORE_REMOVAL, oldIdBeforeRemoval, idBeforeRemoval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NotationPackage.NOTATION_ELEMENT__ID:
				return getId();
			case NotationPackage.NOTATION_ELEMENT__ID_BEFORE_REMOVAL:
				return getIdBeforeRemoval();
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
			case NotationPackage.NOTATION_ELEMENT__ID:
				setId((Long)newValue);
				return;
			case NotationPackage.NOTATION_ELEMENT__ID_BEFORE_REMOVAL:
				setIdBeforeRemoval((String)newValue);
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
			case NotationPackage.NOTATION_ELEMENT__ID:
				setId(ID_EDEFAULT);
				return;
			case NotationPackage.NOTATION_ELEMENT__ID_BEFORE_REMOVAL:
				setIdBeforeRemoval(ID_BEFORE_REMOVAL_EDEFAULT);
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
			case NotationPackage.NOTATION_ELEMENT__ID:
				return id != ID_EDEFAULT;
			case NotationPackage.NOTATION_ELEMENT__ID_BEFORE_REMOVAL:
				return ID_BEFORE_REMOVAL_EDEFAULT == null ? idBeforeRemoval != null : !ID_BEFORE_REMOVAL_EDEFAULT.equals(idBeforeRemoval);
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
		result.append(" (id: ");
		result.append(id);
		result.append(", idBeforeRemoval: ");
		result.append(idBeforeRemoval);
		result.append(')');
		return result.toString();
	}

} //NotationElementImpl