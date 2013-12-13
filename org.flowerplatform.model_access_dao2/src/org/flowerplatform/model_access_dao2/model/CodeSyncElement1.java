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
 * A representation of the model object '<em><b>Code Sync Element1</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.CodeSyncElement1#getId <em>Id</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.CodeSyncElement1#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.CodeSyncElement1#getChildren <em>Children</em>}</li>
 *   <li>{@link org.flowerplatform.model_access_dao2.model.CodeSyncElement1#getRelations <em>Relations</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getCodeSyncElement1()
 * @model
 * @generated
 */
public interface CodeSyncElement1 extends EObject {
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
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getCodeSyncElement1_Id()
	 * @model id="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao2.model.CodeSyncElement1#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getCodeSyncElement1_Name()
	 * @model default=""
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model_access_dao2.model.CodeSyncElement1#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.model_access_dao2.model.CodeSyncElement1}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getCodeSyncElement1_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<CodeSyncElement1> getChildren();

	/**
	 * Returns the value of the '<em><b>Relations</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.model_access_dao2.model.Relation1}.
	 * It is bidirectional and its opposite is '{@link org.flowerplatform.model_access_dao2.model.Relation1#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relations</em>' containment reference list.
	 * @see org.flowerplatform.model_access_dao2.model.ModelPackage#getCodeSyncElement1_Relations()
	 * @see org.flowerplatform.model_access_dao2.model.Relation1#getSource
	 * @model opposite="source" containment="true"
	 * @generated
	 */
	EList<Relation1> getRelations();

} // CodeSyncElement1
