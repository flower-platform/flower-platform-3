/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Code Sync Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement#getNextSiblingSeparator <em>Next Sibling Separator</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement#getChildType <em>Child Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstCodeSyncElement()
 * @model
 * @generated
 */
public interface RegExAstCodeSyncElement extends CodeSyncElement {
	/**
	 * Returns the value of the '<em><b>Next Sibling Separator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Sibling Separator</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Sibling Separator</em>' attribute.
	 * @see #setNextSiblingSeparator(String)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstCodeSyncElement_NextSiblingSeparator()
	 * @model
	 * @generated
	 */
	String getNextSiblingSeparator();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement#getNextSiblingSeparator <em>Next Sibling Separator</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Sibling Separator</em>' attribute.
	 * @see #getNextSiblingSeparator()
	 * @generated
	 */
	void setNextSiblingSeparator(String value);

	/**
	 * Returns the value of the '<em><b>Child Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Child Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Child Type</em>' attribute.
	 * @see #setChildType(String)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstCodeSyncElement_ChildType()
	 * @model
	 * @generated
	 */
	String getChildType();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement#getChildType <em>Child Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Child Type</em>' attribute.
	 * @see #getChildType()
	 * @generated
	 */
	void setChildType(String value);

} // RegExAstCodeSyncElement
