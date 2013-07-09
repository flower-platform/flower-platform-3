/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package astcache.wiki.impl;

import astcache.wiki.FlowerBlock;
import astcache.wiki.Page;
import astcache.wiki.WikiFactory;
import astcache.wiki.WikiPackage;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class WikiPackageImpl extends EPackageImpl implements WikiPackage {
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
	 * @see astcache.wiki.WikiPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private WikiPackageImpl() {
		super(eNS_URI, WikiFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link WikiPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static WikiPackage init() {
		if (isInited) return (WikiPackage)EPackage.Registry.INSTANCE.getEPackage(WikiPackage.eNS_URI);

		// Obtain or create and register package
		WikiPackageImpl theWikiPackage = (WikiPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof WikiPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new WikiPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		CodeSyncPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theWikiPackage.createPackageContents();

		// Initialize created meta-data
		theWikiPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theWikiPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(WikiPackage.eNS_URI, theWikiPackage);
		return theWikiPackage;
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
	public WikiFactory getWikiFactory() {
		return (WikiFactory)getEFactoryInstance();
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

		// Add supertypes to classes
		pageEClass.getESuperTypes().add(theCodeSyncPackage.getAstCacheElement());
		flowerBlockEClass.getESuperTypes().add(theCodeSyncPackage.getAstCacheElement());

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

		// Create resource
		createResource(eNS_URI);
	}

} //WikiPackageImpl
