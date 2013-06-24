/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.notation;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.Node#getLayoutConstraint <em>Layout Constraint</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.notation.NotationPackage#getNode()
 * @model
 * @generated
 */
public interface Node extends View {
	/**
	 * Returns the value of the '<em><b>Layout Constraint</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Layout Constraint</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Layout Constraint</em>' containment reference.
	 * @see #setLayoutConstraint(Bounds)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getNode_LayoutConstraint()
	 * @model containment="true"
	 * @generated
	 */
	Bounds getLayoutConstraint();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.Node#getLayoutConstraint <em>Layout Constraint</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Layout Constraint</em>' containment reference.
	 * @see #getLayoutConstraint()
	 * @generated
	 */
	void setLayoutConstraint(Bounds value);

} // Node
