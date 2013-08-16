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
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CodeSyncPackage.MIND_MAP_ROOT__ICONS:
				return getIcons();
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
		result.append(')');
		return result.toString();
	}

} //MindMapRootImpl
