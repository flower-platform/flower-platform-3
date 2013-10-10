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
 *   <li>{@link com.crispico.flower.mp.model.codesync.MindMapElement#getMinWidth <em>Min Width</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.MindMapElement#getMaxWidth <em>Max Width</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Min Width</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Width</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Width</em>' attribute.
	 * @see #setMinWidth(Long)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getMindMapElement_MinWidth()
	 * @model default="1"
	 * @generated
	 */
	Long getMinWidth();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.MindMapElement#getMinWidth <em>Min Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Width</em>' attribute.
	 * @see #getMinWidth()
	 * @generated
	 */
	void setMinWidth(Long value);

	/**
	 * Returns the value of the '<em><b>Max Width</b></em>' attribute.
	 * The default value is <code>"600"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Width</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Width</em>' attribute.
	 * @see #setMaxWidth(Long)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getMindMapElement_MaxWidth()
	 * @model default="600"
	 * @generated
	 */
	Long getMaxWidth();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.MindMapElement#getMaxWidth <em>Max Width</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Width</em>' attribute.
	 * @see #getMaxWidth()
	 * @generated
	 */
	void setMaxWidth(Long value);

} // MindMapElement
