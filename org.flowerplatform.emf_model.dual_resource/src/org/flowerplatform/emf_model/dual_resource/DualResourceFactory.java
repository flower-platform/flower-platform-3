/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.dual_resource;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage
 * @generated
 */
public interface DualResourceFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DualResourceFactory eINSTANCE = org.flowerplatform.emf_model.dual_resource.impl.DualResourceFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Test Reference Holder</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Test Reference Holder</em>'.
	 * @generated
	 */
	TestReferenceHolder createTestReferenceHolder();

	/**
	 * Returns a new object of class '<em>Test Referenced Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Test Referenced Object</em>'.
	 * @generated
	 */
	TestReferencedObject createTestReferencedObject();

	/**
	 * Returns a new object of class '<em>Object</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Object</em>'.
	 * @generated
	 */
	DualResourceObject createDualResourceObject();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	DualResourcePackage getDualResourcePackage();

} //DualResourceFactory
