/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.notation;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mind Map Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.MindMapNode#isExpanded <em>Expanded</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.MindMapNode#isHasChildren <em>Has Children</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.MindMapNode#getSide <em>Side</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.notation.NotationPackage#getMindMapNode()
 * @model
 * @generated
 */
public interface MindMapNode extends Node {
	/**
	 * Returns the value of the '<em><b>Expanded</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Expanded</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expanded</em>' attribute.
	 * @see #setExpanded(boolean)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getMindMapNode_Expanded()
	 * @model
	 * @generated
	 */
	boolean isExpanded();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.MindMapNode#isExpanded <em>Expanded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expanded</em>' attribute.
	 * @see #isExpanded()
	 * @generated
	 */
	void setExpanded(boolean value);

	/**
	 * Returns the value of the '<em><b>Has Children</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Has Children</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Has Children</em>' attribute.
	 * @see #setHasChildren(boolean)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getMindMapNode_HasChildren()
	 * @model
	 * @generated
	 */
	boolean isHasChildren();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.MindMapNode#isHasChildren <em>Has Children</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Has Children</em>' attribute.
	 * @see #isHasChildren()
	 * @generated
	 */
	void setHasChildren(boolean value);

	/**
	 * Returns the value of the '<em><b>Side</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Side</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Side</em>' attribute.
	 * @see #setSide(int)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getMindMapNode_Side()
	 * @model
	 * @generated
	 */
	int getSide();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.MindMapNode#getSide <em>Side</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Side</em>' attribute.
	 * @see #getSide()
	 * @generated
	 */
	void setSide(int value);

} // MindMapNode
