/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.astcache.code;

import com.crispico.flower.mp.model.codesync.AstCacheElement;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Constant</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.EnumConstant#getArguments <em>Arguments</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getEnumConstant()
 * @model
 * @generated
 */
public interface EnumConstant extends AstCacheElement, ModifiableElement, DocumentableElement {
	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' attribute list.
	 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getEnumConstant_Arguments()
	 * @model
	 * @generated
	 */
	EList<String> getArguments();

} // EnumConstant
