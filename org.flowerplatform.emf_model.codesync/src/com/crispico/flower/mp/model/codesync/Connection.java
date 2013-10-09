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
 * A representation of the model object '<em><b>Connection</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.Connection#getSource <em>Source</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.Connection#getTarget <em>Target</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getConnection()
 * @model
 * @generated
 */
public interface Connection extends EObject {
	/**
	 * Returns the value of the '<em><b>Source</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getConnections <em>Connections</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source</em>' container reference.
	 * @see #setSource(CodeSyncElement)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getConnection_Source()
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getConnections
	 * @model opposite="connections" transient="false"
	 * @generated
	 */
	CodeSyncElement getSource();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.Connection#getSource <em>Source</em>}' container reference.
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
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getConnection_Target()
	 * @model
	 * @generated
	 */
	CodeSyncElement getTarget();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.Connection#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(CodeSyncElement value);

} // Connection
