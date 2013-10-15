/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.codesync.impl;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import com.crispico.flower.mp.model.codesync.MindMapElement;
import com.crispico.flower.mp.model.codesync.MindMapRoot;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mind Map Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapRootImpl#getIcons <em>Icons</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapRootImpl#getMinWidth <em>Min Width</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapRootImpl#getMaxWidth <em>Max Width</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapRootImpl#getSide <em>Side</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.impl.MindMapRootImpl#isExpanded <em>Expanded</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MindMapRootImpl extends CodeSyncRootImpl implements MindMapRoot {
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
	protected MindMapRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CodeSyncPackage.Literals.MIND_MAP_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getIcons() {
		if (icons == null) {
			icons = new EDataTypeEList<String>(String.class, this, CodeSyncPackage.MIND_MAP_ROOT__ICONS);
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
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.MIND_MAP_ROOT__MIN_WIDTH, oldMinWidth, minWidth));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.MIND_MAP_ROOT__MAX_WIDTH, oldMaxWidth, maxWidth));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.MIND_MAP_ROOT__SIDE, oldSide, side));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CodeSyncPackage.MIND_MAP_ROOT__EXPANDED, oldExpanded, expanded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CodeSyncPackage.MIND_MAP_ROOT__ICONS:
				return getIcons();
			case CodeSyncPackage.MIND_MAP_ROOT__MIN_WIDTH:
				return getMinWidth();
			case CodeSyncPackage.MIND_MAP_ROOT__MAX_WIDTH:
				return getMaxWidth();
			case CodeSyncPackage.MIND_MAP_ROOT__SIDE:
				return getSide();
			case CodeSyncPackage.MIND_MAP_ROOT__EXPANDED:
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
			case CodeSyncPackage.MIND_MAP_ROOT__ICONS:
				getIcons().clear();
				getIcons().addAll((Collection<? extends String>)newValue);
				return;
			case CodeSyncPackage.MIND_MAP_ROOT__MIN_WIDTH:
				setMinWidth((Long)newValue);
				return;
			case CodeSyncPackage.MIND_MAP_ROOT__MAX_WIDTH:
				setMaxWidth((Long)newValue);
				return;
			case CodeSyncPackage.MIND_MAP_ROOT__SIDE:
				setSide((Integer)newValue);
				return;
			case CodeSyncPackage.MIND_MAP_ROOT__EXPANDED:
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
			case CodeSyncPackage.MIND_MAP_ROOT__ICONS:
				getIcons().clear();
				return;
			case CodeSyncPackage.MIND_MAP_ROOT__MIN_WIDTH:
				setMinWidth(MIN_WIDTH_EDEFAULT);
				return;
			case CodeSyncPackage.MIND_MAP_ROOT__MAX_WIDTH:
				setMaxWidth(MAX_WIDTH_EDEFAULT);
				return;
			case CodeSyncPackage.MIND_MAP_ROOT__SIDE:
				setSide(SIDE_EDEFAULT);
				return;
			case CodeSyncPackage.MIND_MAP_ROOT__EXPANDED:
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
			case CodeSyncPackage.MIND_MAP_ROOT__ICONS:
				return icons != null && !icons.isEmpty();
			case CodeSyncPackage.MIND_MAP_ROOT__MIN_WIDTH:
				return MIN_WIDTH_EDEFAULT == null ? minWidth != null : !MIN_WIDTH_EDEFAULT.equals(minWidth);
			case CodeSyncPackage.MIND_MAP_ROOT__MAX_WIDTH:
				return MAX_WIDTH_EDEFAULT == null ? maxWidth != null : !MAX_WIDTH_EDEFAULT.equals(maxWidth);
			case CodeSyncPackage.MIND_MAP_ROOT__SIDE:
				return side != SIDE_EDEFAULT;
			case CodeSyncPackage.MIND_MAP_ROOT__EXPANDED:
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
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == MindMapElement.class) {
			switch (derivedFeatureID) {
				case CodeSyncPackage.MIND_MAP_ROOT__ICONS: return CodeSyncPackage.MIND_MAP_ELEMENT__ICONS;
				case CodeSyncPackage.MIND_MAP_ROOT__MIN_WIDTH: return CodeSyncPackage.MIND_MAP_ELEMENT__MIN_WIDTH;
				case CodeSyncPackage.MIND_MAP_ROOT__MAX_WIDTH: return CodeSyncPackage.MIND_MAP_ELEMENT__MAX_WIDTH;
				case CodeSyncPackage.MIND_MAP_ROOT__SIDE: return CodeSyncPackage.MIND_MAP_ELEMENT__SIDE;
				case CodeSyncPackage.MIND_MAP_ROOT__EXPANDED: return CodeSyncPackage.MIND_MAP_ELEMENT__EXPANDED;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == MindMapElement.class) {
			switch (baseFeatureID) {
				case CodeSyncPackage.MIND_MAP_ELEMENT__ICONS: return CodeSyncPackage.MIND_MAP_ROOT__ICONS;
				case CodeSyncPackage.MIND_MAP_ELEMENT__MIN_WIDTH: return CodeSyncPackage.MIND_MAP_ROOT__MIN_WIDTH;
				case CodeSyncPackage.MIND_MAP_ELEMENT__MAX_WIDTH: return CodeSyncPackage.MIND_MAP_ROOT__MAX_WIDTH;
				case CodeSyncPackage.MIND_MAP_ELEMENT__SIDE: return CodeSyncPackage.MIND_MAP_ROOT__SIDE;
				case CodeSyncPackage.MIND_MAP_ELEMENT__EXPANDED: return CodeSyncPackage.MIND_MAP_ROOT__EXPANDED;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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

} //MindMapRootImpl
