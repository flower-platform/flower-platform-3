/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.astcache.code;

import org.eclipse.emf.common.util.EList;

import com.crispico.flower.mp.model.codesync.AstCacheElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Class</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.Class#getSuperClasses <em>Super Classes</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.Class#getSuperInterfaces <em>Super Interfaces</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getClass_()
 * @model
 * @generated
 */
public interface Class extends AstCacheElement, ModifiableElement, DocumentableElement {

	/**
	 * Returns the value of the '<em><b>Super Classes</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super Classes</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super Classes</em>' attribute list.
	 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getClass_SuperClasses()
	 * @model
	 * @generated
	 */
	EList<String> getSuperClasses();

	/**
	 * Returns the value of the '<em><b>Super Interfaces</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Super Interfaces</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Super Interfaces</em>' attribute list.
	 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getClass_SuperInterfaces()
	 * @model
	 * @generated
	 */
	EList<String> getSuperInterfaces();
} // Class
