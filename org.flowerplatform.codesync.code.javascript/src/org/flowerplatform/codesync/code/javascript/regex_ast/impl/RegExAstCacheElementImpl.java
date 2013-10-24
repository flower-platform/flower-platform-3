/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast.impl;

import com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cache Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCacheElementImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCacheElementImpl#getKeyParameter <em>Key Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RegExAstCacheElementImpl extends AstCacheElementImpl implements RegExAstCacheElement {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<RegExAstNodeParameter> parameters;

	/**
	 * The default value of the '{@link #getKeyParameter() <em>Key Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyParameter()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_PARAMETER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKeyParameter() <em>Key Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKeyParameter()
	 * @generated
	 * @ordered
	 */
	protected String keyParameter = KEY_PARAMETER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RegExAstCacheElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RegExAstPackage.Literals.REG_EX_AST_CACHE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RegExAstNodeParameter> getParameters() {
		if (parameters == null) {
			parameters = new EObjectContainmentEList<RegExAstNodeParameter>(RegExAstNodeParameter.class, this, RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKeyParameter() {
		return keyParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKeyParameter(String newKeyParameter) {
		String oldKeyParameter = keyParameter;
		keyParameter = newKeyParameter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__KEY_PARAMETER, oldKeyParameter, keyParameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__PARAMETERS:
				return ((InternalEList<?>)getParameters()).basicRemove(otherEnd, msgs);
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
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__PARAMETERS:
				return getParameters();
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__KEY_PARAMETER:
				return getKeyParameter();
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
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__PARAMETERS:
				getParameters().clear();
				getParameters().addAll((Collection<? extends RegExAstNodeParameter>)newValue);
				return;
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__KEY_PARAMETER:
				setKeyParameter((String)newValue);
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
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__PARAMETERS:
				getParameters().clear();
				return;
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__KEY_PARAMETER:
				setKeyParameter(KEY_PARAMETER_EDEFAULT);
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
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__PARAMETERS:
				return parameters != null && !parameters.isEmpty();
			case RegExAstPackage.REG_EX_AST_CACHE_ELEMENT__KEY_PARAMETER:
				return KEY_PARAMETER_EDEFAULT == null ? keyParameter != null : !KEY_PARAMETER_EDEFAULT.equals(keyParameter);
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
		result.append(" (keyParameter: ");
		result.append(keyParameter);
		result.append(')');
		return result.toString();
	}

} //RegExAstCacheElementImpl
