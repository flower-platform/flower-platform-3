/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao2.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.Node1#getId <em>Id</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.Node1#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.Node1#getDiagrammableElement <em>Diagrammable Element</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.Node1#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getNode1()
 * @model
 * @generated
 */
public interface Node1 extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getNode1_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao2.model.Node1#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

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
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getNode1_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao2.model.Node1#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getNode1_DiagrammableElement()
	 * @model resolveProxies="false"
	 * @generated
	 */
	CodeSyncElement1 getDiagrammableElement();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao2.model.Node1#getDiagrammableElement <em>Diagrammable Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Diagrammable Element</em>' reference.
	 * @see #getDiagrammableElement()
	 * @generated
	 */
	void setDiagrammableElement(CodeSyncElement1 value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.model_access_dao2.model.Node1}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getNode1_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<Node1> getChildren();

} // Node1
