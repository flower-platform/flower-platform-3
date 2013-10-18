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
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.DiagramImpl#getNewElementsPath <em>New Elements Path</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.DiagramImpl#isShowNewElementsPathDialog <em>Show New Elements Path Dialog</em>}</li>
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
	 * The default value of the '{@link #getNewElementsPath() <em>New Elements Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewElementsPath()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_ELEMENTS_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewElementsPath() <em>New Elements Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewElementsPath()
	 * @generated
	 * @ordered
	 */
	protected String newElementsPath = NEW_ELEMENTS_PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #isShowNewElementsPathDialog() <em>Show New Elements Path Dialog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowNewElementsPathDialog()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHOW_NEW_ELEMENTS_PATH_DIALOG_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isShowNewElementsPathDialog() <em>Show New Elements Path Dialog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShowNewElementsPathDialog()
	 * @generated
	 * @ordered
	 */
	protected boolean showNewElementsPathDialog = SHOW_NEW_ELEMENTS_PATH_DIALOG_EDEFAULT;

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
	public String getNewElementsPath() {
		return newElementsPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewElementsPath(String newNewElementsPath) {
		String oldNewElementsPath = newElementsPath;
		newElementsPath = newNewElementsPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.DIAGRAM__NEW_ELEMENTS_PATH, oldNewElementsPath, newElementsPath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isShowNewElementsPathDialog() {
		return showNewElementsPathDialog;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShowNewElementsPathDialog(boolean newShowNewElementsPathDialog) {
		boolean oldShowNewElementsPathDialog = showNewElementsPathDialog;
		showNewElementsPathDialog = newShowNewElementsPathDialog;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.DIAGRAM__SHOW_NEW_ELEMENTS_PATH_DIALOG, oldShowNewElementsPathDialog, showNewElementsPathDialog));
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
			case NotationPackage.DIAGRAM__NEW_ELEMENTS_PATH:
				return getNewElementsPath();
			case NotationPackage.DIAGRAM__SHOW_NEW_ELEMENTS_PATH_DIALOG:
				return isShowNewElementsPathDialog();
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
			case NotationPackage.DIAGRAM__NEW_ELEMENTS_PATH:
				setNewElementsPath((String)newValue);
				return;
			case NotationPackage.DIAGRAM__SHOW_NEW_ELEMENTS_PATH_DIALOG:
				setShowNewElementsPathDialog((Boolean)newValue);
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
			case NotationPackage.DIAGRAM__NEW_ELEMENTS_PATH:
				setNewElementsPath(NEW_ELEMENTS_PATH_EDEFAULT);
				return;
			case NotationPackage.DIAGRAM__SHOW_NEW_ELEMENTS_PATH_DIALOG:
				setShowNewElementsPathDialog(SHOW_NEW_ELEMENTS_PATH_DIALOG_EDEFAULT);
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
			case NotationPackage.DIAGRAM__NEW_ELEMENTS_PATH:
				return NEW_ELEMENTS_PATH_EDEFAULT == null ? newElementsPath != null : !NEW_ELEMENTS_PATH_EDEFAULT.equals(newElementsPath);
			case NotationPackage.DIAGRAM__SHOW_NEW_ELEMENTS_PATH_DIALOG:
				return showNewElementsPathDialog != SHOW_NEW_ELEMENTS_PATH_DIALOG_EDEFAULT;
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
		result.append(", newElementsPath: ");
		result.append(newElementsPath);
		result.append(", showNewElementsPathDialog: ");
		result.append(showNewElementsPathDialog);
		result.append(')');
		return result.toString();
	}

} //DiagramImpl