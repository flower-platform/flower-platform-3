/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast;

import com.crispico.flower.mp.model.codesync.AstCacheElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cache Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#getKeyParameter <em>Key Parameter</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstCacheElement()
 * @model
 * @generated
 */
public interface RegExAstCacheElement extends AstCacheElement {
	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstCacheElement_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<RegExAstNodeParameter> getParameters();

	/**
	 * Returns the value of the '<em><b>Key Parameter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Key Parameter</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Parameter</em>' attribute.
	 * @see #setKeyParameter(String)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstCacheElement_KeyParameter()
	 * @model
	 * @generated
	 */
	String getKeyParameter();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#getKeyParameter <em>Key Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Parameter</em>' attribute.
	 * @see #getKeyParameter()
	 * @generated
	 */
	void setKeyParameter(String value);

} // RegExAstCacheElement
