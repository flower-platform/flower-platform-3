/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getChildren <em>Children</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getKeyParameter <em>Key Parameter</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getType <em>Type</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getOffset <em>Offset</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getLength <em>Length</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isAdded <em>Added</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isDeleted <em>Deleted</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getChildrenInsertPoints <em>Children Insert Points</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getNextSiblingInsertPoint <em>Next Sibling Insert Point</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode()
 * @model
 * @generated
 */
public interface RegExAstNode extends EObject {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<RegExAstNode> getChildren();

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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_Parameters()
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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_KeyParameter()
	 * @model
	 * @generated
	 */
	String getKeyParameter();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getKeyParameter <em>Key Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Parameter</em>' attribute.
	 * @see #getKeyParameter()
	 * @generated
	 */
	void setKeyParameter(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Offset</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Offset</em>' attribute.
	 * @see #setOffset(int)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_Offset()
	 * @model
	 * @generated
	 */
	int getOffset();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getOffset <em>Offset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Offset</em>' attribute.
	 * @see #getOffset()
	 * @generated
	 */
	void setOffset(int value);

	/**
	 * Returns the value of the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Length</em>' attribute.
	 * @see #setLength(int)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_Length()
	 * @model
	 * @generated
	 */
	int getLength();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getLength <em>Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Length</em>' attribute.
	 * @see #getLength()
	 * @generated
	 */
	void setLength(int value);

	/**
	 * Returns the value of the '<em><b>Added</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Added</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Added</em>' attribute.
	 * @see #setAdded(boolean)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_Added()
	 * @model
	 * @generated
	 */
	boolean isAdded();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isAdded <em>Added</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Added</em>' attribute.
	 * @see #isAdded()
	 * @generated
	 */
	void setAdded(boolean value);

	/**
	 * Returns the value of the '<em><b>Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deleted</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Deleted</em>' attribute.
	 * @see #setDeleted(boolean)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_Deleted()
	 * @model
	 * @generated
	 */
	boolean isDeleted();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isDeleted <em>Deleted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Deleted</em>' attribute.
	 * @see #isDeleted()
	 * @generated
	 */
	void setDeleted(boolean value);

	/**
	 * Returns the value of the '<em><b>Children Insert Points</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link java.lang.Integer},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children Insert Points</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children Insert Points</em>' map.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_ChildrenInsertPoints()
	 * @model mapType="org.flowerplatform.codesync.code.javascript.regex_ast.StringToIntegerEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EIntegerObject>"
	 * @generated
	 */
	EMap<String, Integer> getChildrenInsertPoints();

	/**
	 * Returns the value of the '<em><b>Next Sibling Insert Point</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next Sibling Insert Point</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next Sibling Insert Point</em>' attribute.
	 * @see #setNextSiblingInsertPoint(int)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getRegExAstNode_NextSiblingInsertPoint()
	 * @model
	 * @generated
	 */
	int getNextSiblingInsertPoint();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getNextSiblingInsertPoint <em>Next Sibling Insert Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Sibling Insert Point</em>' attribute.
	 * @see #getNextSiblingInsertPoint()
	 * @generated
	 */
	void setNextSiblingInsertPoint(int value);

} // RegExAstNode
