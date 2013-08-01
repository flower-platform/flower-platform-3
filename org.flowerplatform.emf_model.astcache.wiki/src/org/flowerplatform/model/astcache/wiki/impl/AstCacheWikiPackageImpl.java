/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model.astcache.wiki.impl;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.flowerplatform.model.astcache.wiki.AstCacheWikiFactory;
import org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage;
import org.flowerplatform.model.astcache.wiki.FlowerBlock;
import org.flowerplatform.model.astcache.wiki.Heading;
import org.flowerplatform.model.astcache.wiki.Page;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AstCacheWikiPackageImpl extends EPackageImpl implements AstCacheWikiPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass pageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass flowerBlockEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass headingEClass = null;

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
	 * @see org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AstCacheWikiPackageImpl() {
		super(eNS_URI, AstCacheWikiFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link AstCacheWikiPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AstCacheWikiPackage init() {
		if (isInited) return (AstCacheWikiPackage)EPackage.Registry.INSTANCE.getEPackage(AstCacheWikiPackage.eNS_URI);

		// Obtain or create and register package
		AstCacheWikiPackageImpl theAstCacheWikiPackage = (AstCacheWikiPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AstCacheWikiPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AstCacheWikiPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CodeSyncPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theAstCacheWikiPackage.createPackageContents();

		// Initialize created meta-data
		theAstCacheWikiPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAstCacheWikiPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AstCacheWikiPackage.eNS_URI, theAstCacheWikiPackage);
		return theAstCacheWikiPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPage() {
		return pageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPage_InitialContent() {
		return (EAttribute)pageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPage_LineDelimiter() {
		return (EAttribute)pageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPage_Diff() {
		return (EAttribute)pageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFlowerBlock() {
		return flowerBlockEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFlowerBlock_Content() {
		return (EAttribute)flowerBlockEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFlowerBlock_LineStart() {
		return (EAttribute)flowerBlockEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFlowerBlock_LineEnd() {
		return (EAttribute)flowerBlockEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFlowerBlock_Conflict() {
		return (EAttribute)flowerBlockEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getHeading() {
		return headingEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getHeading_OriginalFormat() {
		return (EAttribute)headingEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstCacheWikiFactory getAstCacheWikiFactory() {
		return (AstCacheWikiFactory)getEFactoryInstance();
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
		pageEClass = createEClass(PAGE);
		createEAttribute(pageEClass, PAGE__INITIAL_CONTENT);
		createEAttribute(pageEClass, PAGE__LINE_DELIMITER);
		createEAttribute(pageEClass, PAGE__DIFF);

		flowerBlockEClass = createEClass(FLOWER_BLOCK);
		createEAttribute(flowerBlockEClass, FLOWER_BLOCK__CONTENT);
		createEAttribute(flowerBlockEClass, FLOWER_BLOCK__LINE_START);
		createEAttribute(flowerBlockEClass, FLOWER_BLOCK__LINE_END);
		createEAttribute(flowerBlockEClass, FLOWER_BLOCK__CONFLICT);

		headingEClass = createEClass(HEADING);
		createEAttribute(headingEClass, HEADING__ORIGINAL_FORMAT);
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
		CodeSyncPackage theCodeSyncPackage = (CodeSyncPackage)EPackage.Registry.INSTANCE.getEPackage(CodeSyncPackage.eNS_URI);
		EcorePackage theEcorePackage = (EcorePackage)EPackage.Registry.INSTANCE.getEPackage(EcorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		pageEClass.getESuperTypes().add(theCodeSyncPackage.getAstCacheElement());
		flowerBlockEClass.getESuperTypes().add(theCodeSyncPackage.getAstCacheElement());
		headingEClass.getESuperTypes().add(theCodeSyncPackage.getCodeSyncElement());

		// Initialize classes and features; add operations and parameters
		initEClass(pageEClass, Page.class, "Page", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPage_InitialContent(), ecorePackage.getEString(), "initialContent", null, 0, 1, Page.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPage_LineDelimiter(), ecorePackage.getEString(), "lineDelimiter", null, 0, 1, Page.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPage_Diff(), ecorePackage.getEJavaObject(), "diff", null, 0, 1, Page.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(flowerBlockEClass, FlowerBlock.class, "FlowerBlock", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFlowerBlock_Content(), ecorePackage.getEString(), "content", null, 0, 1, FlowerBlock.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFlowerBlock_LineStart(), ecorePackage.getEInt(), "lineStart", null, 0, 1, FlowerBlock.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFlowerBlock_LineEnd(), ecorePackage.getEInt(), "lineEnd", null, 0, 1, FlowerBlock.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFlowerBlock_Conflict(), ecorePackage.getEBoolean(), "conflict", null, 0, 1, FlowerBlock.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(headingEClass, Heading.class, "Heading", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHeading_OriginalFormat(), theEcorePackage.getEString(), "originalFormat", null, 0, 1, Heading.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //AstCacheWikiPackageImpl
