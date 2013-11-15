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
	 * The number of structural features of the '<em>Code Sync Element1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.CodeSyncElement1EMFImpl <em>Code Sync Element1 EMF</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.CodeSyncElement1EMFImpl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getCodeSyncElement1EMF()
	 * @generated
	 */
	int CODE_SYNC_ELEMENT1_EMF = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1_EMF__ID = CODE_SYNC_ELEMENT1__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1_EMF__NAME = CODE_SYNC_ELEMENT1__NAME;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1_EMF__CHILDREN = CODE_SYNC_ELEMENT1_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Code Sync Element1 EMF</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT1_EMF_FEATURE_COUNT = CODE_SYNC_ELEMENT1_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.EntityEMFImpl <em>Entity EMF</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.EntityEMFImpl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getEntityEMF()
	 * @generated
	 */
	int ENTITY_EMF = 2;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EMF__ID = CODE_SYNC_ELEMENT1_EMF__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EMF__NAME = CODE_SYNC_ELEMENT1_EMF__NAME;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EMF__CHILDREN = CODE_SYNC_ELEMENT1_EMF__CHILDREN;

	/**
	 * The feature id for the '<em><b>Referenced Elements</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EMF__REFERENCED_ELEMENTS = CODE_SYNC_ELEMENT1_EMF_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Entity EMF</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENTITY_EMF_FEATURE_COUNT = CODE_SYNC_ELEMENT1_EMF_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.Node1Impl <em>Node1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.Node1Impl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getNode1()
	 * @generated
	 */
	int NODE1 = 3;

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
	 * The number of structural features of the '<em>Node1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.Node1EMFImpl <em>Node1 EMF</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.Node1EMFImpl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getNode1EMF()
	 * @generated
	 */
	int NODE1_EMF = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1_EMF__ID = NODE1__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1_EMF__NAME = NODE1__NAME;

	/**
	 * The feature id for the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1_EMF__DIAGRAMMABLE_ELEMENT = NODE1_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1_EMF__CHILDREN = NODE1_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Node1 EMF</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE1_EMF_FEATURE_COUNT = NODE1_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.Diagram1Impl <em>Diagram1</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.Diagram1Impl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getDiagram1()
	 * @generated
	 */
	int DIAGRAM1 = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1__ID = NODE1_EMF__ID;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1__NAME = NODE1_EMF__NAME;

	/**
	 * The feature id for the '<em><b>Diagrammable Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1__DIAGRAMMABLE_ELEMENT = NODE1_EMF__DIAGRAMMABLE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1__CHILDREN = NODE1_EMF__CHILDREN;

	/**
	 * The number of structural features of the '<em>Diagram1</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DIAGRAM1_FEATURE_COUNT = NODE1_EMF_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model_access_dao.model.impl.ResourceInfoImpl <em>Resource Info</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model_access_dao.model.impl.ResourceInfoImpl
	 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getResourceInfo()
	 * @generated
	 */
	int RESOURCE_INFO = 6;

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
	 * Returns the meta object for class '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF <em>Code Sync Element1 EMF</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Code Sync Element1 EMF</em>'.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF
	 * @generated
	 */
	EClass getCodeSyncElement1EMF();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.flowerplatform.model_access_dao.model.CodeSyncElement1EMF#getChildren()
	 * @see #getCodeSyncElement1EMF()
	 * @generated
	 */
	EReference getCodeSyncElement1EMF_Children();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model_access_dao.model.EntityEMF <em>Entity EMF</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity EMF</em>'.
	 * @see org.flowerplatform.model_access_dao.model.EntityEMF
	 * @generated
	 */
	EClass getEntityEMF();

	/**
	 * Returns the meta object for the reference list '{@link org.flowerplatform.model_access_dao.model.EntityEMF#getReferencedElements <em>Referenced Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Referenced Elements</em>'.
	 * @see org.flowerplatform.model_access_dao.model.EntityEMF#getReferencedElements()
	 * @see #getEntityEMF()
	 * @generated
	 */
	EReference getEntityEMF_ReferencedElements();

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
	 * Returns the meta object for class '{@link org.flowerplatform.model_access_dao.model.Node1EMF <em>Node1 EMF</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node1 EMF</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Node1EMF
	 * @generated
	 */
	EClass getNode1EMF();

	/**
	 * Returns the meta object for the reference '{@link org.flowerplatform.model_access_dao.model.Node1EMF#getDiagrammableElement <em>Diagrammable Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Diagrammable Element</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Node1EMF#getDiagrammableElement()
	 * @see #getNode1EMF()
	 * @generated
	 */
	EReference getNode1EMF_DiagrammableElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.model_access_dao.model.Node1EMF#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.flowerplatform.model_access_dao.model.Node1EMF#getChildren()
	 * @see #getNode1EMF()
	 * @generated
	 */
	EReference getNode1EMF_Children();

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
		 * The meta object literal for the '{@link org.flowerplatform.model_access_dao.model.impl.CodeSyncElement1EMFImpl <em>Code Sync Element1 EMF</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model_access_dao.model.impl.CodeSyncElement1EMFImpl
		 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getCodeSyncElement1EMF()
		 * @generated
		 */
		EClass CODE_SYNC_ELEMENT1_EMF = eINSTANCE.getCodeSyncElement1EMF();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CODE_SYNC_ELEMENT1_EMF__CHILDREN = eINSTANCE.getCodeSyncElement1EMF_Children();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.model_access_dao.model.impl.EntityEMFImpl <em>Entity EMF</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model_access_dao.model.impl.EntityEMFImpl
		 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getEntityEMF()
		 * @generated
		 */
		EClass ENTITY_EMF = eINSTANCE.getEntityEMF();

		/**
		 * The meta object literal for the '<em><b>Referenced Elements</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENTITY_EMF__REFERENCED_ELEMENTS = eINSTANCE.getEntityEMF_ReferencedElements();

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
		 * The meta object literal for the '{@link org.flowerplatform.model_access_dao.model.impl.Node1EMFImpl <em>Node1 EMF</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model_access_dao.model.impl.Node1EMFImpl
		 * @see org.flowerplatform.model_access_dao.model.impl.ModelPackageImpl#getNode1EMF()
		 * @generated
		 */
		EClass NODE1_EMF = eINSTANCE.getNode1EMF();

		/**
		 * The meta object literal for the '<em><b>Diagrammable Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE1_EMF__DIAGRAMMABLE_ELEMENT = eINSTANCE.getNode1EMF_DiagrammableElement();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE1_EMF__CHILDREN = eINSTANCE.getNode1EMF_Children();

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
