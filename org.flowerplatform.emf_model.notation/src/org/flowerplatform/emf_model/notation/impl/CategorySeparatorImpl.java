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

import org.flowerplatform.emf_model.notation.CategorySeparator;
import org.flowerplatform.emf_model.notation.NotationPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Category Separator</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.CategorySeparatorImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.CategorySeparatorImpl#getNewChildCodeSyncType <em>New Child Code Sync Type</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.impl.CategorySeparatorImpl#getNewChildIcon <em>New Child Icon</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CategorySeparatorImpl extends NodeImpl implements CategorySeparator {
	/**
	 * The default value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected static final String CATEGORY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected String category = CATEGORY_EDEFAULT;

	/**
	 * The default value of the '{@link #getNewChildCodeSyncType() <em>New Child Code Sync Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewChildCodeSyncType()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_CHILD_CODE_SYNC_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewChildCodeSyncType() <em>New Child Code Sync Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewChildCodeSyncType()
	 * @generated
	 * @ordered
	 */
	protected String newChildCodeSyncType = NEW_CHILD_CODE_SYNC_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getNewChildIcon() <em>New Child Icon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewChildIcon()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_CHILD_ICON_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewChildIcon() <em>New Child Icon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewChildIcon()
	 * @generated
	 * @ordered
	 */
	protected String newChildIcon = NEW_CHILD_ICON_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CategorySeparatorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return NotationPackage.Literals.CATEGORY_SEPARATOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCategory(String newCategory) {
		String oldCategory = category;
		category = newCategory;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.CATEGORY_SEPARATOR__CATEGORY, oldCategory, category));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNewChildCodeSyncType() {
		return newChildCodeSyncType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewChildCodeSyncType(String newNewChildCodeSyncType) {
		String oldNewChildCodeSyncType = newChildCodeSyncType;
		newChildCodeSyncType = newNewChildCodeSyncType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_CODE_SYNC_TYPE, oldNewChildCodeSyncType, newChildCodeSyncType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNewChildIcon() {
		return newChildIcon;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewChildIcon(String newNewChildIcon) {
		String oldNewChildIcon = newChildIcon;
		newChildIcon = newNewChildIcon;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_ICON, oldNewChildIcon, newChildIcon));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case NotationPackage.CATEGORY_SEPARATOR__CATEGORY:
				return getCategory();
			case NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_CODE_SYNC_TYPE:
				return getNewChildCodeSyncType();
			case NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_ICON:
				return getNewChildIcon();
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
			case NotationPackage.CATEGORY_SEPARATOR__CATEGORY:
				setCategory((String)newValue);
				return;
			case NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_CODE_SYNC_TYPE:
				setNewChildCodeSyncType((String)newValue);
				return;
			case NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_ICON:
				setNewChildIcon((String)newValue);
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
			case NotationPackage.CATEGORY_SEPARATOR__CATEGORY:
				setCategory(CATEGORY_EDEFAULT);
				return;
			case NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_CODE_SYNC_TYPE:
				setNewChildCodeSyncType(NEW_CHILD_CODE_SYNC_TYPE_EDEFAULT);
				return;
			case NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_ICON:
				setNewChildIcon(NEW_CHILD_ICON_EDEFAULT);
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
			case NotationPackage.CATEGORY_SEPARATOR__CATEGORY:
				return CATEGORY_EDEFAULT == null ? category != null : !CATEGORY_EDEFAULT.equals(category);
			case NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_CODE_SYNC_TYPE:
				return NEW_CHILD_CODE_SYNC_TYPE_EDEFAULT == null ? newChildCodeSyncType != null : !NEW_CHILD_CODE_SYNC_TYPE_EDEFAULT.equals(newChildCodeSyncType);
			case NotationPackage.CATEGORY_SEPARATOR__NEW_CHILD_ICON:
				return NEW_CHILD_ICON_EDEFAULT == null ? newChildIcon != null : !NEW_CHILD_ICON_EDEFAULT.equals(newChildIcon);
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
		result.append(" (category: ");
		result.append(category);
		result.append(", newChildCodeSyncType: ");
		result.append(newChildCodeSyncType);
		result.append(", newChildIcon: ");
		result.append(newChildIcon);
		result.append(')');
		return result.toString();
	}

} //CategorySeparatorImpl
