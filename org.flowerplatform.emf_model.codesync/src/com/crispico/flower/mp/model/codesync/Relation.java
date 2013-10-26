/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.codesync;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.Relation#getSource <em>Source</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.Relation#getTarget <em>Target</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.Relation#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getRelation()
 * @model
 * @generated
 */
public interface Relation extends EObject {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getRelations <em>Relations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' container reference.
	 * @see #setSource(CodeSyncElement)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getRelation_Source()
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getRelations
	 * @model opposite="relations" transient="false"
	 * @generated
	 */
	CodeSyncElement getSource();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.Relation#getSource <em>Source</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' container reference.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(CodeSyncElement value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(CodeSyncElement)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getRelation_Target()
	 * @model
	 * @generated
	 */
	CodeSyncElement getTarget();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.Relation#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(CodeSyncElement value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getRelation_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.Relation#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

} // Relation
