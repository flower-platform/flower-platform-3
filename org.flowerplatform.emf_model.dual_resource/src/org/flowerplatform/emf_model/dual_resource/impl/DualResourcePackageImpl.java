/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.dual_resource.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.flowerplatform.emf_model.dual_resource.DualResourceFactory;
import org.flowerplatform.emf_model.dual_resource.DualResourceObject;
import org.flowerplatform.emf_model.dual_resource.DualResourcePackage;
import org.flowerplatform.emf_model.dual_resource.TestReferenceHolder;
import org.flowerplatform.emf_model.dual_resource.TestReferencedObject;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DualResourcePackageImpl extends EPackageImpl implements DualResourcePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass testReferenceHolderEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass testReferencedObjectEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dualResourceObjectEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.flowerplatform.emf_model.dual_resource.DualResourcePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private DualResourcePackageImpl() {
		super(eNS_URI, DualResourceFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link DualResourcePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static DualResourcePackage init() {
		if (isInited) return (DualResourcePackage)EPackage.Registry.INSTANCE.getEPackage(DualResourcePackage.eNS_URI);

		// Obtain or create and register package
		DualResourcePackageImpl theDualResourcePackage = (DualResourcePackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof DualResourcePackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new DualResourcePackageImpl());

		isInited = true;

		// Create package meta-data objects
		theDualResourcePackage.createPackageContents();

		// Initialize created meta-data
		theDualResourcePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theDualResourcePackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(DualResourcePackage.eNS_URI, theDualResourcePackage);
		return theDualResourcePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTestReferenceHolder() {
		return testReferenceHolderEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTestReferenceHolder_ReferencedObject() {
		return (EReference)testReferenceHolderEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTestReferenceHolder_Name() {
		return (EAttribute)testReferenceHolderEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTestReferenceHolder_Children() {
		return (EReference)testReferenceHolderEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getTestReferencedObject() {
		return testReferencedObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getTestReferencedObject_Children() {
		return (EReference)testReferencedObjectEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getDualResourceObject() {
		return dualResourceObjectEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getTestReferencedObject_Name() {
		return (EAttribute)testReferencedObjectEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DualResourceFactory getDualResourceFactory() {
		return (DualResourceFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		testReferenceHolderEClass = createEClass(TEST_REFERENCE_HOLDER);
		createEAttribute(testReferenceHolderEClass, TEST_REFERENCE_HOLDER__NAME);
		createEReference(testReferenceHolderEClass, TEST_REFERENCE_HOLDER__CHILDREN);
		createEReference(testReferenceHolderEClass, TEST_REFERENCE_HOLDER__REFERENCED_OBJECT);

		testReferencedObjectEClass = createEClass(TEST_REFERENCED_OBJECT);
		createEAttribute(testReferencedObjectEClass, TEST_REFERENCED_OBJECT__NAME);
		createEReference(testReferencedObjectEClass, TEST_REFERENCED_OBJECT__CHILDREN);

		dualResourceObjectEClass = createEClass(DUAL_RESOURCE_OBJECT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		testReferenceHolderEClass.getESuperTypes().add(this.getDualResourceObject());
		testReferencedObjectEClass.getESuperTypes().add(this.getDualResourceObject());

		// Initialize classes and features; add operations and parameters
		initEClass(testReferenceHolderEClass, TestReferenceHolder.class, "TestReferenceHolder", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTestReferenceHolder_Name(), ecorePackage.getEString(), "name", null, 0, 1, TestReferenceHolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTestReferenceHolder_Children(), this.getTestReferenceHolder(), null, "children", null, 0, -1, TestReferenceHolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTestReferenceHolder_ReferencedObject(), this.getTestReferencedObject(), null, "referencedObject", null, 0, 1, TestReferenceHolder.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(testReferencedObjectEClass, TestReferencedObject.class, "TestReferencedObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getTestReferencedObject_Name(), ecorePackage.getEString(), "name", null, 0, 1, TestReferencedObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTestReferencedObject_Children(), this.getTestReferencedObject(), null, "children", null, 0, -1, TestReferencedObject.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dualResourceObjectEClass, DualResourceObject.class, "DualResourceObject", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //DualResourcePackageImpl
