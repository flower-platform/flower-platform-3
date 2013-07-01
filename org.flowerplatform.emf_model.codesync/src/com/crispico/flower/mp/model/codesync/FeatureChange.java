/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.codesync;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.FeatureChange#getOldValue <em>Old Value</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.FeatureChange#getOldValueAsString <em>Old Value As String</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.FeatureChange#getOldValueAsContainmentList <em>Old Value As Containment List</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.FeatureChange#getNewValue <em>New Value</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.FeatureChange#getNewValueAsString <em>New Value As String</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.FeatureChange#getNewValueAsContainmentList <em>New Value As Containment List</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getFeatureChange()
 * @model
 * @generated
 */
public interface FeatureChange extends EObject {
	/**
	 * Returns the value of the '<em><b>Old Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Value</em>' attribute.
	 * @see #setOldValue(Object)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getFeatureChange_OldValue()
	 * @model transient="true" derived="true"
	 * @generated
	 */
	Object getOldValue();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getOldValue <em>Old Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Value</em>' attribute.
	 * @see #getOldValue()
	 * @generated
	 */
	void setOldValue(Object value);

	/**
	 * Returns the value of the '<em><b>Old Value As String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Value As String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Value As String</em>' attribute.
	 * @see #setOldValueAsString(String)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getFeatureChange_OldValueAsString()
	 * @model
	 * @generated
	 */
	String getOldValueAsString();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getOldValueAsString <em>Old Value As String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Old Value As String</em>' attribute.
	 * @see #getOldValueAsString()
	 * @generated
	 */
	void setOldValueAsString(String value);

	/**
	 * Returns the value of the '<em><b>Old Value As Containment List</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Old Value As Containment List</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Old Value As Containment List</em>' containment reference list.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getFeatureChange_OldValueAsContainmentList()
	 * @model containment="true"
	 * @generated
	 */
	EList<EObject> getOldValueAsContainmentList();

	/**
	 * Returns the value of the '<em><b>New Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Value</em>' attribute.
	 * @see #setNewValue(Object)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getFeatureChange_NewValue()
	 * @model transient="true" derived="true"
	 * @generated
	 */
	Object getNewValue();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getNewValue <em>New Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Value</em>' attribute.
	 * @see #getNewValue()
	 * @generated
	 */
	void setNewValue(Object value);

	/**
	 * Returns the value of the '<em><b>New Value As String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Value As String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Value As String</em>' attribute.
	 * @see #setNewValueAsString(String)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getFeatureChange_NewValueAsString()
	 * @model
	 * @generated
	 */
	String getNewValueAsString();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getNewValueAsString <em>New Value As String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Value As String</em>' attribute.
	 * @see #getNewValueAsString()
	 * @generated
	 */
	void setNewValueAsString(String value);

	/**
	 * Returns the value of the '<em><b>New Value As Containment List</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Value As Containment List</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Value As Containment List</em>' containment reference list.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getFeatureChange_NewValueAsContainmentList()
	 * @model containment="true"
	 * @generated
	 */
	EList<EObject> getNewValueAsContainmentList();

} // FeatureChange
