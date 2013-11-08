/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.dual_resource;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.flowerplatform.emf_model.dual_resource.DualResourceFactory
 * @model kind="package"
 * @generated
 */
public interface DualResourcePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "dual_resource";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.flower-platform.com/xmi/dual_resource_1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "dual_resource";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	DualResourcePackage eINSTANCE = org.flowerplatform.emf_model.dual_resource.impl.DualResourcePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.dual_resource.impl.DualResourceObjectImpl <em>Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.dual_resource.impl.DualResourceObjectImpl
	 * @see org.flowerplatform.emf_model.dual_resource.impl.DualResourcePackageImpl#getDualResourceObject()
	 * @generated
	 */
	int DUAL_RESOURCE_OBJECT = 2;

	/**
	 * The number of structural features of the '<em>Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DUAL_RESOURCE_OBJECT_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.dual_resource.impl.TestReferenceHolderImpl <em>Test Reference Holder</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.dual_resource.impl.TestReferenceHolderImpl
	 * @see org.flowerplatform.emf_model.dual_resource.impl.DualResourcePackageImpl#getTestReferenceHolder()
	 * @generated
	 */
	int TEST_REFERENCE_HOLDER = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_REFERENCE_HOLDER__NAME = DUAL_RESOURCE_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_REFERENCE_HOLDER__CHILDREN = DUAL_RESOURCE_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Referenced Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_REFERENCE_HOLDER__REFERENCED_OBJECT = DUAL_RESOURCE_OBJECT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Test Reference Holder</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_REFERENCE_HOLDER_FEATURE_COUNT = DUAL_RESOURCE_OBJECT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.flowerplatform.emf_model.dual_resource.impl.TestReferencedObjectImpl <em>Test Referenced Object</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.emf_model.dual_resource.impl.TestReferencedObjectImpl
	 * @see org.flowerplatform.emf_model.dual_resource.impl.DualResourcePackageImpl#getTestReferencedObject()
	 * @generated
	 */
	int TEST_REFERENCED_OBJECT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_REFERENCED_OBJECT__NAME = DUAL_RESOURCE_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_REFERENCED_OBJECT__CHILDREN = DUAL_RESOURCE_OBJECT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Test Referenced Object</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEST_REFERENCED_OBJECT_FEATURE_COUNT = DUAL_RESOURCE_OBJECT_FEATURE_COUNT + 2;


	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder <em>Test Reference Holder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Reference Holder</em>'.
	 * @see org.flowerplatform.emf_model.dual_resource.TestReferenceHolder
	 * @generated
	 */
	EClass getTestReferenceHolder();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getReferencedObject <em>Referenced Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referenced Object</em>'.
	 * @see org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getReferencedObject()
	 * @see #getTestReferenceHolder()
	 * @generated
	 */
	EReference getTestReferenceHolder_ReferencedObject();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getName()
	 * @see #getTestReferenceHolder()
	 * @generated
	 */
	EAttribute getTestReferenceHolder_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.flowerplatform.emf_model.dual_resource.TestReferenceHolder#getChildren()
	 * @see #getTestReferenceHolder()
	 * @generated
	 */
	EReference getTestReferenceHolder_Children();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.dual_resource.TestReferencedObject <em>Test Referenced Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Test Referenced Object</em>'.
	 * @see org.flowerplatform.emf_model.dual_resource.TestReferencedObject
	 * @generated
	 */
	EClass getTestReferencedObject();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.emf_model.dual_resource.TestReferencedObject#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.flowerplatform.emf_model.dual_resource.TestReferencedObject#getChildren()
	 * @see #getTestReferencedObject()
	 * @generated
	 */
	EReference getTestReferencedObject_Children();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.emf_model.dual_resource.DualResourceObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object</em>'.
	 * @see org.flowerplatform.emf_model.dual_resource.DualResourceObject
	 * @generated
	 */
	EClass getDualResourceObject();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.emf_model.dual_resource.TestReferencedObject#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.emf_model.dual_resource.TestReferencedObject#getName()
	 * @see #getTestReferencedObject()
	 * @generated
	 */
	EAttribute getTestReferencedObject_Name();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	DualResourceFactory getDualResourceFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.dual_resource.impl.TestReferenceHolderImpl <em>Test Reference Holder</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.dual_resource.impl.TestReferenceHolderImpl
		 * @see org.flowerplatform.emf_model.dual_resource.impl.DualResourcePackageImpl#getTestReferenceHolder()
		 * @generated
		 */
		EClass TEST_REFERENCE_HOLDER = eINSTANCE.getTestReferenceHolder();

		/**
		 * The meta object literal for the '<em><b>Referenced Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEST_REFERENCE_HOLDER__REFERENCED_OBJECT = eINSTANCE.getTestReferenceHolder_ReferencedObject();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_REFERENCE_HOLDER__NAME = eINSTANCE.getTestReferenceHolder_Name();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEST_REFERENCE_HOLDER__CHILDREN = eINSTANCE.getTestReferenceHolder_Children();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.dual_resource.impl.TestReferencedObjectImpl <em>Test Referenced Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.dual_resource.impl.TestReferencedObjectImpl
		 * @see org.flowerplatform.emf_model.dual_resource.impl.DualResourcePackageImpl#getTestReferencedObject()
		 * @generated
		 */
		EClass TEST_REFERENCED_OBJECT = eINSTANCE.getTestReferencedObject();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEST_REFERENCED_OBJECT__CHILDREN = eINSTANCE.getTestReferencedObject_Children();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.emf_model.dual_resource.impl.DualResourceObjectImpl <em>Object</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.emf_model.dual_resource.impl.DualResourceObjectImpl
		 * @see org.flowerplatform.emf_model.dual_resource.impl.DualResourcePackageImpl#getDualResourceObject()
		 * @generated
		 */
		EClass DUAL_RESOURCE_OBJECT = eINSTANCE.getDualResourceObject();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEST_REFERENCED_OBJECT__NAME = eINSTANCE.getTestReferencedObject_Name();

	}

} //DualResourcePackage
