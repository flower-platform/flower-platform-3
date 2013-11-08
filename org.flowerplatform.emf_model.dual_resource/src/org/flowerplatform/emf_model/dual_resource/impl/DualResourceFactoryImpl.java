/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.emf_model.dual_resource.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.flowerplatform.emf_model.dual_resource.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class DualResourceFactoryImpl extends EFactoryImpl implements DualResourceFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static DualResourceFactory init() {
		try {
			DualResourceFactory theDualResourceFactory = (DualResourceFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.flower-platform.com/xmi/dual_resource_1.0.0"); 
			if (theDualResourceFactory != null) {
				return theDualResourceFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new DualResourceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DualResourceFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case DualResourcePackage.TEST_REFERENCE_HOLDER: return createTestReferenceHolder();
			case DualResourcePackage.TEST_REFERENCED_OBJECT: return createTestReferencedObject();
			case DualResourcePackage.DUAL_RESOURCE_OBJECT: return createDualResourceObject();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestReferenceHolder createTestReferenceHolder() {
		TestReferenceHolderImpl testReferenceHolder = new TestReferenceHolderImpl();
		return testReferenceHolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TestReferencedObject createTestReferencedObject() {
		TestReferencedObjectImpl testReferencedObject = new TestReferencedObjectImpl();
		return testReferencedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DualResourceObject createDualResourceObject() {
		DualResourceObjectImpl dualResourceObject = new DualResourceObjectImpl();
		return dualResourceObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DualResourcePackage getDualResourcePackage() {
		return (DualResourcePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static DualResourcePackage getPackage() {
		return DualResourcePackage.eINSTANCE;
	}

} //DualResourceFactoryImpl
