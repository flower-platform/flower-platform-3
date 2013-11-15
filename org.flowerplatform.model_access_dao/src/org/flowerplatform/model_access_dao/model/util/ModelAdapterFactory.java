/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao.model.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.flowerplatform.model_access_dao.model.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.model_access_dao.model.ModelPackage
 * @generated
 */
public class ModelAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static ModelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ModelPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelSwitch<Adapter> modelSwitch =
		new ModelSwitch<Adapter>() {
			@Override
			public Adapter caseCodeSyncElement1(CodeSyncElement1 object) {
				return createCodeSyncElement1Adapter();
			}
			@Override
			public Adapter caseCodeSyncElement1EMF(CodeSyncElement1EMF object) {
				return createCodeSyncElement1EMFAdapter();
			}
			@Override
			public Adapter caseEntityEMF(EntityEMF object) {
				return createEntityEMFAdapter();
			}
			@Override
			public Adapter caseNode1(Node1 object) {
				return createNode1Adapter();
			}
			@Override
			public Adapter caseNode1EMF(Node1EMF object) {
				return createNode1EMFAdapter();
			}
			@Override
			public Adapter caseDiagram1(Diagram1 object) {
				return createDiagram1Adapter();
			}
			@Override
			public Adapter caseResourceInfo(ResourceInfo object) {
				return createResourceInfoAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1 <em>Code Sync Element1</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1
	 * @generated
	 */
	public Adapter createCodeSyncElement1Adapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF <em>Code Sync Element1 EMF</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF
	 * @generated
	 */
	public Adapter createCodeSyncElement1EMFAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.model_access_dao.model.EntityEMF <em>Entity EMF</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.model_access_dao.model.EntityEMF
	 * @generated
	 */
	public Adapter createEntityEMFAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.model_access_dao.model.Node1 <em>Node1</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.model_access_dao.model.Node1
	 * @generated
	 */
	public Adapter createNode1Adapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.model_access_dao.model.Node1EMF <em>Node1 EMF</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.model_access_dao.model.Node1EMF
	 * @generated
	 */
	public Adapter createNode1EMFAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.model_access_dao.model.Diagram1 <em>Diagram1</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.model_access_dao.model.Diagram1
	 * @generated
	 */
	public Adapter createDiagram1Adapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.flowerplatform.model_access_dao.model.ResourceInfo <em>Resource Info</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.flowerplatform.model_access_dao.model.ResourceInfo
	 * @generated
	 */
	public Adapter createResourceInfoAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //ModelAdapterFactory
