/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast.impl;

import com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Code Sync Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCodeSyncElementImpl#getTemplate <em>Template</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCodeSyncElementImpl#getNextSiblingSeparator <em>Next Sibling Separator</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCodeSyncElementImpl#getChildType <em>Child Type</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RegExAstCodeSyncElementImpl extends CodeSyncElementImpl implements RegExAstCodeSyncElement {
	/**
	 * The default value of the '{@link #getTemplate() <em>Template</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemplate()
	 * @generated
	 * @ordered
	 */
	protected static final String TEMPLATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTemplate() <em>Template</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemplate()
	 * @generated
	 * @ordered
	 */
	protected String template = TEMPLATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getNextSiblingSeparator() <em>Next Sibling Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextSiblingSeparator()
	 * @generated
	 * @ordered
	 */
	protected static final String NEXT_SIBLING_SEPARATOR_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNextSiblingSeparator() <em>Next Sibling Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextSiblingSeparator()
	 * @generated
	 * @ordered
	 */
	protected String nextSiblingSeparator = NEXT_SIBLING_SEPARATOR_EDEFAULT;

	/**
	 * The default value of the '{@link #getChildType() <em>Child Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildType()
	 * @generated
	 * @ordered
	 */
	protected static final String CHILD_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getChildType() <em>Child Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildType()
	 * @generated
	 * @ordered
	 */
	protected String childType = CHILD_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RegExAstCodeSyncElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RegExAstPackage.Literals.REG_EX_AST_CODE_SYNC_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTemplate() {
		return template;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTemplate(String newTemplate) {
		String oldTemplate = template;
		template = newTemplate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__TEMPLATE, oldTemplate, template));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNextSiblingSeparator() {
		return nextSiblingSeparator;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNextSiblingSeparator(String newNextSiblingSeparator) {
		String oldNextSiblingSeparator = nextSiblingSeparator;
		nextSiblingSeparator = newNextSiblingSeparator;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__NEXT_SIBLING_SEPARATOR, oldNextSiblingSeparator, nextSiblingSeparator));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getChildType() {
		return childType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setChildType(String newChildType) {
		String oldChildType = childType;
		childType = newChildType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__CHILD_TYPE, oldChildType, childType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__TEMPLATE:
				return getTemplate();
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__NEXT_SIBLING_SEPARATOR:
				return getNextSiblingSeparator();
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__CHILD_TYPE:
				return getChildType();
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
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__TEMPLATE:
				setTemplate((String)newValue);
				return;
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__NEXT_SIBLING_SEPARATOR:
				setNextSiblingSeparator((String)newValue);
				return;
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__CHILD_TYPE:
				setChildType((String)newValue);
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
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__TEMPLATE:
				setTemplate(TEMPLATE_EDEFAULT);
				return;
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__NEXT_SIBLING_SEPARATOR:
				setNextSiblingSeparator(NEXT_SIBLING_SEPARATOR_EDEFAULT);
				return;
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__CHILD_TYPE:
				setChildType(CHILD_TYPE_EDEFAULT);
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
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__TEMPLATE:
				return TEMPLATE_EDEFAULT == null ? template != null : !TEMPLATE_EDEFAULT.equals(template);
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__NEXT_SIBLING_SEPARATOR:
				return NEXT_SIBLING_SEPARATOR_EDEFAULT == null ? nextSiblingSeparator != null : !NEXT_SIBLING_SEPARATOR_EDEFAULT.equals(nextSiblingSeparator);
			case RegExAstPackage.REG_EX_AST_CODE_SYNC_ELEMENT__CHILD_TYPE:
				return CHILD_TYPE_EDEFAULT == null ? childType != null : !CHILD_TYPE_EDEFAULT.equals(childType);
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
		result.append(" (template: ");
		result.append(template);
		result.append(", nextSiblingSeparator: ");
		result.append(nextSiblingSeparator);
		result.append(", childType: ");
		result.append(childType);
		result.append(')');
		return result.toString();
	}

} //RegExAstCodeSyncElementImpl
