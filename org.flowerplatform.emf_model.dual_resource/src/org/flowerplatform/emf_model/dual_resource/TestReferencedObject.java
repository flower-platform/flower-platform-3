/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.dual_resource;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Test Referenced Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.dual_resource.TestReferencedObject#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.dual_resource.TestReferencedObject#getChildren <em>Children</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage#getTestReferencedObject()
 * @model
 * @generated
 */
public interface TestReferencedObject extends DualResourceObject {
	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.emf_model.dual_resource.TestReferencedObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage#getTestReferencedObject_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<TestReferencedObject> getChildren();

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
	 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage#getTestReferencedObject_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.dual_resource.TestReferencedObject#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // TestReferencedObject
