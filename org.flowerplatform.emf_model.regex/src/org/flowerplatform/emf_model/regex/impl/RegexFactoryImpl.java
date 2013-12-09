/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.regex.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.flowerplatform.emf_model.regex.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RegexFactoryImpl extends EFactoryImpl implements RegexFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static RegexFactory init() {
		try {
			RegexFactory theRegexFactory = (RegexFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.flower-platform.com/xmi/regex_1.0.0"); 
			if (theRegexFactory != null) {
				return theRegexFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new RegexFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegexFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case RegexPackage.PARSER_REGEX: return createParserRegex();
			case RegexPackage.MACRO_REGEX: return createMacroRegex();
			case RegexPackage.ROOT: return createRoot();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParserRegex createParserRegex() {
		ParserRegexImpl parserRegex = new ParserRegexImpl();
		return parserRegex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MacroRegex createMacroRegex() {
		MacroRegexImpl macroRegex = new MacroRegexImpl();
		return macroRegex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Root createRoot() {
		RootImpl root = new RootImpl();
		return root;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegexPackage getRegexPackage() {
		return (RegexPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static RegexPackage getPackage() {
		return RegexPackage.eINSTANCE;
	}

} //RegexFactoryImpl
