/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.crispico.flower.mp.model.codesync;

import java.io.Serializable;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isSynchronized <em>Synchronized</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isChildrenSynchronized <em>Children Synchronized</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isAdded <em>Added</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isDeleted <em>Deleted</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getStatusFlags <em>Status Flags</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getName <em>Name</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getType <em>Type</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getAstCacheElement <em>Ast Cache Element</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getChildren <em>Children</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getFeatureChanges <em>Feature Changes</em>}</li>
 *   <li>{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getRelations <em>Relations</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement()
 * @model
 * @generated
 */
public interface CodeSyncElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Synchronized</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Synchronized</em>' attribute.
	 * @see #setSynchronized(boolean)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_Synchronized()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	boolean isSynchronized();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isSynchronized <em>Synchronized</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Synchronized</em>' attribute.
	 * @see #isSynchronized()
	 * @generated
	 */
	void setSynchronized(boolean value);

	/**
	 * Returns the value of the '<em><b>Children Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children Synchronized</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children Synchronized</em>' attribute.
	 * @see #setChildrenSynchronized(boolean)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_ChildrenSynchronized()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	boolean isChildrenSynchronized();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isChildrenSynchronized <em>Children Synchronized</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Children Synchronized</em>' attribute.
	 * @see #isChildrenSynchronized()
	 * @generated
	 */
	void setChildrenSynchronized(boolean value);

	/**
	 * Returns the value of the '<em><b>Added</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Added</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Added</em>' attribute.
	 * @see #setAdded(boolean)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_Added()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	boolean isAdded();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isAdded <em>Added</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Added</em>' attribute.
	 * @see #isAdded()
	 * @generated
	 */
	void setAdded(boolean value);

	/**
	 * Returns the value of the '<em><b>Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Deleted</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Deleted</em>' attribute.
	 * @see #setDeleted(boolean)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_Deleted()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
	boolean isDeleted();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isDeleted <em>Deleted</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Deleted</em>' attribute.
	 * @see #isDeleted()
	 * @generated
	 */
	void setDeleted(boolean value);

	/**
	 * Returns the value of the '<em><b>Status Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Status Flags</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status Flags</em>' attribute.
	 * @see #setStatusFlags(int)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_StatusFlags()
	 * @model
	 * @generated
	 */
	int getStatusFlags();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getStatusFlags <em>Status Flags</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status Flags</em>' attribute.
	 * @see #getStatusFlags()
	 * @generated
	 */
	void setStatusFlags(int value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Ast Cache Element</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.crispico.flower.mp.model.codesync.AstCacheElement#getCodeSyncElement <em>Code Sync Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ast Cache Element</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ast Cache Element</em>' reference.
	 * @see #setAstCacheElement(AstCacheElement)
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_AstCacheElement()
	 * @see com.crispico.flower.mp.model.codesync.AstCacheElement#getCodeSyncElement
	 * @model opposite="codeSyncElement"
	 * @generated
	 */
	AstCacheElement getAstCacheElement();

	/**
	 * Sets the value of the '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getAstCacheElement <em>Ast Cache Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ast Cache Element</em>' reference.
	 * @see #getAstCacheElement()
	 * @generated
	 */
	void setAstCacheElement(AstCacheElement value);

	/**
	 * Returns the value of the '<em><b>Children</b></em>' containment reference list.
	 * The list contents are of type {@link com.crispico.flower.mp.model.codesync.CodeSyncElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Children</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Children</em>' containment reference list.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_Children()
	 * @model containment="true"
	 * @generated
	 */
	EList<CodeSyncElement> getChildren();

	/**
	 * Returns the value of the '<em><b>Feature Changes</b></em>' map.
	 * The key is of type {@link org.eclipse.emf.ecore.EStructuralFeature},
	 * and the value is of type {@link com.crispico.flower.mp.model.codesync.FeatureChange},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Feature Changes</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Feature Changes</em>' map.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_FeatureChanges()
	 * @model mapType="com.crispico.flower.mp.model.codesync.EStructuralFeatureToFeatureChangeEntry<org.eclipse.emf.ecore.EStructuralFeature, com.crispico.flower.mp.model.codesync.FeatureChange>"
	 * @generated
	 */
	EMap<EStructuralFeature, FeatureChange> getFeatureChanges();

	/**
	 * Returns the value of the '<em><b>Relations</b></em>' containment reference list.
	 * The list contents are of type {@link com.crispico.flower.mp.model.codesync.Relation}.
	 * It is bidirectional and its opposite is '{@link com.crispico.flower.mp.model.codesync.Relation#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relations</em>' containment reference list.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncPackage#getCodeSyncElement_Relations()
	 * @see com.crispico.flower.mp.model.codesync.Relation#getSource
	 * @model opposite="source" containment="true"
	 * @generated
	 */
	EList<Relation> getRelations();

} // CodeSyncElement