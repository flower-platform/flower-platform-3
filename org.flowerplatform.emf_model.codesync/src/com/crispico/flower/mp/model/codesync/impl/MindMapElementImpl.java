/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.codesync.impl;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.MindMapElement;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mind Map Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapElementImpl#getIcons <em>Icons</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapElementImpl#getMinWidth <em>Min Width</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapElementImpl#getMaxWidth <em>Max Width</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapElementImpl#getSide <em>Side</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapElementImpl#isExpanded <em>Expanded</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MindMapElementImpl extends CodeSyncElementImpl implements MindMapElement {
	/**
	 * The cached value of the '{@link #getIcons() <em>Icons</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIcons()
	 * @generated
	 * @ordered
	 */
	protected EList<String> icons;

	/**
	 * The default value of the '{@link #getMinWidth() <em>Min Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinWidth()
	 * @generated
	 * @ordered
	 */
	protected static final Long MIN_WIDTH_EDEFAULT = new Long(1L);
	/**
	 * The cached value of the '{@link #getMinWidth() <em>Min Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinWidth()
	 * @generated
	 * @ordered
	 */
	protected Long minWidth = MIN_WIDTH_EDEFAULT;
	/**
	 * The default value of the '{@link #getMaxWidth() <em>Max Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxWidth()
	 * @generated
	 * @ordered
	 */
	protected static final Long MAX_WIDTH_EDEFAULT = new Long(600L);
	/**
	 * The cached value of the '{@link #getMaxWidth() <em>Max Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxWidth()
	 * @generated
	 * @ordered
	 */
	protected Long maxWidth = MAX_WIDTH_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MindMapElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CodeSyncPackage.Literals.MIND_MAP_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getIcons() {
		if (icons == null) {
			icons = new EDataTypeEList<String>(String.class, this, CodeSyncPackage.MIND_MAP_ELEMENT__ICONS);
		}
		return icons;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Long getMinWidth() {
		return minWidth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinWidth(Long newMinWidth) {
		Long oldMinWidth = minWidth;
		minWidth = newMinWidth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.MIND_MAP_ELEMENT__MIN_WIDTH, oldMinWidth, minWidth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Long getMaxWidth() {
		return maxWidth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxWidth(Long newMaxWidth) {
		Long oldMaxWidth = maxWidth;
		maxWidth = newMaxWidth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.MIND_MAP_ELEMENT__MAX_WIDTH, oldMaxWidth, maxWidth));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.MIND_MAP_ELEMENT__SIDE, oldSide, side));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.MIND_MAP_ELEMENT__EXPANDED, oldExpanded, expanded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CodeSyncPackage.MIND_MAP_ELEMENT__ICONS:
				return getIcons();
			case CodeSyncPackage.MIND_MAP_ELEMENT__MIN_WIDTH:
				return getMinWidth();
			case CodeSyncPackage.MIND_MAP_ELEMENT__MAX_WIDTH:
				return getMaxWidth();
			case CodeSyncPackage.MIND_MAP_ELEMENT__SIDE:
				return getSide();
			case CodeSyncPackage.MIND_MAP_ELEMENT__EXPANDED:
				return isExpanded();
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
			case CodeSyncPackage.MIND_MAP_ELEMENT__ICONS:
				getIcons().clear();
				getIcons().addAll((Collection<? extends String>)newValue);
				return;
			case CodeSyncPackage.MIND_MAP_ELEMENT__MIN_WIDTH:
				setMinWidth((Long)newValue);
				return;
			case CodeSyncPackage.MIND_MAP_ELEMENT__MAX_WIDTH:
				setMaxWidth((Long)newValue);
				return;
			case CodeSyncPackage.MIND_MAP_ELEMENT__SIDE:
				setSide((Integer)newValue);
				return;
			case CodeSyncPackage.MIND_MAP_ELEMENT__EXPANDED:
				setExpanded((Boolean)newValue);
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
			case CodeSyncPackage.MIND_MAP_ELEMENT__ICONS:
				getIcons().clear();
				return;
			case CodeSyncPackage.MIND_MAP_ELEMENT__MIN_WIDTH:
				setMinWidth(MIN_WIDTH_EDEFAULT);
				return;
			case CodeSyncPackage.MIND_MAP_ELEMENT__MAX_WIDTH:
				setMaxWidth(MAX_WIDTH_EDEFAULT);
				return;
			case CodeSyncPackage.MIND_MAP_ELEMENT__SIDE:
				setSide(SIDE_EDEFAULT);
				return;
			case CodeSyncPackage.MIND_MAP_ELEMENT__EXPANDED:
				setExpanded(EXPANDED_EDEFAULT);
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
			case CodeSyncPackage.MIND_MAP_ELEMENT__ICONS:
				return icons != null && !icons.isEmpty();
			case CodeSyncPackage.MIND_MAP_ELEMENT__MIN_WIDTH:
				return MIN_WIDTH_EDEFAULT == null ? minWidth != null : !MIN_WIDTH_EDEFAULT.equals(minWidth);
			case CodeSyncPackage.MIND_MAP_ELEMENT__MAX_WIDTH:
				return MAX_WIDTH_EDEFAULT == null ? maxWidth != null : !MAX_WIDTH_EDEFAULT.equals(maxWidth);
			case CodeSyncPackage.MIND_MAP_ELEMENT__SIDE:
				return side != SIDE_EDEFAULT;
			case CodeSyncPackage.MIND_MAP_ELEMENT__EXPANDED:
				return expanded != EXPANDED_EDEFAULT;
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
		result.append(" (icons: ");
		result.append(icons);
		result.append(", minWidth: ");
		result.append(minWidth);
		result.append(", maxWidth: ");
		result.append(maxWidth);
		result.append(", side: ");
		result.append(side);
		result.append(", expanded: ");
		result.append(expanded);
		result.append(')');
		return result.toString();
	}

} //MindMapElementImpl
