/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.notation;

import org.eclipse.emf.common.util.EList;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diagram</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.Diagram#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.Diagram#getPersistentEdges <em>Persistent Edges</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.notation.NotationPackage#getDiagram()
 * @model
 * @generated
 */
public interface Diagram extends View {
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
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getDiagram_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.Diagram#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Persistent Edges</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.emf_model.notation.Edge}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Persistent Edges</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Persistent Edges</em>' containment reference list.
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getDiagram_PersistentEdges()
	 * @model containment="true"
	 * @generated
	 */
	EList<Edge> getPersistentEdges();

} // Diagram
