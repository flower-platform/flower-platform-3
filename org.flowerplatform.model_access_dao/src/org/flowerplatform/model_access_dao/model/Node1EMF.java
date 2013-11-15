/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao.model;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node1 EMF</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.model_access_dao.model.Node1EMF#getDiagrammableElement <em>Diagrammable Element</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao.model.Node1EMF#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getNode1EMF()
 * @model
 * @generated
 */
public interface Node1EMF extends Node1 {
	/**
	 * Returns the value of the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Diagrammable Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Diagrammable Element</em>' reference.
	 * @see #setDiagrammableElement(CodeSyncElement1)
	 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getNode1EMF_DiagrammableElement()
	 * @model resolveProxies="false"
	 * @generated
	 */
	CodeSyncElement1 getDiagrammableElement();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao.model.Node1EMF#getDiagrammableElement <em>Diagrammable Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagrammable Element</em>' reference.
	 * @see #getDiagrammableElement()
	 * @generated
	 */
	void setDiagrammableElement(CodeSyncElement1 value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.model_access_dao.model.Node1}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.flowerplatform.model_access_dao.model.ModelPackage#getNode1EMF_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node1> getChildren();

} // Node1EMF
