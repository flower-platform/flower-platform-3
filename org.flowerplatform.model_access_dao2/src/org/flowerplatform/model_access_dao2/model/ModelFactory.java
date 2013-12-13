/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao2.model;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.model_access_dao2.model.ModelPackage
 * @generated
 */
public interface ModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelFactory eINSTANCE = org.flowerplatform.model_access_dao2.model.impl.ModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Code Sync Element1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Code Sync Element1</em>'.
	 * @generated
	 */
	CodeSyncElement1 createCodeSyncElement1();

	/**
	 * Returns a new object of class '<em>Relation1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Relation1</em>'.
	 * @generated
	 */
	Relation1 createRelation1();

	/**
	 * Returns a new object of class '<em>Node1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node1</em>'.
	 * @generated
	 */
	Node1 createNode1();

	/**
	 * Returns a new object of class '<em>Diagram1</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Diagram1</em>'.
	 * @generated
	 */
	Diagram1 createDiagram1();

	/**
	 * Returns a new object of class '<em>Resource Info</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resource Info</em>'.
	 * @generated
	 */
	ResourceInfo createResourceInfo();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ModelPackage getModelPackage();

} //ModelFactory
