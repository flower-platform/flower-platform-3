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
 * A representation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.astcache.code.Operation#getParameters <em>Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getOperation()
 * @model
 * @generated
 */
public interface Operation extends AstCacheElement, ModifiableElement, DocumentableElement, TypedElement {

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link com.crispico.flower.mp.model.astcache.code.Parameter}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference list.
	 * @see com.crispico.flower.mp.model.astcache.code.AstCacheCodePackage#getOperation_Parameters()
	 * @model containment="true"
	 * @generated
	 */
	EList<Parameter> getParameters();
} // Operation
