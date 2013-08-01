/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model.astcache.wiki;

import com.crispico.flower.mp.model.codesync.CodeSyncPackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

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
 * @see org.flowerplatform.model.astcache.wiki.AstCacheWikiFactory
 * @model kind="package"
 * @generated
 */
public interface AstCacheWikiPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "wiki";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.flower-platform.com/xmi/astcache_wiki_1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "astcache_wiki";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AstCacheWikiPackage eINSTANCE = org.flowerplatform.model.astcache.wiki.impl.AstCacheWikiPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.flowerplatform.model.astcache.wiki.impl.PageImpl <em>Page</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model.astcache.wiki.impl.PageImpl
	 * @see org.flowerplatform.model.astcache.wiki.impl.AstCacheWikiPackageImpl#getPage()
	 * @generated
	 */
	int PAGE = 0;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Initial Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__INITIAL_CONTENT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Line Delimiter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__LINE_DELIMITER = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Diff</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE__DIFF = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Page</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PAGE_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model.astcache.wiki.impl.FlowerBlockImpl <em>Flower Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model.astcache.wiki.impl.FlowerBlockImpl
	 * @see org.flowerplatform.model.astcache.wiki.impl.AstCacheWikiPackageImpl#getFlowerBlock()
	 * @generated
	 */
	int FLOWER_BLOCK = 1;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWER_BLOCK__CODE_SYNC_ELEMENT = CodeSyncPackage.AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT;

	/**
	 * The feature id for the '<em><b>Content</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWER_BLOCK__CONTENT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Line Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWER_BLOCK__LINE_START = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Line End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWER_BLOCK__LINE_END = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Conflict</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWER_BLOCK__CONFLICT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Flower Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOWER_BLOCK_FEATURE_COUNT = CodeSyncPackage.AST_CACHE_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The meta object id for the '{@link org.flowerplatform.model.astcache.wiki.impl.HeadingImpl <em>Heading</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.flowerplatform.model.astcache.wiki.impl.HeadingImpl
	 * @see org.flowerplatform.model.astcache.wiki.impl.AstCacheWikiPackageImpl#getHeading()
	 * @generated
	 */
	int HEADING = 2;

	/**
	 * The feature id for the '<em><b>Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__SYNCHRONIZED = CodeSyncPackage.CODE_SYNC_ELEMENT__SYNCHRONIZED;

	/**
	 * The feature id for the '<em><b>Children Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__CHILDREN_SYNCHRONIZED = CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED;

	/**
	 * The feature id for the '<em><b>Added</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__ADDED = CodeSyncPackage.CODE_SYNC_ELEMENT__ADDED;

	/**
	 * The feature id for the '<em><b>Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__DELETED = CodeSyncPackage.CODE_SYNC_ELEMENT__DELETED;

	/**
	 * The feature id for the '<em><b>Status Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__STATUS_FLAGS = CodeSyncPackage.CODE_SYNC_ELEMENT__STATUS_FLAGS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__NAME = CodeSyncPackage.CODE_SYNC_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__TYPE = CodeSyncPackage.CODE_SYNC_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Ast Cache Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__AST_CACHE_ELEMENT = CodeSyncPackage.CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__CHILDREN = CodeSyncPackage.CODE_SYNC_ELEMENT__CHILDREN;

	/**
	 * The feature id for the '<em><b>Feature Changes</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__FEATURE_CHANGES = CodeSyncPackage.CODE_SYNC_ELEMENT__FEATURE_CHANGES;

	/**
	 * The feature id for the '<em><b>Original Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING__ORIGINAL_FORMAT = CodeSyncPackage.CODE_SYNC_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Heading</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEADING_FEATURE_COUNT = CodeSyncPackage.CODE_SYNC_ELEMENT_FEATURE_COUNT + 1;


	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model.astcache.wiki.Page <em>Page</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Page</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.Page
	 * @generated
	 */
	EClass getPage();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model.astcache.wiki.Page#getInitialContent <em>Initial Content</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Initial Content</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.Page#getInitialContent()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_InitialContent();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model.astcache.wiki.Page#getLineDelimiter <em>Line Delimiter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Line Delimiter</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.Page#getLineDelimiter()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_LineDelimiter();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model.astcache.wiki.Page#getDiff <em>Diff</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Diff</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.Page#getDiff()
	 * @see #getPage()
	 * @generated
	 */
	EAttribute getPage_Diff();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model.astcache.wiki.FlowerBlock <em>Flower Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Flower Block</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.FlowerBlock
	 * @generated
	 */
	EClass getFlowerBlock();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model.astcache.wiki.FlowerBlock#getContent <em>Content</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Content</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.FlowerBlock#getContent()
	 * @see #getFlowerBlock()
	 * @generated
	 */
	EAttribute getFlowerBlock_Content();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model.astcache.wiki.FlowerBlock#getLineStart <em>Line Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Line Start</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.FlowerBlock#getLineStart()
	 * @see #getFlowerBlock()
	 * @generated
	 */
	EAttribute getFlowerBlock_LineStart();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model.astcache.wiki.FlowerBlock#getLineEnd <em>Line End</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Line End</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.FlowerBlock#getLineEnd()
	 * @see #getFlowerBlock()
	 * @generated
	 */
	EAttribute getFlowerBlock_LineEnd();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model.astcache.wiki.FlowerBlock#isConflict <em>Conflict</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Conflict</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.FlowerBlock#isConflict()
	 * @see #getFlowerBlock()
	 * @generated
	 */
	EAttribute getFlowerBlock_Conflict();

	/**
	 * Returns the meta object for class '{@link org.flowerplatform.model.astcache.wiki.Heading <em>Heading</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Heading</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.Heading
	 * @generated
	 */
	EClass getHeading();

	/**
	 * Returns the meta object for the attribute '{@link org.flowerplatform.model.astcache.wiki.Heading#getOriginalFormat <em>Original Format</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Original Format</em>'.
	 * @see org.flowerplatform.model.astcache.wiki.Heading#getOriginalFormat()
	 * @see #getHeading()
	 * @generated
	 */
	EAttribute getHeading_OriginalFormat();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AstCacheWikiFactory getAstCacheWikiFactory();

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
		 * The meta object literal for the '{@link org.flowerplatform.model.astcache.wiki.impl.PageImpl <em>Page</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model.astcache.wiki.impl.PageImpl
		 * @see org.flowerplatform.model.astcache.wiki.impl.AstCacheWikiPackageImpl#getPage()
		 * @generated
		 */
		EClass PAGE = eINSTANCE.getPage();

		/**
		 * The meta object literal for the '<em><b>Initial Content</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__INITIAL_CONTENT = eINSTANCE.getPage_InitialContent();

		/**
		 * The meta object literal for the '<em><b>Line Delimiter</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__LINE_DELIMITER = eINSTANCE.getPage_LineDelimiter();

		/**
		 * The meta object literal for the '<em><b>Diff</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PAGE__DIFF = eINSTANCE.getPage_Diff();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.model.astcache.wiki.impl.FlowerBlockImpl <em>Flower Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model.astcache.wiki.impl.FlowerBlockImpl
		 * @see org.flowerplatform.model.astcache.wiki.impl.AstCacheWikiPackageImpl#getFlowerBlock()
		 * @generated
		 */
		EClass FLOWER_BLOCK = eINSTANCE.getFlowerBlock();

		/**
		 * The meta object literal for the '<em><b>Content</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLOWER_BLOCK__CONTENT = eINSTANCE.getFlowerBlock_Content();

		/**
		 * The meta object literal for the '<em><b>Line Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLOWER_BLOCK__LINE_START = eINSTANCE.getFlowerBlock_LineStart();

		/**
		 * The meta object literal for the '<em><b>Line End</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLOWER_BLOCK__LINE_END = eINSTANCE.getFlowerBlock_LineEnd();

		/**
		 * The meta object literal for the '<em><b>Conflict</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLOWER_BLOCK__CONFLICT = eINSTANCE.getFlowerBlock_Conflict();

		/**
		 * The meta object literal for the '{@link org.flowerplatform.model.astcache.wiki.impl.HeadingImpl <em>Heading</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.flowerplatform.model.astcache.wiki.impl.HeadingImpl
		 * @see org.flowerplatform.model.astcache.wiki.impl.AstCacheWikiPackageImpl#getHeading()
		 * @generated
		 */
		EClass HEADING = eINSTANCE.getHeading();

		/**
		 * The meta object literal for the '<em><b>Original Format</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HEADING__ORIGINAL_FORMAT = eINSTANCE.getHeading_OriginalFormat();

	}

} //AstCacheWikiPackage
