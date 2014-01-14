/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.regex.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.flowerplatform.emf_model.regex.MacroRegex;
import org.flowerplatform.emf_model.regex.ParserRegex;
import org.flowerplatform.emf_model.regex.RegexPackage;
import org.flowerplatform.emf_model.regex.Root;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.regex.impl.RootImpl#getParserRegexes <em>Parser Regexes</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.regex.impl.RootImpl#getMacroRegexes <em>Macro Regexes</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.regex.impl.RootImpl#getExtensions <em>Extensions</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RootImpl extends EObjectImpl implements Root {
	/**
	 * The cached value of the '{@link #getParserRegexes() <em>Parser Regexes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParserRegexes()
	 * @generated
	 * @ordered
	 */
	protected EList<ParserRegex> parserRegexes;

	/**
	 * The cached value of the '{@link #getMacroRegexes() <em>Macro Regexes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMacroRegexes()
	 * @generated
	 * @ordered
	 */
	protected EList<MacroRegex> macroRegexes;

	/**
	 * The cached value of the '{@link #getExtensions() <em>Extensions</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtensions()
	 * @generated
	 * @ordered
	 */
	protected EList<String> extensions;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RegexPackage.Literals.ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ParserRegex> getParserRegexes() {
		if (parserRegexes == null) {
			parserRegexes = new EObjectContainmentEList<ParserRegex>(ParserRegex.class, this, RegexPackage.ROOT__PARSER_REGEXES);
		}
		return parserRegexes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MacroRegex> getMacroRegexes() {
		if (macroRegexes == null) {
			macroRegexes = new EObjectContainmentEList<MacroRegex>(MacroRegex.class, this, RegexPackage.ROOT__MACRO_REGEXES);
		}
		return macroRegexes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getExtensions() {
		if (extensions == null) {
			extensions = new EDataTypeUniqueEList<String>(String.class, this, RegexPackage.ROOT__EXTENSIONS);
		}
		return extensions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RegexPackage.ROOT__PARSER_REGEXES:
				return ((InternalEList<?>)getParserRegexes()).basicRemove(otherEnd, msgs);
			case RegexPackage.ROOT__MACRO_REGEXES:
				return ((InternalEList<?>)getMacroRegexes()).basicRemove(otherEnd, msgs);
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
			case RegexPackage.ROOT__PARSER_REGEXES:
				return getParserRegexes();
			case RegexPackage.ROOT__MACRO_REGEXES:
				return getMacroRegexes();
			case RegexPackage.ROOT__EXTENSIONS:
				return getExtensions();
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
			case RegexPackage.ROOT__PARSER_REGEXES:
				getParserRegexes().clear();
				getParserRegexes().addAll((Collection<? extends ParserRegex>)newValue);
				return;
			case RegexPackage.ROOT__MACRO_REGEXES:
				getMacroRegexes().clear();
				getMacroRegexes().addAll((Collection<? extends MacroRegex>)newValue);
				return;
			case RegexPackage.ROOT__EXTENSIONS:
				getExtensions().clear();
				getExtensions().addAll((Collection<? extends String>)newValue);
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
			case RegexPackage.ROOT__PARSER_REGEXES:
				getParserRegexes().clear();
				return;
			case RegexPackage.ROOT__MACRO_REGEXES:
				getMacroRegexes().clear();
				return;
			case RegexPackage.ROOT__EXTENSIONS:
				getExtensions().clear();
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
			case RegexPackage.ROOT__PARSER_REGEXES:
				return parserRegexes != null && !parserRegexes.isEmpty();
			case RegexPackage.ROOT__MACRO_REGEXES:
				return macroRegexes != null && !macroRegexes.isEmpty();
			case RegexPackage.ROOT__EXTENSIONS:
				return extensions != null && !extensions.isEmpty();
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
		result.append(" (extensions: ");
		result.append(extensions);
		result.append(')');
		return result.toString();
	}

} //RootImpl
