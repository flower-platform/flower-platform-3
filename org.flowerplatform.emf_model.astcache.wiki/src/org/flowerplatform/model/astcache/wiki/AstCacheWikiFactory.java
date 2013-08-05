/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model.astcache.wiki;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage
 * @generated
 */
public interface AstCacheWikiFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AstCacheWikiFactory eINSTANCE = org.flowerplatform.model.astcache.wiki.impl.AstCacheWikiFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Page</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Page</em>'.
	 * @generated
	 */
	Page createPage();

	/**
	 * Returns a new object of class '<em>Flower Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Flower Block</em>'.
	 * @generated
	 */
	FlowerBlock createFlowerBlock();

	/**
	 * Returns a new object of class '<em>Node With Original Format</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Node With Original Format</em>'.
	 * @generated
	 */
	NodeWithOriginalFormat createNodeWithOriginalFormat();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AstCacheWikiPackage getAstCacheWikiPackage();

} //AstCacheWikiFactory
