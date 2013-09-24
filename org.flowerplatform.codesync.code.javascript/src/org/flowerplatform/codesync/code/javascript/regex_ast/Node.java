/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getChildren <em>Children</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getKeyParameter <em>Key Parameter</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getType <em>Type</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#isCategoryNode <em>Category Node</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getOffset <em>Offset</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getLength <em>Length</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#isAdded <em>Added</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getTemplate <em>Template</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getChildrenInsertPoint <em>Children Insert Point</em>}</li>
 *   <li>{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getNextSiblingInsertPoint <em>Next Sibling Insert Point</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode()
 * @model
 * @generated
 */
public interface Node extends EObject {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.codesync.code.javascript.regex_ast.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getChildren();

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.codesync.code.javascript.regex_ast.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getParameters();

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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_KeyParameter()
	 * @model
	 * @generated
	 */
	String getKeyParameter();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getKeyParameter <em>Key Parameter</em>}' attribute.
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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Category Node</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category Node</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category Node</em>' attribute.
	 * @see #setCategoryNode(boolean)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_CategoryNode()
	 * @model
	 * @generated
	 */
	boolean isCategoryNode();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#isCategoryNode <em>Category Node</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category Node</em>' attribute.
	 * @see #isCategoryNode()
	 * @generated
	 */
	void setCategoryNode(boolean value);

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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_Offset()
	 * @model
	 * @generated
	 */
	int getOffset();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getOffset <em>Offset</em>}' attribute.
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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_Length()
	 * @model
	 * @generated
	 */
	int getLength();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getLength <em>Length</em>}' attribute.
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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_Added()
	 * @model
	 * @generated
	 */
	boolean isAdded();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#isAdded <em>Added</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Added</em>' attribute.
	 * @see #isAdded()
	 * @generated
	 */
	void setAdded(boolean value);

	/**
	 * Returns the value of the '<em><b>Template</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Template</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Template</em>' attribute.
	 * @see #setTemplate(String)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_Template()
	 * @model
	 * @generated
	 */
	String getTemplate();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getTemplate <em>Template</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Template</em>' attribute.
	 * @see #getTemplate()
	 * @generated
	 */
	void setTemplate(String value);

	/**
	 * Returns the value of the '<em><b>Children Insert Point</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children Insert Point</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children Insert Point</em>' attribute.
	 * @see #setChildrenInsertPoint(int)
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_ChildrenInsertPoint()
	 * @model
	 * @generated
	 */
	int getChildrenInsertPoint();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getChildrenInsertPoint <em>Children Insert Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Children Insert Point</em>' attribute.
	 * @see #getChildrenInsertPoint()
	 * @generated
	 */
	void setChildrenInsertPoint(int value);

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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#getNode_NextSiblingInsertPoint()
	 * @model
	 * @generated
	 */
	int getNextSiblingInsertPoint();

	/**
	 * Sets the value of the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getNextSiblingInsertPoint <em>Next Sibling Insert Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next Sibling Insert Point</em>' attribute.
	 * @see #getNextSiblingInsertPoint()
	 * @generated
	 */
	void setNextSiblingInsertPoint(int value);

} // Node
