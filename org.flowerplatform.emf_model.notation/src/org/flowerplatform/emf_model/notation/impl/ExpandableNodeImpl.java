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

import org.flowerplatform.emf_model.notation.ExpandableNode;
import org.flowerplatform.emf_model.notation.NotationPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expandable Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.ExpandableNodeImpl#isExpanded <em>Expanded</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.ExpandableNodeImpl#isHasChildren <em>Has Children</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExpandableNodeImpl extends NodeImpl implements ExpandableNode {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExpandableNodeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NotationPackage.Literals.EXPANDABLE_NODE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.EXPANDABLE_NODE__EXPANDED, oldExpanded, expanded));
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
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.EXPANDABLE_NODE__HAS_CHILDREN, oldHasChildren, hasChildren));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NotationPackage.EXPANDABLE_NODE__EXPANDED:
				return isExpanded();
			case NotationPackage.EXPANDABLE_NODE__HAS_CHILDREN:
				return isHasChildren();
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
			case NotationPackage.EXPANDABLE_NODE__EXPANDED:
				setExpanded((Boolean)newValue);
				return;
			case NotationPackage.EXPANDABLE_NODE__HAS_CHILDREN:
				setHasChildren((Boolean)newValue);
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
			case NotationPackage.EXPANDABLE_NODE__EXPANDED:
				setExpanded(EXPANDED_EDEFAULT);
				return;
			case NotationPackage.EXPANDABLE_NODE__HAS_CHILDREN:
				setHasChildren(HAS_CHILDREN_EDEFAULT);
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
			case NotationPackage.EXPANDABLE_NODE__EXPANDED:
				return expanded != EXPANDED_EDEFAULT;
			case NotationPackage.EXPANDABLE_NODE__HAS_CHILDREN:
				return hasChildren != HAS_CHILDREN_EDEFAULT;
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
		result.append(')');
		return result.toString();
	}

} //ExpandableNodeImpl
