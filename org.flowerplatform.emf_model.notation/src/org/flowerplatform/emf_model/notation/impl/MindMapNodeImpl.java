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

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.flowerplatform.emf_model.notation.MindMapNode;
import org.flowerplatform.emf_model.notation.NotationPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mind Map Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.MindMapNodeImpl#isExpanded <em>Expanded</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.MindMapNodeImpl#isHasChildren <em>Has Children</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.MindMapNodeImpl#getSide <em>Side</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MindMapNodeImpl extends NodeImpl implements MindMapNode {
	/**
	 * The default value of the '{@link #isExpanded() <em>Expanded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExpanded()
	 * @generated
	 * @ordered
	 */
	protected static final boolean EXPANDED_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isExpanded() <em>Expanded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isExpanded()
	 * @generated
	 * @ordered
	 */
	protected boolean expanded = EXPANDED_EDEFAULT;
	/**
	 * The default value of the '{@link #isHasChildren() <em>Has Children</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasChildren()
	 * @generated
	 * @ordered
	 */
	protected static final boolean HAS_CHILDREN_EDEFAULT = false;
	/**
	 * The cached value of the '{@link #isHasChildren() <em>Has Children</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isHasChildren()
	 * @generated
	 * @ordered
	 */
	protected boolean hasChildren = HAS_CHILDREN_EDEFAULT;
	/**
	 * The default value of the '{@link #getSide() <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSide()
	 * @generated
	 * @ordered
	 */
	protected static final int SIDE_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getSide() <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSide()
	 * @generated
	 * @ordered
	 */
	protected int side = SIDE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MindMapNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NotationPackage.Literals.MIND_MAP_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isExpanded() {
		return expanded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExpanded(boolean newExpanded) {
		boolean oldExpanded = expanded;
		expanded = newExpanded;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.MIND_MAP_NODE__EXPANDED, oldExpanded, expanded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isHasChildren() {
		return hasChildren;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHasChildren(boolean newHasChildren) {
		boolean oldHasChildren = hasChildren;
		hasChildren = newHasChildren;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.MIND_MAP_NODE__HAS_CHILDREN, oldHasChildren, hasChildren));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSide() {
		return side;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSide(int newSide) {
		int oldSide = side;
		side = newSide;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.MIND_MAP_NODE__SIDE, oldSide, side));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NotationPackage.MIND_MAP_NODE__EXPANDED:
				return isExpanded();
			case NotationPackage.MIND_MAP_NODE__HAS_CHILDREN:
				return isHasChildren();
			case NotationPackage.MIND_MAP_NODE__SIDE:
				return getSide();
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
			case NotationPackage.MIND_MAP_NODE__EXPANDED:
				setExpanded((Boolean)newValue);
				return;
			case NotationPackage.MIND_MAP_NODE__HAS_CHILDREN:
				setHasChildren((Boolean)newValue);
				return;
			case NotationPackage.MIND_MAP_NODE__SIDE:
				setSide((Integer)newValue);
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
			case NotationPackage.MIND_MAP_NODE__EXPANDED:
				setExpanded(EXPANDED_EDEFAULT);
				return;
			case NotationPackage.MIND_MAP_NODE__HAS_CHILDREN:
				setHasChildren(HAS_CHILDREN_EDEFAULT);
				return;
			case NotationPackage.MIND_MAP_NODE__SIDE:
				setSide(SIDE_EDEFAULT);
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
			case NotationPackage.MIND_MAP_NODE__EXPANDED:
				return expanded != EXPANDED_EDEFAULT;
			case NotationPackage.MIND_MAP_NODE__HAS_CHILDREN:
				return hasChildren != HAS_CHILDREN_EDEFAULT;
			case NotationPackage.MIND_MAP_NODE__SIDE:
				return side != SIDE_EDEFAULT;
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
		result.append(" (expanded: ");
		result.append(expanded);
		result.append(", hasChildren: ");
		result.append(hasChildren);
		result.append(", side: ");
		result.append(side);
		result.append(')');
		return result.toString();
	}

} //MindMapNodeImpl