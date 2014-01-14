/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.regex;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.regex.Root#getParserRegexes <em>Parser Regexes</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.regex.Root#getMacroRegexes <em>Macro Regexes</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.regex.Root#getExtensions <em>Extensions</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.regex.RegexPackage#getRoot()
 * @model
 * @generated
 */
public interface Root extends EObject {
	/**
	 * Returns the value of the '<em><b>Parser Regexes</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.emf_model.regex.ParserRegex}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parser Regexes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parser Regexes</em>' containment reference list.
	 * @see org.flowerplatform.emf_model.regex.RegexPackage#getRoot_ParserRegexes()
	 * @model containment="true"
	 * @generated
	 */
	EList<ParserRegex> getParserRegexes();

	/**
	 * Returns the value of the '<em><b>Macro Regexes</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.emf_model.regex.MacroRegex}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Macro Regexes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Macro Regexes</em>' containment reference list.
	 * @see org.flowerplatform.emf_model.regex.RegexPackage#getRoot_MacroRegexes()
	 * @model containment="true"
	 * @generated
	 */
	EList<MacroRegex> getMacroRegexes();

	/**
	 * Returns the value of the '<em><b>Extensions</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extensions</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extensions</em>' attribute list.
	 * @see org.flowerplatform.emf_model.regex.RegexPackage#getRoot_Extensions()
	 * @model
	 * @generated
	 */
	EList<String> getExtensions();

} // Root
