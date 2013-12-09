/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.regex;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Macro Regex</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.regex.MacroRegex#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.regex.MacroRegex#getRegex <em>Regex</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.regex.RegexPackage#getMacroRegex()
 * @model
 * @generated
 */
public interface MacroRegex extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.flowerplatform.emf_model.regex.RegexPackage#getMacroRegex_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.regex.MacroRegex#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Regex</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Regex</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Regex</em>' attribute.
	 * @see #setRegex(String)
	 * @see org.flowerplatform.emf_model.regex.RegexPackage#getMacroRegex_Regex()
	 * @model
	 * @generated
	 */
	String getRegex();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.regex.MacroRegex#getRegex <em>Regex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Regex</em>' attribute.
	 * @see #getRegex()
	 * @generated
	 */
	void setRegex(String value);

} // MacroRegex
