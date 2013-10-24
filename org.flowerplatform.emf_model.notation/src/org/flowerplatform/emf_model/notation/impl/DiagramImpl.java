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

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.flowerplatform.emf_model.notation.Diagram;
import org.flowerplatform.emf_model.notation.Edge;
import org.flowerplatform.emf_model.notation.NotationPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Diagram</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.DiagramImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.DiagramImpl#getPersistentEdges <em>Persistent Edges</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.DiagramImpl#getLocationForNewElements <em>Location For New Elements</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.DiagramImpl#isShowLocationForNewElementsDialog <em>Show Location For New Elements Dialog</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DiagramImpl extends ViewImpl implements Diagram {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPersistentEdges() <em>Persistent Edges</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPersistentEdges()
	 * @generated
	 * @ordered
	 */
	protected EList<Edge> persistentEdges;

	/**
	 * The default value of the '{@link #getLocationForNewElements() <em>Location For New Elements</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationForNewElements()
	 * @generated
	 * @ordered
	 */
	protected static final String LOCATION_FOR_NEW_ELEMENTS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocationForNewElements() <em>Location For New Elements</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationForNewElements()
	 * @generated
	 * @ordered
	 */
	protected String locationForNewElements = LOCATION_FOR_NEW_ELEMENTS_EDEFAULT;

	/**
	 * The default value of the '{@link #isShowLocationForNewElementsDialog() <em>Show Location For New Elements Dialog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowLocationForNewElementsDialog()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isShowLocationForNewElementsDialog() <em>Show Location For New Elements Dialog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowLocationForNewElementsDialog()
	 * @generated
	 * @ordered
	 */
	protected boolean showLocationForNewElementsDialog = SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DiagramImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NotationPackage.Literals.DIAGRAM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.DIAGRAM__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Edge> getPersistentEdges() {
		if (persistentEdges == null) {
			persistentEdges = new EObjectContainmentEList<Edge>(Edge.class, this, NotationPackage.DIAGRAM__PERSISTENT_EDGES);
		}
		return persistentEdges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getLocationForNewElements() {
		return locationForNewElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocationForNewElements(String newLocationForNewElements) {
		String oldLocationForNewElements = locationForNewElements;
		locationForNewElements = newLocationForNewElements;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.DIAGRAM__LOCATION_FOR_NEW_ELEMENTS, oldLocationForNewElements, locationForNewElements));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isShowLocationForNewElementsDialog() {
		return showLocationForNewElementsDialog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShowLocationForNewElementsDialog(boolean newShowLocationForNewElementsDialog) {
		boolean oldShowLocationForNewElementsDialog = showLocationForNewElementsDialog;
		showLocationForNewElementsDialog = newShowLocationForNewElementsDialog;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.DIAGRAM__SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG, oldShowLocationForNewElementsDialog, showLocationForNewElementsDialog));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case NotationPackage.DIAGRAM__PERSISTENT_EDGES:
				return ((InternalEList<?>)getPersistentEdges()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NotationPackage.DIAGRAM__NAME:
				return getName();
			case NotationPackage.DIAGRAM__PERSISTENT_EDGES:
				return getPersistentEdges();
			case NotationPackage.DIAGRAM__LOCATION_FOR_NEW_ELEMENTS:
				return getLocationForNewElements();
			case NotationPackage.DIAGRAM__SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG:
				return isShowLocationForNewElementsDialog();
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
			case NotationPackage.DIAGRAM__NAME:
				setName((String)newValue);
				return;
			case NotationPackage.DIAGRAM__PERSISTENT_EDGES:
				getPersistentEdges().clear();
				getPersistentEdges().addAll((Collection<? extends Edge>)newValue);
				return;
			case NotationPackage.DIAGRAM__LOCATION_FOR_NEW_ELEMENTS:
				setLocationForNewElements((String)newValue);
				return;
			case NotationPackage.DIAGRAM__SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG:
				setShowLocationForNewElementsDialog((Boolean)newValue);
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
			case NotationPackage.DIAGRAM__NAME:
				setName(NAME_EDEFAULT);
				return;
			case NotationPackage.DIAGRAM__PERSISTENT_EDGES:
				getPersistentEdges().clear();
				return;
			case NotationPackage.DIAGRAM__LOCATION_FOR_NEW_ELEMENTS:
				setLocationForNewElements(LOCATION_FOR_NEW_ELEMENTS_EDEFAULT);
				return;
			case NotationPackage.DIAGRAM__SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG:
				setShowLocationForNewElementsDialog(SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_EDEFAULT);
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
			case NotationPackage.DIAGRAM__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case NotationPackage.DIAGRAM__PERSISTENT_EDGES:
				return persistentEdges != null && !persistentEdges.isEmpty();
			case NotationPackage.DIAGRAM__LOCATION_FOR_NEW_ELEMENTS:
				return LOCATION_FOR_NEW_ELEMENTS_EDEFAULT == null ? locationForNewElements != null : !LOCATION_FOR_NEW_ELEMENTS_EDEFAULT.equals(locationForNewElements);
			case NotationPackage.DIAGRAM__SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG:
				return showLocationForNewElementsDialog != SHOW_LOCATION_FOR_NEW_ELEMENTS_DIALOG_EDEFAULT;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", locationForNewElements: ");
		result.append(locationForNewElements);
		result.append(", showLocationForNewElementsDialog: ");
		result.append(showLocationForNewElementsDialog);
		result.append(')');
		return result.toString();
	}

} //DiagramImpl