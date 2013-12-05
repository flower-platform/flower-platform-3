/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.regex;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parser Regex</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.regex.ParserRegex#getAction <em>Action</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.regex.RegexPackage#getParserRegex()
 * @model
 * @generated
 */
public interface ParserRegex extends MacroRegex {
	/**
	 * Returns the value of the '<em><b>Action</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Action</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Action</em>' attribute.
	 * @see #setAction(String)
	 * @see org.flowerplatform.emf_model.regex.RegexPackage#getParserRegex_Action()
	 * @model
	 * @generated
	 */
	String getAction();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.regex.ParserRegex#getAction <em>Action</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Action</em>' attribute.
	 * @see #getAction()
	 * @generated
	 */
	void setAction(String value);

} // ParserRegex
