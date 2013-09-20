/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
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
 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory
 * @model kind="package"
 * @generated
 */
public interface RegExAstPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "regex_ast";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "regex_ast";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "regex_ast";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	RegExAstPackage eINSTANCE = org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.NodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.NodeImpl
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getNode()
	 * @generated
	 */
	int NODE = 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__CHILDREN = 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__PARAMETERS = 1;

	/**
	 * The feature id for the '<em><b>Key Parameter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__KEY_PARAMETER = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__TYPE = 3;

	/**
	 * The feature id for the '<em><b>Category Node</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__CATEGORY_NODE = 4;

	/**
	 * The feature id for the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__OFFSET = 5;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE__LENGTH = 6;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NODE_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.ParameterImpl <em>Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.ParameterImpl
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getParameter()
	 * @generated
	 */
	int PARAMETER = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__OFFSET = 2;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER__LENGTH = 3;

	/**
	 * The number of structural features of the '<em>Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PARAMETER_FEATURE_COUNT = 4;


	/**
	 * The meta object id for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCacheElementImpl <em>Cache Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCacheElementImpl
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstCacheElement()
	 * @generated
	 */
	int REG_EX_AST_CACHE_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CACHE_ELEMENT__PARAMETERS = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Key Parameter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CACHE_ELEMENT__KEY_PARAMETER = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Category Node</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CACHE_ELEMENT__CATEGORY_NODE = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Cache Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CACHE_ELEMENT_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;


	/**
	 * Returns the meta object for class '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Node
	 * @generated
	 */
	EClass getNode();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Node#getChildren()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Children();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Node#getParameters()
	 * @see #getNode()
	 * @generated
	 */
	EReference getNode_Parameters();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getKeyParameter <em>Key Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key Parameter</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Node#getKeyParameter()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_KeyParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Node#getType()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#isCategoryNode <em>Category Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Category Node</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Node#isCategoryNode()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_CategoryNode();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getOffset <em>Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Node#getOffset()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Offset();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Node#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Node#getLength()
	 * @see #getNode()
	 * @generated
	 */
	EAttribute getNode_Length();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Parameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Parameter</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Parameter
	 * @generated
	 */
	EClass getParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Parameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Parameter#getName()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Parameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Parameter#getValue()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Parameter#getOffset <em>Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Parameter#getOffset()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Offset();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.Parameter#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.Parameter#getLength()
	 * @see #getParameter()
	 * @generated
	 */
	EAttribute getParameter_Length();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement <em>Cache Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cache Element</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement
	 * @generated
	 */
	EClass getRegExAstCacheElement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#getParameters()
	 * @see #getRegExAstCacheElement()
	 * @generated
	 */
	EReference getRegExAstCacheElement_Parameters();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#getKeyParameter <em>Key Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key Parameter</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#getKeyParameter()
	 * @see #getRegExAstCacheElement()
	 * @generated
	 */
	EAttribute getRegExAstCacheElement_KeyParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#isCategoryNode <em>Category Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Category Node</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement#isCategoryNode()
	 * @see #getRegExAstCacheElement()
	 * @generated
	 */
	EAttribute getRegExAstCacheElement_CategoryNode();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	RegExAstFactory getRegExAstFactory();

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
		 * The meta object literal for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.NodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.NodeImpl
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getNode()
		 * @generated
		 */
		EClass NODE = eINSTANCE.getNode();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__CHILDREN = eINSTANCE.getNode_Children();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference NODE__PARAMETERS = eINSTANCE.getNode_Parameters();

		/**
		 * The meta object literal for the '<em><b>Key Parameter</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__KEY_PARAMETER = eINSTANCE.getNode_KeyParameter();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__TYPE = eINSTANCE.getNode_Type();

		/**
		 * The meta object literal for the '<em><b>Category Node</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__CATEGORY_NODE = eINSTANCE.getNode_CategoryNode();

		/**
		 * The meta object literal for the '<em><b>Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__OFFSET = eINSTANCE.getNode_Offset();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NODE__LENGTH = eINSTANCE.getNode_Length();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.ParameterImpl <em>Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.ParameterImpl
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getParameter()
		 * @generated
		 */
		EClass PARAMETER = eINSTANCE.getParameter();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__NAME = eINSTANCE.getParameter_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__VALUE = eINSTANCE.getParameter_Value();

		/**
		 * The meta object literal for the '<em><b>Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__OFFSET = eINSTANCE.getParameter_Offset();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PARAMETER__LENGTH = eINSTANCE.getParameter_Length();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCacheElementImpl <em>Cache Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCacheElementImpl
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstCacheElement()
		 * @generated
		 */
		EClass REG_EX_AST_CACHE_ELEMENT = eINSTANCE.getRegExAstCacheElement();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REG_EX_AST_CACHE_ELEMENT__PARAMETERS = eINSTANCE.getRegExAstCacheElement_Parameters();

		/**
		 * The meta object literal for the '<em><b>Key Parameter</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_CACHE_ELEMENT__KEY_PARAMETER = eINSTANCE.getRegExAstCacheElement_KeyParameter();

		/**
		 * The meta object literal for the '<em><b>Category Node</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_CACHE_ELEMENT__CATEGORY_NODE = eINSTANCE.getRegExAstCacheElement_CategoryNode();

	}

} //RegExAstPackage
