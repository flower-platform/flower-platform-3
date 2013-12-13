/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao2.model.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.flowerplatform.model_access_dao2.model.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelFactoryImpl extends EFactoryImpl implements ModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ModelFactory init() {
		try {
			ModelFactory theModelFactory = (ModelFactory)EPackage.Registry.INSTANCE.getEFactory("model"); 
			if (theModelFactory != null) {
				return theModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelFactoryImpl() {
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
			case ModelPackage.CODE_SYNC_ELEMENT1: return createCodeSyncElement1();
			case ModelPackage.RELATION1: return createRelation1();
			case ModelPackage.NODE1: return createNode1();
			case ModelPackage.DIAGRAM1: return createDiagram1();
			case ModelPackage.RESOURCE_INFO: return createResourceInfo();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CodeSyncElement1 createCodeSyncElement1() {
		CodeSyncElement1Impl codeSyncElement1 = new CodeSyncElement1Impl();
		return codeSyncElement1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Relation1 createRelation1() {
		Relation1Impl relation1 = new Relation1Impl();
		return relation1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Node1 createNode1() {
		Node1Impl node1 = new Node1Impl();
		return node1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Diagram1 createDiagram1() {
		Diagram1Impl diagram1 = new Diagram1Impl();
		return diagram1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceInfo createResourceInfo() {
		ResourceInfoImpl resourceInfo = new ResourceInfoImpl();
		return resourceInfo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelPackage getModelPackage() {
		return (ModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ModelPackage getPackage() {
		return ModelPackage.eINSTANCE;
	}

} //ModelFactoryImpl
