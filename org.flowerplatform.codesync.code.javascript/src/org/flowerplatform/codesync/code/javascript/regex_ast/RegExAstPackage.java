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
	String eNS_URI = "http://www.flower-platform.com/xmi/regex_ast_1.0.0";

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
	 * The meta object id for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstNodeImpl <em>Node</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstNodeImpl
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstNode()
	 * @generated
	 */
	int REG_EX_AST_NODE = 0;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__CHILDREN = 0;

	/**
	 * The feature id for the '<em><b>Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__PARAMETERS = 1;

	/**
	 * The feature id for the '<em><b>Key Parameter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__KEY_PARAMETER = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__TYPE = 3;

	/**
	 * The feature id for the '<em><b>Category Node</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__CATEGORY_NODE = 4;

	/**
	 * The feature id for the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__OFFSET = 5;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__LENGTH = 6;

	/**
	 * The feature id for the '<em><b>Added</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__ADDED = 7;

	/**
	 * The feature id for the '<em><b>Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__DELETED = 8;

	/**
	 * The feature id for the '<em><b>Children Insert Points</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__CHILDREN_INSERT_POINTS = 9;

	/**
	 * The feature id for the '<em><b>Next Sibling Insert Point</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__NEXT_SIBLING_INSERT_POINT = 10;

	/**
	 * The feature id for the '<em><b>Next Sibling Separator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__NEXT_SIBLING_SEPARATOR = 11;

	/**
	 * The feature id for the '<em><b>Child Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE__CHILD_TYPE = 12;

	/**
	 * The number of structural features of the '<em>Node</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE_FEATURE_COUNT = 13;

	/**
	 * The meta object id for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.StringToIntegerEntryImpl <em>String To Integer Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.StringToIntegerEntryImpl
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getStringToIntegerEntry()
	 * @generated
	 */
	int STRING_TO_INTEGER_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_INTEGER_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_INTEGER_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>String To Integer Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_TO_INTEGER_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstNodeParameterImpl <em>Node Parameter</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstNodeParameterImpl
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstNodeParameter()
	 * @generated
	 */
	int REG_EX_AST_NODE_PARAMETER = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE_PARAMETER__NAME = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE_PARAMETER__VALUE = 1;

	/**
	 * The feature id for the '<em><b>Offset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE_PARAMETER__OFFSET = 2;

	/**
	 * The feature id for the '<em><b>Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE_PARAMETER__LENGTH = 3;

	/**
	 * The number of structural features of the '<em>Node Parameter</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_NODE_PARAMETER_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCodeSyncElementImpl <em>Code Sync Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCodeSyncElementImpl
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstCodeSyncElement()
	 * @generated
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__SYNCHRONIZED = CodeSyncPackage.CODE_SYNC_ELEMENT__SYNCHRONIZED;

	/**
	 * The feature id for the '<em><b>Children Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED = CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED;

	/**
	 * The feature id for the '<em><b>Added</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__ADDED = CodeSyncPackage.CODE_SYNC_ELEMENT__ADDED;

	/**
	 * The feature id for the '<em><b>Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__DELETED = CodeSyncPackage.CODE_SYNC_ELEMENT__DELETED;

	/**
	 * The feature id for the '<em><b>Status Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__STATUS_FLAGS = CodeSyncPackage.CODE_SYNC_ELEMENT__STATUS_FLAGS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__NAME = CodeSyncPackage.CODE_SYNC_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__TYPE = CodeSyncPackage.CODE_SYNC_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Ast Cache Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT = CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__CHILDREN = CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN;

	/**
	 * The feature id for the '<em><b>Feature Changes</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__FEATURE_CHANGES = CodeSyncPackage.CODE_SYNC_ELEMENT__FEATURE_CHANGES;

	/**
	 * The feature id for the '<em><b>Relations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__RELATIONS = CodeSyncPackage.CODE_SYNC_ELEMENT__RELATIONS;

	/**
	 * The feature id for the '<em><b>Next Sibling Separator</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__NEXT_SIBLING_SEPARATOR = CodeSyncPackage.CODE_SYNC_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Child Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT__CHILD_TYPE = CodeSyncPackage.CODE_SYNC_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Code Sync Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REG_EX_AST_CODE_SYNC_ELEMENT_FEATURE_COUNT = CodeSyncPackage.CODE_SYNC_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The meta object id for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCacheElementImpl <em>Cache Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCacheElementImpl
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstCacheElement()
	 * @generated
	 */
	int REG_EX_AST_CACHE_ELEMENT = 4;

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
	 * Returns the meta object for class '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode <em>Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode
	 * @generated
	 */
	EClass getRegExAstNode();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getChildren()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EReference getRegExAstNode_Children();

	/**
	 * Returns the meta object for the containment reference list '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getParameters <em>Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameters</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getParameters()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EReference getRegExAstNode_Parameters();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getKeyParameter <em>Key Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key Parameter</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getKeyParameter()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_KeyParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getType()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_Type();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isCategoryNode <em>Category Node</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Category Node</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isCategoryNode()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_CategoryNode();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getOffset <em>Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getOffset()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_Offset();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getLength()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_Length();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isAdded <em>Added</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Added</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isAdded()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_Added();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isDeleted <em>Deleted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Deleted</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#isDeleted()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_Deleted();

	/**
	 * Returns the meta object for the map '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getChildrenInsertPoints <em>Children Insert Points</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Children Insert Points</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getChildrenInsertPoints()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EReference getRegExAstNode_ChildrenInsertPoints();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getNextSiblingInsertPoint <em>Next Sibling Insert Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Next Sibling Insert Point</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getNextSiblingInsertPoint()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_NextSiblingInsertPoint();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getNextSiblingSeparator <em>Next Sibling Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Next Sibling Separator</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getNextSiblingSeparator()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_NextSiblingSeparator();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getChildType <em>Child Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Child Type</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode#getChildType()
	 * @see #getRegExAstNode()
	 * @generated
	 */
	EAttribute getRegExAstNode_ChildType();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>String To Integer Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>String To Integer Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueDataType="org.eclipse.emf.ecore.EIntegerObject"
	 * @generated
	 */
	EClass getStringToIntegerEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getStringToIntegerEntry()
	 * @generated
	 */
	EAttribute getStringToIntegerEntry_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getStringToIntegerEntry()
	 * @generated
	 */
	EAttribute getStringToIntegerEntry_Value();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter <em>Node Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Node Parameter</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter
	 * @generated
	 */
	EClass getRegExAstNodeParameter();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter#getName()
	 * @see #getRegExAstNodeParameter()
	 * @generated
	 */
	EAttribute getRegExAstNodeParameter_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter#getValue()
	 * @see #getRegExAstNodeParameter()
	 * @generated
	 */
	EAttribute getRegExAstNodeParameter_Value();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter#getOffset <em>Offset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Offset</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter#getOffset()
	 * @see #getRegExAstNodeParameter()
	 * @generated
	 */
	EAttribute getRegExAstNodeParameter_Offset();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter#getLength <em>Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Length</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter#getLength()
	 * @see #getRegExAstNodeParameter()
	 * @generated
	 */
	EAttribute getRegExAstNodeParameter_Length();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement <em>Code Sync Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Code Sync Element</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement
	 * @generated
	 */
	EClass getRegExAstCodeSyncElement();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement#getNextSiblingSeparator <em>Next Sibling Separator</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Next Sibling Separator</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement#getNextSiblingSeparator()
	 * @see #getRegExAstCodeSyncElement()
	 * @generated
	 */
	EAttribute getRegExAstCodeSyncElement_NextSiblingSeparator();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement#getChildType <em>Child Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Child Type</em>'.
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement#getChildType()
	 * @see #getRegExAstCodeSyncElement()
	 * @generated
	 */
	EAttribute getRegExAstCodeSyncElement_ChildType();

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
		 * The meta object literal for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstNodeImpl <em>Node</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstNodeImpl
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstNode()
		 * @generated
		 */
		EClass REG_EX_AST_NODE = eINSTANCE.getRegExAstNode();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REG_EX_AST_NODE__CHILDREN = eINSTANCE.getRegExAstNode_Children();

		/**
		 * The meta object literal for the '<em><b>Parameters</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REG_EX_AST_NODE__PARAMETERS = eINSTANCE.getRegExAstNode_Parameters();

		/**
		 * The meta object literal for the '<em><b>Key Parameter</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__KEY_PARAMETER = eINSTANCE.getRegExAstNode_KeyParameter();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__TYPE = eINSTANCE.getRegExAstNode_Type();

		/**
		 * The meta object literal for the '<em><b>Category Node</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__CATEGORY_NODE = eINSTANCE.getRegExAstNode_CategoryNode();

		/**
		 * The meta object literal for the '<em><b>Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__OFFSET = eINSTANCE.getRegExAstNode_Offset();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__LENGTH = eINSTANCE.getRegExAstNode_Length();

		/**
		 * The meta object literal for the '<em><b>Added</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__ADDED = eINSTANCE.getRegExAstNode_Added();

		/**
		 * The meta object literal for the '<em><b>Deleted</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__DELETED = eINSTANCE.getRegExAstNode_Deleted();

		/**
		 * The meta object literal for the '<em><b>Children Insert Points</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REG_EX_AST_NODE__CHILDREN_INSERT_POINTS = eINSTANCE.getRegExAstNode_ChildrenInsertPoints();

		/**
		 * The meta object literal for the '<em><b>Next Sibling Insert Point</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__NEXT_SIBLING_INSERT_POINT = eINSTANCE.getRegExAstNode_NextSiblingInsertPoint();

		/**
		 * The meta object literal for the '<em><b>Next Sibling Separator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__NEXT_SIBLING_SEPARATOR = eINSTANCE.getRegExAstNode_NextSiblingSeparator();

		/**
		 * The meta object literal for the '<em><b>Child Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE__CHILD_TYPE = eINSTANCE.getRegExAstNode_ChildType();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.StringToIntegerEntryImpl <em>String To Integer Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.StringToIntegerEntryImpl
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getStringToIntegerEntry()
		 * @generated
		 */
		EClass STRING_TO_INTEGER_ENTRY = eINSTANCE.getStringToIntegerEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_TO_INTEGER_ENTRY__KEY = eINSTANCE.getStringToIntegerEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_TO_INTEGER_ENTRY__VALUE = eINSTANCE.getStringToIntegerEntry_Value();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstNodeParameterImpl <em>Node Parameter</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstNodeParameterImpl
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstNodeParameter()
		 * @generated
		 */
		EClass REG_EX_AST_NODE_PARAMETER = eINSTANCE.getRegExAstNodeParameter();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE_PARAMETER__NAME = eINSTANCE.getRegExAstNodeParameter_Name();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE_PARAMETER__VALUE = eINSTANCE.getRegExAstNodeParameter_Value();

		/**
		 * The meta object literal for the '<em><b>Offset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE_PARAMETER__OFFSET = eINSTANCE.getRegExAstNodeParameter_Offset();

		/**
		 * The meta object literal for the '<em><b>Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_NODE_PARAMETER__LENGTH = eINSTANCE.getRegExAstNodeParameter_Length();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCodeSyncElementImpl <em>Code Sync Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstCodeSyncElementImpl
		 * @see org.flowerplatform.codesync.code.javascript.regex_ast.impl.RegExAstPackageImpl#getRegExAstCodeSyncElement()
		 * @generated
		 */
		EClass REG_EX_AST_CODE_SYNC_ELEMENT = eINSTANCE.getRegExAstCodeSyncElement();

		/**
		 * The meta object literal for the '<em><b>Next Sibling Separator</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_CODE_SYNC_ELEMENT__NEXT_SIBLING_SEPARATOR = eINSTANCE.getRegExAstCodeSyncElement_NextSiblingSeparator();

		/**
		 * The meta object literal for the '<em><b>Child Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REG_EX_AST_CODE_SYNC_ELEMENT__CHILD_TYPE = eINSTANCE.getRegExAstCodeSyncElement_ChildType();

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
