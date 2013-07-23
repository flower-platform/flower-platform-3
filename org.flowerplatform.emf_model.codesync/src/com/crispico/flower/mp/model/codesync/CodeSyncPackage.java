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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.crispico.flower.mp.model.codesync.CodeSyncFactory
 * @model kind="package"
 * @generated
 */
public interface CodeSyncPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "codesync";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.flower-platform.com/xmi/codesync_1.0.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "codesync";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CodeSyncPackage eINSTANCE = com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl <em>Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getCodeSyncElement()
	 * @generated
	 */
	int CODE_SYNC_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__SYNCHRONIZED = 0;

	/**
	 * The feature id for the '<em><b>Children Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED = 1;

	/**
	 * The feature id for the '<em><b>Added</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__ADDED = 2;

	/**
	 * The feature id for the '<em><b>Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__DELETED = 3;

	/**
	 * The feature id for the '<em><b>Status Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__STATUS_FLAGS = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__NAME = 5;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__TYPE = 6;

	/**
	 * The feature id for the '<em><b>Ast Cache Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT = 7;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__CHILDREN = 8;

	/**
	 * The feature id for the '<em><b>Feature Changes</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT__FEATURE_CHANGES = 9;

	/**
	 * The number of structural features of the '<em>Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ELEMENT_FEATURE_COUNT = 10;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.codesync.impl.EStructuralFeatureToFeatureChangeEntryImpl <em>EStructural Feature To Feature Change Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.codesync.impl.EStructuralFeatureToFeatureChangeEntryImpl
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getEStructuralFeatureToFeatureChangeEntry()
	 * @generated
	 */
	int ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EStructural Feature To Feature Change Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl <em>Feature Change</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getFeatureChange()
	 * @generated
	 */
	int FEATURE_CHANGE = 2;

	/**
	 * The feature id for the '<em><b>Old Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CHANGE__OLD_VALUE = 0;

	/**
	 * The feature id for the '<em><b>Old Value As String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CHANGE__OLD_VALUE_AS_STRING = 1;

	/**
	 * The feature id for the '<em><b>Old Value As Containment List</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST = 2;

	/**
	 * The feature id for the '<em><b>New Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CHANGE__NEW_VALUE = 3;

	/**
	 * The feature id for the '<em><b>New Value As String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CHANGE__NEW_VALUE_AS_STRING = 4;

	/**
	 * The feature id for the '<em><b>New Value As Containment List</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST = 5;

	/**
	 * The number of structural features of the '<em>Feature Change</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CHANGE_FEATURE_COUNT = 6;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncRootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncRootImpl
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getCodeSyncRoot()
	 * @generated
	 */
	int CODE_SYNC_ROOT = 3;

	/**
	 * The feature id for the '<em><b>Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__SYNCHRONIZED = CODE_SYNC_ELEMENT__SYNCHRONIZED;

	/**
	 * The feature id for the '<em><b>Children Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__CHILDREN_SYNCHRONIZED = CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED;

	/**
	 * The feature id for the '<em><b>Added</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__ADDED = CODE_SYNC_ELEMENT__ADDED;

	/**
	 * The feature id for the '<em><b>Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__DELETED = CODE_SYNC_ELEMENT__DELETED;

	/**
	 * The feature id for the '<em><b>Status Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__STATUS_FLAGS = CODE_SYNC_ELEMENT__STATUS_FLAGS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__NAME = CODE_SYNC_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__TYPE = CODE_SYNC_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Ast Cache Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__AST_CACHE_ELEMENT = CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__CHILDREN = CODE_SYNC_ELEMENT__CHILDREN;

	/**
	 * The feature id for the '<em><b>Feature Changes</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT__FEATURE_CHANGES = CODE_SYNC_ELEMENT__FEATURE_CHANGES;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CODE_SYNC_ROOT_FEATURE_COUNT = CODE_SYNC_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link java.io.Serializable <em>Serializable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.io.Serializable
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getSerializable()
	 * @generated
	 */
	int SERIALIZABLE = 5;

	/**
	 * The number of structural features of the '<em>Serializable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SERIALIZABLE_FEATURE_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl <em>Ast Cache Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getAstCacheElement()
	 * @generated
	 */
	int AST_CACHE_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Code Sync Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT = SERIALIZABLE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Ast Cache Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AST_CACHE_ELEMENT_FEATURE_COUNT = SERIALIZABLE_FEATURE_COUNT + 1;


	/**
	 * The meta object id for the '{@link com.crispico.flower.mp.model.codesync.impl.ScenarioElementImpl <em>Scenario Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.crispico.flower.mp.model.codesync.impl.ScenarioElementImpl
	 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getScenarioElement()
	 * @generated
	 */
	int SCENARIO_ELEMENT = 6;

	/**
	 * The feature id for the '<em><b>Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__SYNCHRONIZED = CODE_SYNC_ELEMENT__SYNCHRONIZED;

	/**
	 * The feature id for the '<em><b>Children Synchronized</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__CHILDREN_SYNCHRONIZED = CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED;

	/**
	 * The feature id for the '<em><b>Added</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__ADDED = CODE_SYNC_ELEMENT__ADDED;

	/**
	 * The feature id for the '<em><b>Deleted</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__DELETED = CODE_SYNC_ELEMENT__DELETED;

	/**
	 * The feature id for the '<em><b>Status Flags</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__STATUS_FLAGS = CODE_SYNC_ELEMENT__STATUS_FLAGS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__NAME = CODE_SYNC_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__TYPE = CODE_SYNC_ELEMENT__TYPE;

	/**
	 * The feature id for the '<em><b>Ast Cache Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__AST_CACHE_ELEMENT = CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT;

	/**
	 * The feature id for the '<em><b>Children</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__CHILDREN = CODE_SYNC_ELEMENT__CHILDREN;

	/**
	 * The feature id for the '<em><b>Feature Changes</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__FEATURE_CHANGES = CODE_SYNC_ELEMENT__FEATURE_CHANGES;

	/**
	 * The feature id for the '<em><b>Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__NUMBER = CODE_SYNC_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Interaction</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__INTERACTION = CODE_SYNC_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT__COMMENT = CODE_SYNC_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Scenario Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCENARIO_ELEMENT_FEATURE_COUNT = CODE_SYNC_ELEMENT_FEATURE_COUNT + 3;


	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement <em>Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Element</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement
	 * @generated
	 */
	EClass getCodeSyncElement();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isSynchronized <em>Synchronized</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Synchronized</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#isSynchronized()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EAttribute getCodeSyncElement_Synchronized();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isChildrenSynchronized <em>Children Synchronized</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Children Synchronized</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#isChildrenSynchronized()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EAttribute getCodeSyncElement_ChildrenSynchronized();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isAdded <em>Added</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Added</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#isAdded()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EAttribute getCodeSyncElement_Added();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#isDeleted <em>Deleted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Deleted</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#isDeleted()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EAttribute getCodeSyncElement_Deleted();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getStatusFlags <em>Status Flags</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Status Flags</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getStatusFlags()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EAttribute getCodeSyncElement_StatusFlags();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getName()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EAttribute getCodeSyncElement_Name();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getType()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EAttribute getCodeSyncElement_Type();

	/**
	 * Returns the meta object for the reference '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getAstCacheElement <em>Ast Cache Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Ast Cache Element</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getAstCacheElement()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EReference getCodeSyncElement_AstCacheElement();

	/**
	 * Returns the meta object for the containment reference list '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Children</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getChildren()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EReference getCodeSyncElement_Children();

	/**
	 * Returns the meta object for the map '{@link com.crispico.flower.mp.model.codesync.CodeSyncElement#getFeatureChanges <em>Feature Changes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Feature Changes</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncElement#getFeatureChanges()
	 * @see #getCodeSyncElement()
	 * @generated
	 */
	EReference getCodeSyncElement_FeatureChanges();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EStructural Feature To Feature Change Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EStructural Feature To Feature Change Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="org.eclipse.emf.ecore.EStructuralFeature"
	 *        valueType="com.crispico.flower.mp.model.codesync.FeatureChange" valueContainment="true"
	 * @generated
	 */
	EClass getEStructuralFeatureToFeatureChangeEntry();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStructuralFeatureToFeatureChangeEntry()
	 * @generated
	 */
	EReference getEStructuralFeatureToFeatureChangeEntry_Key();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEStructuralFeatureToFeatureChangeEntry()
	 * @generated
	 */
	EReference getEStructuralFeatureToFeatureChangeEntry_Value();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.codesync.FeatureChange <em>Feature Change</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Change</em>'.
	 * @see com.crispico.flower.mp.model.codesync.FeatureChange
	 * @generated
	 */
	EClass getFeatureChange();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getOldValue <em>Old Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Old Value</em>'.
	 * @see com.crispico.flower.mp.model.codesync.FeatureChange#getOldValue()
	 * @see #getFeatureChange()
	 * @generated
	 */
	EAttribute getFeatureChange_OldValue();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getOldValueAsString <em>Old Value As String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Old Value As String</em>'.
	 * @see com.crispico.flower.mp.model.codesync.FeatureChange#getOldValueAsString()
	 * @see #getFeatureChange()
	 * @generated
	 */
	EAttribute getFeatureChange_OldValueAsString();

	/**
	 * Returns the meta object for the containment reference list '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getOldValueAsContainmentList <em>Old Value As Containment List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Old Value As Containment List</em>'.
	 * @see com.crispico.flower.mp.model.codesync.FeatureChange#getOldValueAsContainmentList()
	 * @see #getFeatureChange()
	 * @generated
	 */
	EReference getFeatureChange_OldValueAsContainmentList();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getNewValue <em>New Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>New Value</em>'.
	 * @see com.crispico.flower.mp.model.codesync.FeatureChange#getNewValue()
	 * @see #getFeatureChange()
	 * @generated
	 */
	EAttribute getFeatureChange_NewValue();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getNewValueAsString <em>New Value As String</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>New Value As String</em>'.
	 * @see com.crispico.flower.mp.model.codesync.FeatureChange#getNewValueAsString()
	 * @see #getFeatureChange()
	 * @generated
	 */
	EAttribute getFeatureChange_NewValueAsString();

	/**
	 * Returns the meta object for the containment reference list '{@link com.crispico.flower.mp.model.codesync.FeatureChange#getNewValueAsContainmentList <em>New Value As Containment List</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>New Value As Containment List</em>'.
	 * @see com.crispico.flower.mp.model.codesync.FeatureChange#getNewValueAsContainmentList()
	 * @see #getFeatureChange()
	 * @generated
	 */
	EReference getFeatureChange_NewValueAsContainmentList();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.codesync.CodeSyncRoot <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see com.crispico.flower.mp.model.codesync.CodeSyncRoot
	 * @generated
	 */
	EClass getCodeSyncRoot();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.codesync.AstCacheElement <em>Ast Cache Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ast Cache Element</em>'.
	 * @see com.crispico.flower.mp.model.codesync.AstCacheElement
	 * @generated
	 */
	EClass getAstCacheElement();

	/**
	 * Returns the meta object for the reference '{@link com.crispico.flower.mp.model.codesync.AstCacheElement#getCodeSyncElement <em>Code Sync Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Code Sync Element</em>'.
	 * @see com.crispico.flower.mp.model.codesync.AstCacheElement#getCodeSyncElement()
	 * @see #getAstCacheElement()
	 * @generated
	 */
	EReference getAstCacheElement_CodeSyncElement();

	/**
	 * Returns the meta object for class '{@link java.io.Serializable <em>Serializable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Serializable</em>'.
	 * @see java.io.Serializable
	 * @model instanceClass="java.io.Serializable"
	 * @generated
	 */
	EClass getSerializable();

	/**
	 * Returns the meta object for class '{@link com.crispico.flower.mp.model.codesync.ScenarioElement <em>Scenario Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scenario Element</em>'.
	 * @see com.crispico.flower.mp.model.codesync.ScenarioElement
	 * @generated
	 */
	EClass getScenarioElement();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getNumber <em>Number</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number</em>'.
	 * @see com.crispico.flower.mp.model.codesync.ScenarioElement#getNumber()
	 * @see #getScenarioElement()
	 * @generated
	 */
	EAttribute getScenarioElement_Number();

	/**
	 * Returns the meta object for the reference '{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getInteraction <em>Interaction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Interaction</em>'.
	 * @see com.crispico.flower.mp.model.codesync.ScenarioElement#getInteraction()
	 * @see #getScenarioElement()
	 * @generated
	 */
	EReference getScenarioElement_Interaction();

	/**
	 * Returns the meta object for the attribute '{@link com.crispico.flower.mp.model.codesync.ScenarioElement#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see com.crispico.flower.mp.model.codesync.ScenarioElement#getComment()
	 * @see #getScenarioElement()
	 * @generated
	 */
	EAttribute getScenarioElement_Comment();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CodeSyncFactory getCodeSyncFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl <em>Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncElementImpl
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getCodeSyncElement()
		 * @generated
		 */
		EClass CODE_SYNC_ELEMENT = eINSTANCE.getCodeSyncElement();

		/**
		 * The meta object literal for the '<em><b>Synchronized</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT__SYNCHRONIZED = eINSTANCE.getCodeSyncElement_Synchronized();

		/**
		 * The meta object literal for the '<em><b>Children Synchronized</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT__CHILDREN_SYNCHRONIZED = eINSTANCE.getCodeSyncElement_ChildrenSynchronized();

		/**
		 * The meta object literal for the '<em><b>Added</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT__ADDED = eINSTANCE.getCodeSyncElement_Added();

		/**
		 * The meta object literal for the '<em><b>Deleted</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT__DELETED = eINSTANCE.getCodeSyncElement_Deleted();

		/**
		 * The meta object literal for the '<em><b>Status Flags</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT__STATUS_FLAGS = eINSTANCE.getCodeSyncElement_StatusFlags();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT__NAME = eINSTANCE.getCodeSyncElement_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CODE_SYNC_ELEMENT__TYPE = eINSTANCE.getCodeSyncElement_Type();

		/**
		 * The meta object literal for the '<em><b>Ast Cache Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CODE_SYNC_ELEMENT__AST_CACHE_ELEMENT = eINSTANCE.getCodeSyncElement_AstCacheElement();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CODE_SYNC_ELEMENT__CHILDREN = eINSTANCE.getCodeSyncElement_Children();

		/**
		 * The meta object literal for the '<em><b>Feature Changes</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CODE_SYNC_ELEMENT__FEATURE_CHANGES = eINSTANCE.getCodeSyncElement_FeatureChanges();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.codesync.impl.EStructuralFeatureToFeatureChangeEntryImpl <em>EStructural Feature To Feature Change Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.codesync.impl.EStructuralFeatureToFeatureChangeEntryImpl
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getEStructuralFeatureToFeatureChangeEntry()
		 * @generated
		 */
		EClass ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY = eINSTANCE.getEStructuralFeatureToFeatureChangeEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY__KEY = eINSTANCE.getEStructuralFeatureToFeatureChangeEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ESTRUCTURAL_FEATURE_TO_FEATURE_CHANGE_ENTRY__VALUE = eINSTANCE.getEStructuralFeatureToFeatureChangeEntry_Value();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl <em>Feature Change</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.codesync.impl.FeatureChangeImpl
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getFeatureChange()
		 * @generated
		 */
		EClass FEATURE_CHANGE = eINSTANCE.getFeatureChange();

		/**
		 * The meta object literal for the '<em><b>Old Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_CHANGE__OLD_VALUE = eINSTANCE.getFeatureChange_OldValue();

		/**
		 * The meta object literal for the '<em><b>Old Value As String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_CHANGE__OLD_VALUE_AS_STRING = eINSTANCE.getFeatureChange_OldValueAsString();

		/**
		 * The meta object literal for the '<em><b>Old Value As Containment List</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_CHANGE__OLD_VALUE_AS_CONTAINMENT_LIST = eINSTANCE.getFeatureChange_OldValueAsContainmentList();

		/**
		 * The meta object literal for the '<em><b>New Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_CHANGE__NEW_VALUE = eINSTANCE.getFeatureChange_NewValue();

		/**
		 * The meta object literal for the '<em><b>New Value As String</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_CHANGE__NEW_VALUE_AS_STRING = eINSTANCE.getFeatureChange_NewValueAsString();

		/**
		 * The meta object literal for the '<em><b>New Value As Containment List</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FEATURE_CHANGE__NEW_VALUE_AS_CONTAINMENT_LIST = eINSTANCE.getFeatureChange_NewValueAsContainmentList();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.codesync.impl.CodeSyncRootImpl <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncRootImpl
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getCodeSyncRoot()
		 * @generated
		 */
		EClass CODE_SYNC_ROOT = eINSTANCE.getCodeSyncRoot();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl <em>Ast Cache Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.codesync.impl.AstCacheElementImpl
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getAstCacheElement()
		 * @generated
		 */
		EClass AST_CACHE_ELEMENT = eINSTANCE.getAstCacheElement();

		/**
		 * The meta object literal for the '<em><b>Code Sync Element</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AST_CACHE_ELEMENT__CODE_SYNC_ELEMENT = eINSTANCE.getAstCacheElement_CodeSyncElement();

		/**
		 * The meta object literal for the '{@link java.io.Serializable <em>Serializable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.io.Serializable
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getSerializable()
		 * @generated
		 */
		EClass SERIALIZABLE = eINSTANCE.getSerializable();

		/**
		 * The meta object literal for the '{@link com.crispico.flower.mp.model.codesync.impl.ScenarioElementImpl <em>Scenario Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.crispico.flower.mp.model.codesync.impl.ScenarioElementImpl
		 * @see com.crispico.flower.mp.model.codesync.impl.CodeSyncPackageImpl#getScenarioElement()
		 * @generated
		 */
		EClass SCENARIO_ELEMENT = eINSTANCE.getScenarioElement();

		/**
		 * The meta object literal for the '<em><b>Number</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_ELEMENT__NUMBER = eINSTANCE.getScenarioElement_Number();

		/**
		 * The meta object literal for the '<em><b>Interaction</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCENARIO_ELEMENT__INTERACTION = eINSTANCE.getScenarioElement_Interaction();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SCENARIO_ELEMENT__COMMENT = eINSTANCE.getScenarioElement_Comment();

	}

} //CodeSyncPackage