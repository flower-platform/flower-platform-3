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
 * A representation of the model object '<em><b>Test Reference Holder</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getName <em>Name</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getChildren <em>Children</em>}</li>
 *   <li>{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getReferencedObject <em>Referenced Object</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage#getTestReferenceHolder()
 * @model
 * @generated
 */
public interface TestReferenceHolder extends DualResourceObject {
	/**
	 * Returns the value of the '<em><b>Referenced Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced Object</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced Object</em>' reference.
	 * @see #setReferencedObject(TestReferencedObject)
	 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage#getTestReferenceHolder_ReferencedObject()
	 * @model
	 * @generated
	 */
	TestReferencedObject getReferencedObject();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getReferencedObject <em>Referenced Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referenced Object</em>' reference.
	 * @see #getReferencedObject()
	 * @generated
	 */
	void setReferencedObject(TestReferencedObject value);

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
	 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage#getTestReferenceHolder_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage#getTestReferenceHolder_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<TestReferenceHolder> getChildren();

} // TestReferenceHolder
