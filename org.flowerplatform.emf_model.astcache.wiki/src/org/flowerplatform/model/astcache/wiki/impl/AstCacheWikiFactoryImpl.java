/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model.astcache.wiki.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.flowerplatform.model.astcache.wiki.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AstCacheWikiFactoryImpl extends EFactoryImpl implements AstCacheWikiFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AstCacheWikiFactory init() {
		try {
			AstCacheWikiFactory theAstCacheWikiFactory = (AstCacheWikiFactory)EPackage.Registry.INSTANCE.getEFactory("http://www.flower-platform.com/xmi/astcache_wiki_1.0.0"); 
			if (theAstCacheWikiFactory != null) {
				return theAstCacheWikiFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AstCacheWikiFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstCacheWikiFactoryImpl() {
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
			case AstCacheWikiPackage.PAGE: return createPage();
			case AstCacheWikiPackage.FLOWER_BLOCK: return createFlowerBlock();
			case AstCacheWikiPackage.NODE_WITH_ORIGINAL_FORMAT: return createNodeWithOriginalFormat();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Page createPage() {
		PageImpl page = new PageImpl();
		return page;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FlowerBlock createFlowerBlock() {
		FlowerBlockImpl flowerBlock = new FlowerBlockImpl();
		return flowerBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NodeWithOriginalFormat createNodeWithOriginalFormat() {
		NodeWithOriginalFormatImpl nodeWithOriginalFormat = new NodeWithOriginalFormatImpl();
		return nodeWithOriginalFormat;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AstCacheWikiPackage getAstCacheWikiPackage() {
		return (AstCacheWikiPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AstCacheWikiPackage getPackage() {
		return AstCacheWikiPackage.eINSTANCE;
	}

} //AstCacheWikiFactoryImpl
