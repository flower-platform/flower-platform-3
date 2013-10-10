/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.flowerplatform.model.astcache.wiki;

import com.crispico.flower.mp.model.codesync.CodeSyncElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Node With Original Format</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.flowerplatform.model.astcache.wiki.NodeWithOriginalFormat#getOriginalFormat <em>Original Format</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage#getNodeWithOriginalFormat()
 * @model
 * @generated
 */
public interface NodeWithOriginalFormat extends CodeSyncElement {
	/**
	 * Returns the value of the '<em><b>Original Format</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Format</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Format</em>' attribute.
	 * @see #setOriginalFormat(String)
	 * @see org.flowerplatform.model.astcache.wiki.AstCacheWikiPackage#getNodeWithOriginalFormat_OriginalFormat()
	 * @model
	 * @generated
	 */
	String getOriginalFormat();

	/**
	 * Sets the value of the '{@link org.flowerplatform.model.astcache.wiki.NodeWithOriginalFormat#getOriginalFormat <em>Original Format</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Format</em>' attribute.
	 * @see #getOriginalFormat()
	 * @generated
	 */
	void setOriginalFormat(String value);

} // NodeWithOriginalFormat
