/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.codesync;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mind Map Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.MindMapElement#getIcons <em>Icons</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getMindMapElement()
 * @model
 * @generated
 */
public interface MindMapElement extends CodeSyncElement {
	/**
	 * Returns the value of the '<em><b>Icons</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Icons</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Icons</em>' attribute list.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getMindMapElement_Icons()
	 * @model unique="false"
	 * @generated
	 */
	EList<String> getIcons();

} // MindMapElement
