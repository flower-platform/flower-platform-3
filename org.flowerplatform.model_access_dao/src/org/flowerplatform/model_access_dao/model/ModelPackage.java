/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model_access_dao.model;

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
 * @see org.flowerplatform.model_access_dao.model.ModelFactory
 * @model kind="package"
 * @generated
 */
public interface ModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "model";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "model";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "model";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ModelPackage eINSTANCE = org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.CodeSyncElement1Impl <em>Code Sync Element1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.CodeSyncElement1Impl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getCodeSyncElement1()
	 * @generated
	 */
	int CODE_SYNC_ELEMENT1 = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1__NAME = 1;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1__CHILDREN = 2;

	/**
	 * The feature id for the '<em><b>Relations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1__RELATIONS = 3;

	/**
	 * The number of structural features of the '<em>Code Sync Element1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.Relation1Impl <em>Relation1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.Relation1Impl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getRelation1()
	 * @generated
	 */
	int RELATION1 = 1;

	/**
	 * The feature id for the '<em><b>Source</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION1__SOURCE = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION1__TARGET = 1;

	/**
	 * The number of structural features of the '<em>Relation1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RELATION1_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.Node1Impl <em>Node1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.Node1Impl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getNode1()
	 * @generated
	 */
	int NODE1 = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1__ID = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1__NAME = 1;

	/**
	 * The feature id for the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1__DIAGRAMMABLE_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1__CHILDREN = 3;

	/**
	 * The number of structural features of the '<em>Node1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.Diagram1Impl <em>Diagram1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.Diagram1Impl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getDiagram1()
	 * @generated
	 */
	int DIAGRAM1 = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1__ID = NODE1__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1__NAME = NODE1__NAME;

	/**
	 * The feature id for the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1__DIAGRAMMABLE_ELEMENT = NODE1__DIAGRAMMABLE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1__CHILDREN = NODE1__CHILDREN;

	/**
	 * The number of structural features of the '<em>Diagram1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1_FEATURE_COUNT = NODE1_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.ResourceInfoImpl <em>Resource Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.ResourceInfoImpl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getResourceInfo()
	 * @generated
	 */
	int RESOURCE_INFO = 4;

	/**
	 * The feature id for the '<em><b>Repo Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_INFO__REPO_ID = 0;

	/**
	 * The feature id for the '<em><b>Discussable Design Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_INFO__DISCUSSABLE_DESIGN_ID = 1;

	/**
	 * The feature id for the '<em><b>Resource Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_INFO__RESOURCE_ID = 2;

	/**
	 * The number of structural features of the '<em>Resource Info</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOURCE_INFO_FEATURE_COUNT = 3;


	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1 <em>Code Sync Element1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Code Sync Element1</em>'.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1
	 * @generated
	 */
	EClass getCodeSyncElement1();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1#getId()
	 * @see #getCodeSyncElement1()
	 * @generated
	 */
	EAttribute getCodeSyncElement1_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1#getName()
	 * @see #getCodeSyncElement1()
	 * @generated
	 */
	EAttribute getCodeSyncElement1_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1#getChildren()
	 * @see #getCodeSyncElement1()
	 * @generated
	 */
	EReference getCodeSyncElement1_Children();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1#getRelations <em>Relations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Relations</em>'.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1#getRelations()
	 * @see #getCodeSyncElement1()
	 * @generated
	 */
	EReference getCodeSyncElement1_Relations();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model_access_dao.model.Relation1 <em>Relation1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Relation1</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Relation1
	 * @generated
	 */
	EClass getRelation1();

	/**
	 * Returns the meta object for the container reference '{@link org.flowerplatform.model_access_dao.model.Relation1#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Source</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Relation1#getSource()
	 * @see #getRelation1()
	 * @generated
	 */
	EReference getRelation1_Source();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.model_access_dao.model.Relation1#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Relation1#getTarget()
	 * @see #getRelation1()
	 * @generated
	 */
	EReference getRelation1_Target();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model_access_dao.model.Node1 <em>Node1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node1</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Node1
	 * @generated
	 */
	EClass getNode1();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model_access_dao.model.Node1#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Node1#getId()
	 * @see #getNode1()
	 * @generated
	 */
	EAttribute getNode1_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model_access_dao.model.Node1#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Node1#getName()
	 * @see #getNode1()
	 * @generated
	 */
	EAttribute getNode1_Name();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.model_access_dao.model.Node1#getDiagrammableElement <em>Diagrammable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Diagrammable Element</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Node1#getDiagrammableElement()
	 * @see #getNode1()
	 * @generated
	 */
	EReference getNode1_DiagrammableElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.model_access_dao.model.Node1#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Node1#getChildren()
	 * @see #getNode1()
	 * @generated
	 */
	EReference getNode1_Children();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model_access_dao.model.Diagram1 <em>Diagram1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Diagram1</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Diagram1
	 * @generated
	 */
	EClass getDiagram1();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model_access_dao.model.ResourceInfo <em>Resource Info</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resource Info</em>'.
	 * @see org.flowerplatform.model_access_dao.model.ResourceInfo
	 * @generated
	 */
	EClass getResourceInfo();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model_access_dao.model.ResourceInfo#getRepoId <em>Repo Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Repo Id</em>'.
	 * @see org.flowerplatform.model_access_dao.model.ResourceInfo#getRepoId()
	 * @see #getResourceInfo()
	 * @generated
	 */
	EAttribute getResourceInfo_RepoId();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model_access_dao.model.ResourceInfo#getDiscussableDesignId <em>Discussable Design Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Discussable Design Id</em>'.
	 * @see org.flowerplatform.model_access_dao.model.ResourceInfo#getDiscussableDesignId()
	 * @see #getResourceInfo()
	 * @generated
	 */
	EAttribute getResourceInfo_DiscussableDesignId();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model_access_dao.model.ResourceInfo#getResourceId <em>Resource Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Resource Id</em>'.
	 * @see org.flowerplatform.model_access_dao.model.ResourceInfo#getResourceId()
	 * @see #getResourceInfo()
	 * @generated
	 */
	EAttribute getResourceInfo_ResourceId();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ModelFactory getModelFactory();

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
		 * The meta object literal for the '{@link org.flowerplatform.model_access_dao.model.impl.CodeSyncElement1Impl <em>Code Sync Element1</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model_access_dao.model.impl.CodeSyncElement1Impl
		 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getCodeSyncElement1()
		 * @generated
		 */
		EClass CODE_SYNC_ELEMENT1 = eINSTANCE.getCodeSyncElement1();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT1__ID = eINSTANCE.getCodeSyncElement1_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT1__NAME = eINSTANCE.getCodeSyncElement1_Name();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CODE_SYNC_ELEMENT1__CHILDREN = eINSTANCE.getCodeSyncElement1_Children();

		/**
		 * The meta object literal for the '<em><b>Relations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CODE_SYNC_ELEMENT1__RELATIONS = eINSTANCE.getCodeSyncElement1_Relations();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.model_access_dao.model.impl.Relation1Impl <em>Relation1</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model_access_dao.model.impl.Relation1Impl
		 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getRelation1()
		 * @generated
		 */
		EClass RELATION1 = eINSTANCE.getRelation1();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RELATION1__SOURCE = eINSTANCE.getRelation1_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RELATION1__TARGET = eINSTANCE.getRelation1_Target();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.model_access_dao.model.impl.Node1Impl <em>Node1</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model_access_dao.model.impl.Node1Impl
		 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getNode1()
		 * @generated
		 */
		EClass NODE1 = eINSTANCE.getNode1();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE1__ID = eINSTANCE.getNode1_Id();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE1__NAME = eINSTANCE.getNode1_Name();

		/**
		 * The meta object literal for the '<em><b>Diagrammable Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE1__DIAGRAMMABLE_ELEMENT = eINSTANCE.getNode1_DiagrammableElement();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE1__CHILDREN = eINSTANCE.getNode1_Children();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.model_access_dao.model.impl.Diagram1Impl <em>Diagram1</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model_access_dao.model.impl.Diagram1Impl
		 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getDiagram1()
		 * @generated
		 */
		EClass DIAGRAM1 = eINSTANCE.getDiagram1();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.model_access_dao.model.impl.ResourceInfoImpl <em>Resource Info</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model_access_dao.model.impl.ResourceInfoImpl
		 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getResourceInfo()
		 * @generated
		 */
		EClass RESOURCE_INFO = eINSTANCE.getResourceInfo();

		/**
		 * The meta object literal for the '<em><b>Repo Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_INFO__REPO_ID = eINSTANCE.getResourceInfo_RepoId();

		/**
		 * The meta object literal for the '<em><b>Discussable Design Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_INFO__DISCUSSABLE_DESIGN_ID = eINSTANCE.getResourceInfo_DiscussableDesignId();

		/**
		 * The meta object literal for the '<em><b>Resource Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOURCE_INFO__RESOURCE_ID = eINSTANCE.getResourceInfo_ResourceId();

	}

} //ModelPackage
