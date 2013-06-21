/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.notation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>View</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.notation.View#getViewType <em>View Type</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.View#getPersistentChildren <em>Persistent Children</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.View#getViewDetails <em>View Details</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.notation.View#getDiagrammableElement <em>Diagrammable Element</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.notation.NotationPackage#getView()
 * @model abstract="true"
 * @generated
 */
public interface View extends NotationElement {
	/**
	 * Returns the value of the '<em><b>View Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>View Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>View Type</em>' attribute.
	 * @see #setViewType(String)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getView_ViewType()
	 * @model required="true"
	 * @generated
	 */
	String getViewType();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.View#getViewType <em>View Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>View Type</em>' attribute.
	 * @see #getViewType()
	 * @generated
	 */
	void setViewType(String value);

	/**
	 * Returns the value of the '<em><b>Persistent Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.emf_model.notation.Node}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Persistent Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Persistent Children</em>' containment reference list.
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getView_PersistentChildren()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node> getPersistentChildren();

	/**
	 * Returns the value of the '<em><b>View Details</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>View Details</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>View Details</em>' attribute.
	 * @see #setViewDetails(Object)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getView_ViewDetails()
	 * @model transient="true"
	 * @generated
	 */
	Object getViewDetails();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.View#getViewDetails <em>View Details</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>View Details</em>' attribute.
	 * @see #getViewDetails()
	 * @generated
	 */
	void setViewDetails(Object value);

	/**
	 * Returns the value of the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagrammable Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagrammable Element</em>' reference.
	 * @see #setDiagrammableElement(EObject)
	 * @see org.flowerplatform.emf_model.notation.NotationPackage#getView_DiagrammableElement()
	 * @model
	 * @generated
	 */
	EObject getDiagrammableElement();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.notation.View#getDiagrammableElement <em>Diagrammable Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagrammable Element</em>' reference.
	 * @see #getDiagrammableElement()
	 * @generated
	 */
	void setDiagrammableElement(EObject value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	EList<Node> getAllChildren();

} // View
