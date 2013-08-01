/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model.astcache.wiki.impl;

import com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage;
import org.flowerplatform.model.astcache.wiki.Heading;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Heading</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.model.astcache.wiki.impl.HeadingImpl#getOriginalFormat <em>Original Format</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class HeadingImpl extends CodeSyncElementImpl implements Heading {
	/**
	 * The default value of the '{@link #getOriginalFormat() <em>Original Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalFormat()
	 * @generated
	 * @ordered
	 */
	protected static final String ORIGINAL_FORMAT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginalFormat() <em>Original Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginalFormat()
	 * @generated
	 * @ordered
	 */
	protected String originalFormat = ORIGINAL_FORMAT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HeadingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AstCacheWikiPackage.Literals.HEADING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOriginalFormat() {
		return originalFormat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginalFormat(String newOriginalFormat) {
		String oldOriginalFormat = originalFormat;
		originalFormat = newOriginalFormat;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AstCacheWikiPackage.HEADING__ORIGINAL_FORMAT, oldOriginalFormat, originalFormat));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AstCacheWikiPackage.HEADING__ORIGINAL_FORMAT:
				return getOriginalFormat();
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
			case AstCacheWikiPackage.HEADING__ORIGINAL_FORMAT:
				setOriginalFormat((String)newValue);
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
			case AstCacheWikiPackage.HEADING__ORIGINAL_FORMAT:
				setOriginalFormat(ORIGINAL_FORMAT_EDEFAULT);
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
			case AstCacheWikiPackage.HEADING__ORIGINAL_FORMAT:
				return ORIGINAL_FORMAT_EDEFAULT == null ? originalFormat != null : !ORIGINAL_FORMAT_EDEFAULT.equals(originalFormat);
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
		result.append(" (originalFormat: ");
		result.append(originalFormat);
		result.append(')');
		return result.toString();
	}

} //HeadingImpl
