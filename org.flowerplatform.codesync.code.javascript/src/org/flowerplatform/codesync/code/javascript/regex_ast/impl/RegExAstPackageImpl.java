/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.codesync.code.javascript.regex_ast.impl;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;
import java.util.Map;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCacheElement;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstCodeSyncElement;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstFactory;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNode;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstNodeParameter;
import org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class RegExAstPackageImpl extends EPackageImpl implements RegExAstPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass regExAstNodeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass stringToIntegerEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass regExAstNodeParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass regExAstCodeSyncElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass regExAstCacheElementEClass = null;

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
	 * @see org.flowerplatform.codesync.code.javascript.regex_ast.RegExAstPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private RegExAstPackageImpl() {
		super(eNS_URI, RegExAstFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link RegExAstPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static RegExAstPackage init() {
		if (isInited) return (RegExAstPackage)EPackage.Registry.INSTANCE.getEPackage(RegExAstPackage.eNS_URI);

		// Obtain or create and register package
		RegExAstPackageImpl theRegExAstPackage = (RegExAstPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof RegExAstPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new RegExAstPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CodeSyncPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theRegExAstPackage.createPackageContents();

		// Initialize created meta-data
		theRegExAstPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theRegExAstPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(RegExAstPackage.eNS_URI, theRegExAstPackage);
		return theRegExAstPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRegExAstNode() {
		return regExAstNodeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRegExAstNode_Children() {
		return (EReference)regExAstNodeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRegExAstNode_Parameters() {
		return (EReference)regExAstNodeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_KeyParameter() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_Type() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_CategoryNode() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_Offset() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_Length() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_Added() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_Deleted() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_Template() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRegExAstNode_ChildrenInsertPoints() {
		return (EReference)regExAstNodeEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNode_NextSiblingInsertPoint() {
		return (EAttribute)regExAstNodeEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getStringToIntegerEntry() {
		return stringToIntegerEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringToIntegerEntry_Key() {
		return (EAttribute)stringToIntegerEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getStringToIntegerEntry_Value() {
		return (EAttribute)stringToIntegerEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRegExAstNodeParameter() {
		return regExAstNodeParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNodeParameter_Name() {
		return (EAttribute)regExAstNodeParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNodeParameter_Value() {
		return (EAttribute)regExAstNodeParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNodeParameter_Offset() {
		return (EAttribute)regExAstNodeParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstNodeParameter_Length() {
		return (EAttribute)regExAstNodeParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRegExAstCodeSyncElement() {
		return regExAstCodeSyncElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstCodeSyncElement_Template() {
		return (EAttribute)regExAstCodeSyncElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRegExAstCacheElement() {
		return regExAstCacheElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRegExAstCacheElement_Parameters() {
		return (EReference)regExAstCacheElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstCacheElement_KeyParameter() {
		return (EAttribute)regExAstCacheElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRegExAstCacheElement_CategoryNode() {
		return (EAttribute)regExAstCacheElementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RegExAstFactory getRegExAstFactory() {
		return (RegExAstFactory)getEFactoryInstance();
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
		regExAstNodeEClass = createEClass(REG_EX_AST_NODE);
		createEReference(regExAstNodeEClass, REG_EX_AST_NODE__CHILDREN);
		createEReference(regExAstNodeEClass, REG_EX_AST_NODE__PARAMETERS);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__KEY_PARAMETER);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__TYPE);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__CATEGORY_NODE);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__OFFSET);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__LENGTH);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__ADDED);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__DELETED);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__TEMPLATE);
		createEReference(regExAstNodeEClass, REG_EX_AST_NODE__CHILDREN_INSERT_POINTS);
		createEAttribute(regExAstNodeEClass, REG_EX_AST_NODE__NEXT_SIBLING_INSERT_POINT);

		stringToIntegerEntryEClass = createEClass(STRING_TO_INTEGER_ENTRY);
		createEAttribute(stringToIntegerEntryEClass, STRING_TO_INTEGER_ENTRY__KEY);
		createEAttribute(stringToIntegerEntryEClass, STRING_TO_INTEGER_ENTRY__VALUE);

		regExAstNodeParameterEClass = createEClass(REG_EX_AST_NODE_PARAMETER);
		createEAttribute(regExAstNodeParameterEClass, REG_EX_AST_NODE_PARAMETER__NAME);
		createEAttribute(regExAstNodeParameterEClass, REG_EX_AST_NODE_PARAMETER__VALUE);
		createEAttribute(regExAstNodeParameterEClass, REG_EX_AST_NODE_PARAMETER__OFFSET);
		createEAttribute(regExAstNodeParameterEClass, REG_EX_AST_NODE_PARAMETER__LENGTH);

		regExAstCodeSyncElementEClass = createEClass(REG_EX_AST_CODE_SYNC_ELEMENT);
		createEAttribute(regExAstCodeSyncElementEClass, REG_EX_AST_CODE_SYNC_ELEMENT__TEMPLATE);

		regExAstCacheElementEClass = createEClass(REG_EX_AST_CACHE_ELEMENT);
		createEReference(regExAstCacheElementEClass, REG_EX_AST_CACHE_ELEMENT__PARAMETERS);
		createEAttribute(regExAstCacheElementEClass, REG_EX_AST_CACHE_ELEMENT__KEY_PARAMETER);
		createEAttribute(regExAstCacheElementEClass, REG_EX_AST_CACHE_ELEMENT__CATEGORY_NODE);
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

		// Obtain other dependent packages
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);
		CodeSyncPackage theCodeSyncPackage = (CodeSyncPackage)EPackage.Registry.INSTANCE.getEPackage(CodeSyncPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		regExAstCodeSyncElementEClass.getESuperTypes().add(theCodeSyncPackage.getCodeSyncElement());
		regExAstCacheElementEClass.getESuperTypes().add(theCodeSyncPackage.getAstCacheElement());

		// Initialize classes and features; add operations and parameters
		initEClass(regExAstNodeEClass, RegExAstNode.class, "RegExAstNode", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRegExAstNode_Children(), this.getRegExAstNode(), null, "children", null, 0, -1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRegExAstNode_Parameters(), this.getRegExAstNodeParameter(), null, "parameters", null, 0, -1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_KeyParameter(), ecorePackage.getEString(), "keyParameter", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_Type(), ecorePackage.getEString(), "type", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_CategoryNode(), ecorePackage.getEBoolean(), "categoryNode", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_Offset(), ecorePackage.getEInt(), "offset", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_Length(), ecorePackage.getEInt(), "length", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_Added(), theEcorePackage.getEBoolean(), "added", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_Deleted(), theEcorePackage.getEBoolean(), "deleted", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_Template(), theEcorePackage.getEString(), "template", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRegExAstNode_ChildrenInsertPoints(), this.getStringToIntegerEntry(), null, "childrenInsertPoints", null, 0, -1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNode_NextSiblingInsertPoint(), theEcorePackage.getEInt(), "nextSiblingInsertPoint", null, 0, 1, RegExAstNode.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(stringToIntegerEntryEClass, Map.Entry.class, "StringToIntegerEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getStringToIntegerEntry_Key(), theEcorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getStringToIntegerEntry_Value(), theEcorePackage.getEIntegerObject(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(regExAstNodeParameterEClass, RegExAstNodeParameter.class, "RegExAstNodeParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRegExAstNodeParameter_Name(), ecorePackage.getEString(), "name", null, 0, 1, RegExAstNodeParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNodeParameter_Value(), ecorePackage.getEString(), "value", null, 0, 1, RegExAstNodeParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNodeParameter_Offset(), ecorePackage.getEInt(), "offset", null, 0, 1, RegExAstNodeParameter.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstNodeParameter_Length(), ecorePackage.getEInt(), "length", null, 0, 1, RegExAstNodeParameter.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(regExAstCodeSyncElementEClass, RegExAstCodeSyncElement.class, "RegExAstCodeSyncElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRegExAstCodeSyncElement_Template(), theEcorePackage.getEString(), "template", null, 0, 1, RegExAstCodeSyncElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(regExAstCacheElementEClass, RegExAstCacheElement.class, "RegExAstCacheElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRegExAstCacheElement_Parameters(), this.getRegExAstNodeParameter(), null, "parameters", null, 0, -1, RegExAstCacheElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstCacheElement_KeyParameter(), ecorePackage.getEString(), "keyParameter", null, 0, 1, RegExAstCacheElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRegExAstCacheElement_CategoryNode(), ecorePackage.getEBoolean(), "categoryNode", null, 0, 1, RegExAstCacheElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //RegExAstPackageImpl
