/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.regex;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.emf_model.regex.RegexPackage
 * @generated
 */
public interface RegexFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RegexFactory eINSTANCE = org.flowerplatform.emf_model.regex.impl.RegexFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Parser Regex</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Parser Regex</em>'.
	 * @generated
	 */
	ParserRegex createParserRegex();

	/**
	 * Returns a new object of class '<em>Macro Regex</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Macro Regex</em>'.
	 * @generated
	 */
	MacroRegex createMacroRegex();

	/**
	 * Returns a new object of class '<em>Root</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Root</em>'.
	 * @generated
	 */
	Root createRoot();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	RegexPackage getRegexPackage();

} //RegexFactory
